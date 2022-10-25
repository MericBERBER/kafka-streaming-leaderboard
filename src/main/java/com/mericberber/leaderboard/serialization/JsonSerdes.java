package com.mericberber.leaderboard.serialization;


import com.mericberber.leaderboard.model.Game;
import com.mericberber.leaderboard.model.HighScores;
import com.mericberber.leaderboard.model.Player;
import com.mericberber.leaderboard.model.ScoreEvent;
import com.mericberber.leaderboard.model.join.Enriched;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public class JsonSerdes {

    public static Serde<HighScores> HighScores() {
        JsonSerializer<HighScores> serializer = new JsonSerializer<>();
        JsonDeserializer<HighScores> deserializer = new JsonDeserializer<>(HighScores.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }

    public static Serde<Enriched> Enriched() {
        JsonSerializer<Enriched> serializer = new JsonSerializer<>();
        JsonDeserializer<Enriched> deserializer = new JsonDeserializer<>(Enriched.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }

    public static Serde<ScoreEvent> ScoreEvent() {
        JsonSerializer<ScoreEvent> serializer = new JsonSerializer<>();
        JsonDeserializer<ScoreEvent> deserializer = new JsonDeserializer<>(ScoreEvent.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }

    public static Serde<Player> Player() {
        JsonSerializer<Player> serializer = new JsonSerializer<>();
        JsonDeserializer<Player> deserializer = new JsonDeserializer<>(Player.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }

    public static Serde<Game> Game() {
        JsonSerializer<Game> serializer = new JsonSerializer<>();
        JsonDeserializer<Game> deserializer = new JsonDeserializer<>(Game.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }
}