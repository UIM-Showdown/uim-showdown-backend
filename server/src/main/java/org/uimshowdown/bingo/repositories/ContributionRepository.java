package org.uimshowdown.bingo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.Contribution;

public interface ContributionRepository extends CrudRepository<Contribution, Integer> {
    Iterable<Contribution> findAllByPlayerId(int playerId);
}
