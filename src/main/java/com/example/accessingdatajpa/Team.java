package com.example.accessingdatajpa;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
@Entity
public class Team {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String name;

    private int teamCaptain;

    private int rosterSize;
    protected Team() {}

    public Team(String name, int firstPlayer) {
        this.name = name;
        this.teamCaptain = firstPlayer;
        this.rosterSize = 1;
    }

    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTeamCaptain() {
        return teamCaptain;
    }

    public void addPlayer() {
        this.rosterSize ++;
    }

    public void removePlayer() {
        this.rosterSize -= 1;
    }

    public int getRosterSize() {
        return rosterSize;
    }

    public void setTeamCaptain(int teamCaptain) {
        this.teamCaptain = teamCaptain;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", teamCaptain=" + teamCaptain +
                '}';
    }
}
