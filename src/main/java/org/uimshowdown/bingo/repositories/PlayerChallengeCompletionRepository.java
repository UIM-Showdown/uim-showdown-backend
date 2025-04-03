package org.uimshowdown.bingo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.PlayerChallengeCompletion;

public interface PlayerChallengeCompletionRepository extends CrudRepository<PlayerChallengeCompletion, Integer> {
    Iterable<PlayerChallengeCompletion> findAllByChallengeCompletionId(Integer challengeCompletionId);
    Iterable<PlayerChallengeCompletion> findAllByPlayerId(Integer playerId);
    Iterable<PlayerChallengeCompletion> findAllByRelayComponentId(Integer relayComponentId);
}
