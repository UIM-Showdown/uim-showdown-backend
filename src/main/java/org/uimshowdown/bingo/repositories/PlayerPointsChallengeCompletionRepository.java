package org.uimshowdown.bingo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.PlayerPointsChallengeCompletion;

public interface PlayerPointsChallengeCompletionRepository extends CrudRepository<PlayerPointsChallengeCompletion, Integer> {
    Iterable<PlayerPointsChallengeCompletion> findAllByPlayerId(int playerId);
}
