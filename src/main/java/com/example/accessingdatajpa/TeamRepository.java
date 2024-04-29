package com.example.accessingdatajpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<Team, Long> {
    @Override
    Optional<Team> findById(Long aLong);

    @Override
    Iterable<Team> findAll();

    Iterable<Team> findByName(String name);
}