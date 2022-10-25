package com.mericberber.leaderboard.streams;

import com.mericberber.leaderboard.constants.LeaderboardConstants;
import com.mericberber.leaderboard.model.Game;
import com.mericberber.leaderboard.model.HighScores;
import com.mericberber.leaderboard.model.Player;
import com.mericberber.leaderboard.model.ScoreEvent;
import com.mericberber.leaderboard.model.join.Enriched;
import com.mericberber.leaderboard.model.join.ScoreWithPlayer;
import com.mericberber.leaderboard.serialization.JsonSerdes;
import org.apache.kafka.clients.consumer.StickyAssignor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;

public class LeaderboardStreamingTopology {

    public static Topology build(){

        StreamsBuilder streamsBuilder = new StreamsBuilder();

        KStream<String, ScoreEvent> scoreEventKStream = streamsBuilder
                .stream(LeaderboardConstants.SCORE_EVENTS_TOPIC, Consumed.with(Serdes.ByteArray(), JsonSerdes.ScoreEvent()))
                .selectKey((k,v) -> v.getPlayerId().toString());

        KTable<String, Player> playerKTable = streamsBuilder
                .table(LeaderboardConstants.PLAYERS_TOPIC, Consumed.with(Serdes.String(), JsonSerdes.Player()));

        GlobalKTable<String, Game> gameGlobalKTable = streamsBuilder
                .globalTable(LeaderboardConstants.GAMES_TOPIC, Consumed.with(Serdes.String(), JsonSerdes.Game()));

        ValueJoiner<ScoreEvent, Player, ScoreWithPlayer> scoreEventPlayerJoiner = new ValueJoiner<ScoreEvent, Player, ScoreWithPlayer>() {
            @Override
            public ScoreWithPlayer apply(ScoreEvent scoreEvent, Player player) {
                return new ScoreWithPlayer(scoreEvent, player);
            }
        };

        KStream<String, ScoreWithPlayer> scoreWithPlayerKStream = scoreEventKStream
                .join(playerKTable, scoreEventPlayerJoiner,
                        Joined.with(Serdes.String(), JsonSerdes.ScoreEvent(), JsonSerdes.Player()));


        KeyValueMapper<String, ScoreWithPlayer, String> keyValueMapper = new KeyValueMapper<String, ScoreWithPlayer, String>() {
            @Override
            public String apply(String key, ScoreWithPlayer scoreWithPlayer) {
                return String.valueOf(scoreWithPlayer.getScoreEvent().getGameId());
            }
        };

        ValueJoiner<ScoreWithPlayer, Game, Enriched> scoreWithPlayerGameJoiner = new ValueJoiner<ScoreWithPlayer, Game, Enriched>() {
            @Override
            public Enriched apply(ScoreWithPlayer scoreWithPlayer, Game game) {
                return new Enriched(scoreWithPlayer, game);
            }
        };

        KStream<String, Enriched> enrichedKStream = scoreWithPlayerKStream.join(gameGlobalKTable, keyValueMapper, scoreWithPlayerGameJoiner);
        enrichedKStream.print(Printed.<String, Enriched>toSysOut().withLabel("Enriched-KStream"));

        KGroupedStream<String, Enriched> grouped =
                enrichedKStream.groupBy(
                        (key, value) -> value.getGameId().toString(),
                        Grouped.with(Serdes.String(), JsonSerdes.Enriched()));

        /** The initial value of our aggregation will be a new HighScores instances */
        Initializer<HighScores> highScoresInitializer = HighScores::new;

        /** The logic for aggregating high scores is implemented in the HighScores.add method */
        Aggregator<String, Enriched, HighScores> highScoresAdder =
                (key, value, aggregate) -> aggregate.add(value);

        KTable<String, HighScores> highScores =
                grouped.aggregate(
                        highScoresInitializer,
                        highScoresAdder,
                        Materialized.<String, HighScores, KeyValueStore<Bytes, byte[]>>
                                // give the state store an explicit name to make it available for interactive
                                // queries
                                        as(LeaderboardConstants.LEADERBOARD_STATE_STORE)
                                .withKeySerde(Serdes.String())
                                .withValueSerde(JsonSerdes.HighScores()));

        highScores.toStream().to(LeaderboardConstants.HIGH_SCORES_TOPIC);

        return streamsBuilder.build();
    }
}
