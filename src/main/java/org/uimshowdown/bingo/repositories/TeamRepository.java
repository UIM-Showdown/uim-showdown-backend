package org.uimshowdown.bingo.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.Team;

public interface TeamRepository extends CrudRepository<Team, Integer> {
    Optional<Team> findByName(String name);
    Iterable<Team> findByOrderByIdAsc();
    long count();
}
