package com.example.demo.beans;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Player statistics")
public class PlayerStats {
    @ApiModelProperty(notes = "Player ID")
    private int playerId;

    @ApiModelProperty(notes = "Number of goals scored by the player")
    private int goalsScored;

    public PlayerStats() {
    }

    public PlayerStats(int playerId, int goalsScored) {
        this.playerId = playerId;
        this.goalsScored = goalsScored;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getGoalsScored() {
        return goalsScored;
    }

    public void setGoalsScored(int goalsScored) {
        this.goalsScored = goalsScored;
    }
}
