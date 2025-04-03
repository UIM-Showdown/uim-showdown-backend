package org.uimshowdown.bingo.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.Challenge;

public interface ChallengeRepository extends CrudRepository<Challenge, Integer> {
    Optional<Challenge> findByName(String name);
}
