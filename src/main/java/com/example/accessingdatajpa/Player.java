package com.example.accessingdatajpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Iterator;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;

    private int number;

    private int position;
    private String teamName;

    private int ppg;

    private int apg;

    private int rpg;

    private int spg;

    private int bpg;

    private int mpg;

    private int st;
    private int gp = 0;

    protected Player() {}

    public Player(String firstName, String lastName, int number, int position, String teamName, TeamRepository tr) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.number = number;
        this.position = position;
        this.teamName = teamName;

        this.apg = 0;
        this.bpg = 0;
        this.ppg = 0;
        this.rpg = 0;
        this.spg = 0;
        this.mpg = 0;
        this.gp = 0;
        Iterator<Team> t = tr.findByName(teamName).iterator();
        if (!t.hasNext()) {
            Team curr = new Team(teamName, number);
            tr.save(curr);
        } else {
            Team curr = t.next();
            curr.addPlayer();
            tr.save(curr);
        }
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", number=" + number +
                ", position=" + position +
                ", teamName='" + teamName + '\'' +
                ", ppg=" + ppg +
                ", apg=" + apg +
                ", rpg=" + rpg +
                ", spg=" + spg +
                ", bpg=" + bpg +
                ", mpg=" + mpg +
                ", gp=" + gp +
                '}';
    }


    public Long getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getPosition() {
        return position;
    }

    public int getPpg() {
        return ppg;
    }

    public void setPpg(int ppg) {
        this.ppg = ppg;
    }

    public int getApg() {
        return apg;
    }

    public void setApg(int apg) {
        this.apg = apg;
    }

    public int getRpg() {
        return rpg;
    }

    public void setRpg(int rpg) {
        this.rpg = rpg;
    }

    public int getSpg() {
        return spg;
    }

    public void setSpg(int spg) {
        this.spg = spg;
    }

    public int getBpg() {
        return bpg;
    }

    public void setBpg(int bpg) {
        this.bpg = bpg;
    }

    public int getMpg() {
        return mpg;
    }

    public void setMpg(int mpg) {
        this.mpg = mpg;
    }

    public int getGp() {
        return gp;
    }

    public void setGp(int gp) {
        this.gp = gp;
    }

    public int getSt() {
        return st;
    }

    public void setSt(int st) {
        this.st = st;
    }
}