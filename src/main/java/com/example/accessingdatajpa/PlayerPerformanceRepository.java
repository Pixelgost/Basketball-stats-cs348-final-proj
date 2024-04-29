package com.example.accessingdatajpa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface PlayerPerformanceRepository extends CrudRepository<PlayerPerformance, Long> {

    List<PlayerPerformance> findByPlayerID(int playerID);

    PlayerPerformance findById(long id);

    @Override
    Iterable<PlayerPerformance> findAll();
}