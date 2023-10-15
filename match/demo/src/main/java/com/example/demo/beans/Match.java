package com.example.demo.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonProperty;

@ApiModel(description = "Match entity")
public class Match {
    @JsonProperty("name")
    @ApiModelProperty(value = "Match name", example = "Example Match")
    private String name;

    @JsonProperty("id")
    @ApiModelProperty(value = "Match ID", example = "1")
    private Integer id;

    @JsonProperty("team1")
    @ApiModelProperty(value = "First team in the match")
    private Team team1;

    @JsonProperty("team2")
    @ApiModelProperty(value = "Second team in the match")
    private Team team2;

    @JsonProperty("winner")
    @ApiModelProperty(value = "Match winner")
    private Team winner;

    @JsonProperty("scorers")
    @ApiModelProperty(value = "List of players who scored in the match")
    private List<Player> scorers;
    public Match() {
    }

    public Match(Integer id, String name, Team team1, Team team2) {
        this.id = id;
        this.name = name;
        this.team1 = team1;
        this.team2 = team2;
        simulateMatch();
    }
    private void simulateMatch() {
        Random random = new Random();
        int winnerIndex = random.nextInt(2); // Randomly choose 0 or 1 for team1 or team2

        if (winnerIndex == 0) {
            winner = team1;
        } else {
            winner = team2;
        }

        List<Player> winnerPlayers = winner.getPlayers();
        if (!winnerPlayers.isEmpty()) {
            int scorerIndex = random.nextInt(winnerPlayers.size());
            Player scorer = winnerPlayers.get(scorerIndex);
            if (scorers == null) {
                scorers = new ArrayList<>();
            }
            scorers.add(scorer);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Team getTeam1() {
        return team1;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

    public Team getWinner() {
        return winner;
    }

    public void setWinner(Team winner) {
        this.winner = winner;
    }

    public List<Player> getScorers() {
        return scorers;
    }

    public void setScorers(List<Player> scorers) {
        this.scorers = scorers;
    }

    @Override
    public String toString() {
        return "Match [name=" + name + ", id=" + id + ", team1=" + team1.getName() +
                ", team2=" + team2.getName() + ", winner=" + (winner != null ? winner.getName() : "No winner") +
                ", scorers=" + scorers + "]";
    }
}