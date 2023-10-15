package com.example.demo.beans;


public class Player {
    private String name;
    private int id;

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