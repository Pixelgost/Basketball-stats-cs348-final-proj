package com.example.accessingdatajpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.List;

@Entity
public class PlayerPerformance {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private Long playerID;

    private int points;

    private int assists;

    private int rebounds;

    private int steals;

    private int blocks;

    private int minutes;

    private int score;


    protected PlayerPerformance() {}

    public PlayerPerformance (int points, int assists, int rebounds, int steals,
                              int blocks, int minutes, String firstName, String lastName, int number,
                              int position, String teamName, PlayerRepository repository, TeamRepository tr) {
        this.points = points;
        this.assists = assists;
        this.rebounds = rebounds;
        this.steals = steals;
        this.blocks = blocks;
        this.score = points + (2 * (assists + blocks)) + (5 * (steals + blocks));
        this.minutes = minutes;
        List<Player> players = repository.findByNumberAndTeamName(number, teamName);
        if (players.size() > 0) {
            this.playerID = players.get(0).getId();
            Player player = players.get(0);
            player.setPpg(player.getPpg() + points);
            player.setApg(player.getApg() + assists);
            player.setRpg(player.getRpg() + rebounds);
            player.setMpg(player.getMpg() + minutes);
            player.setBpg(player.getBpg() + blocks);
            player.setSpg(player.getSpg() + steals);
            player.setGp(player.getGp() + 1);
            player.setSt(player.getSt() + this.score);
            repository.save(player);

        } else {
            Player curr = new Player(firstName, lastName, number, position, teamName, tr);
            curr.setPpg(curr.getPpg() + points);
            curr.setApg(curr.getApg() + assists);
            curr.setRpg(curr.getRpg() + rebounds);
            curr.setMpg(curr.getMpg() + minutes);
            curr.setBpg(curr.getBpg () + blocks);
            curr.setSpg(curr.getSpg() + steals);
            curr.setGp(curr.getGp() + 1);
            curr.setSt(curr.getSt() + this.score);
            repository.save(curr);
            this.playerID = curr.getId();
        }
    }
    public PlayerPerformance (int points, int assists, int rebounds, int steals,
                              int blocks, int minutes, int playerNum,
                              String playerTeam, PlayerRepository repository) throws Exception {
        this.points = points;
        this.assists = assists;
        this.rebounds = rebounds;
        this.steals = steals;
        this.blocks = blocks;
        this.minutes = minutes;
        this.score = points + (2 * (assists + blocks)) + (5 * (steals + blocks));
        List<Player> players = repository.findByNumberAndTeamName(playerNum, playerTeam);
        if (players.size() > 0) {
            this.playerID = players.get(0).getId();
            Player player = players.get(0);
            player.setPpg(player.getPpg() + points);
            player.setApg(player.getApg() + assists);
            player.setRpg(player.getRpg() + rebounds);
            player.setMpg(player.getMpg() + minutes);
            player.setBpg(player.getBpg() + blocks);
            player.setSpg(player.getSpg() + steals);
            player.setGp(player.getGp() + 1);
            player.setSt(player.getSt() + this.score);
            repository.save(player);
        } else {
            throw new Exception("Cannot find specified player in the database, more information must be provided");
        }
    }


    @Override
    public String toString() {
        return "PlayerPerformance{" +
                "id=" + id +
                ", playerID=" + playerID +
                ", points=" + points +
                ", assists=" + assists +
                ", rebounds=" + rebounds +
                ", steals=" + steals +
                ", blocks=" + blocks +
                ", minutes=" + minutes;
    }
    public int calculateScore() {
        return (points + (2 * (assists + blocks)) + (5 * (steals + blocks)));
    }
    public int getAssists() {
        return assists;
    }

    public Long getPlayerID() {
        return playerID;
    }

    public int getPoints() {
        return points;
    }

    public int getRebounds() {
        return rebounds;
    }

    public int getSteals() {
        return steals;
    }

    public int getBlocks() {
        return blocks;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setPlayerID(Long playerID) {
        this.playerID = playerID;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public void setRebounds(int rebounds) {
        this.rebounds = rebounds;
    }

    public void setSteals(int steals) {
        this.steals = steals;
    }

    public void setBlocks(int blocks) {
        this.blocks = blocks;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Long getId() {
        return id;
    }
}
