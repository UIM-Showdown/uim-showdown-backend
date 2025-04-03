package org.uimshowdown.bingo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.PlayerChallengeCompletion;

public interface PlayerChallengeCompletionRepository extends CrudRepository<PlayerChallengeCompletion, Integer> {
    Iterable<PlayerChallengeCompletion> findAllByChallengeCompletionId(int challengeCompletionId);
    Iterable<PlayerChallengeCompletion> findAllByPlayerId(int playerId);
    Iterable<PlayerChallengeCompletion> findAllByRelayComponentId(int relayComponentId);
}
