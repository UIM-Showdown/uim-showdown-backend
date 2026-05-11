package org.uimshowdown.bingo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.PlayerSpeedChallengeCompletion;

public interface PlayerSpeedChallengeCompletionRepository extends CrudRepository<PlayerSpeedChallengeCompletion, Integer> {
    Iterable<PlayerSpeedChallengeCompletion> findAllByPlayerId(int playerId);
    Iterable<PlayerSpeedChallengeCompletion> findAllByRelayComponentId(int relayComponentId);
}
