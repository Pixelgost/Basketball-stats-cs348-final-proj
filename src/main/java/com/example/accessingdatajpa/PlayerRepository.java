package com.example.accessingdatajpa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface PlayerRepository extends CrudRepository<Player, Long> {

    List<Player> findByLastName(String lastName);

    List<Player> findByFirstName(String lastName);

    List<Player> findByFirstNameAndLastName(String firstName, String lastName);

    List<Player> findByPosition(int position);

    @Override
    Iterable<Player> findAll();
    List<Player> findByTeamName(String teamName);

    List<Player> findByNumber(int number);
    List<Player> findByNumberAndTeamName(int number, String teamName);

    List<Player> findByPositionAndTeamName(int position, String teamName);

    Player findById(long id);
}