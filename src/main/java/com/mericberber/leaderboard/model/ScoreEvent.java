package com.mericberber.leaderboard.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScoreEvent {

    private Long playerId;
    private Long gameId;
    private Double score;
}
