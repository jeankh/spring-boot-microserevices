package com.example.demo.beans;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Player details")
public class Player {
    @ApiModelProperty(notes = "The unique identifier for the player")
    private int id;

    @ApiModelProperty(notes = "The name of the player")
    private String name;
    public Player() {
        // Default constructor
    }

    public Player(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Player [name=" + name + ", id=" + id + "]";
    }
}