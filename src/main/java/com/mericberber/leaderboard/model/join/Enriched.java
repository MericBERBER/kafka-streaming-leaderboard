package com.mericberber.leaderboard.model.join;

import com.mericberber.leaderboard.model.Game;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Enriched implements Comparable<Enriched> {

    private Long playerId;
    private Long gameId;
    private String playerName;
    private String gameName;
    private Double score;

    public Enriched(ScoreWithPlayer scoreEventWithPlayer, Game game) {
        this.playerId = scoreEventWithPlayer.getPlayer().getId();
        this.gameId = game.getId();
        this.playerName = scoreEventWithPlayer.getPlayer().getName();
        this.gameName = game.getName();
        this.score = scoreEventWithPlayer.getScoreEvent().getScore();
    }

    @Override
    public int compareTo(Enriched o) {
        return Double.compare(o.score, score);
    }
}
