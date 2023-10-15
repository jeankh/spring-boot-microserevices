package com.example.demo.beans;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Team entity")
public class Team {

    @ApiModelProperty(value = "The unique identifier for the team")
    private Integer id;

    @ApiModelProperty(value = "The name of the team")
    private String name;

    @ApiModelProperty(value = "List of players in the team")
    private List<Player> players;


    public Team() {

    }
    public Team(Integer id, String name,List<Player> players) {
        super();
        this.id = id;
        this.name = name;
        this.players = players;
    }

    public List<Player> getPlayers(){
        return  players;
    }

    public void setPlayers(List<Player> players){
        this.players=players;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Team [id=" + id + ", name=" + name+ " players =[" +players.toString() +"]\n] ";
    }
}