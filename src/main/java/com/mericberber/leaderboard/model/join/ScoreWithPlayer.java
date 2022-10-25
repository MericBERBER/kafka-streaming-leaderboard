package com.mericberber.leaderboard.model.join;

import com.mericberber.leaderboard.model.Player;
import com.mericberber.leaderboard.model.ScoreEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScoreWithPlayer {

    private ScoreEvent scoreEvent;
    private Player player;
}
