package com.isssr.ticketing_system.dao;

import com.isssr.ticketing_system.entity.Setup;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SetupRepository extends CrudRepository<Setup, Long> {

    Iterable<Setup> findAll();

    Optional<Setup> findById(Setup setup);

    boolean existsBySetup(boolean flag);

}
