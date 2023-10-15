package com.example.demo.beans;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Team statistics")
public class TeamStats {
    @ApiModelProperty(notes = "Team ID")
    private int teamId;

    @ApiModelProperty(notes = "Number of matches won by the team")
    private int matchesWon;

    public TeamStats() {
    }

    public TeamStats(int teamId, int matchesWon) {
        this.teamId = teamId;
        this.matchesWon = matchesWon;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getMatchesWon() {
        return matchesWon;
    }

    public void setMatchesWon(int matchesWon) {
        this.matchesWon = matchesWon;
    }
}
