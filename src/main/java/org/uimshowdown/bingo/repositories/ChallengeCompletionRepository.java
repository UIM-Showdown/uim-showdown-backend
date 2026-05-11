package org.uimshowdown.bingo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.SpeedChallengeCompletion;

public interface ChallengeCompletionRepository extends CrudRepository<SpeedChallengeCompletion, Integer> {
    Iterable<SpeedChallengeCompletion> findAllByChallengeId(int challengeId);
    Iterable<SpeedChallengeCompletion> findAllByTeamId(int teamId);
}
