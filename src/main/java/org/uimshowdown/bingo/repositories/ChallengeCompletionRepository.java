package org.uimshowdown.bingo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.ChallengeCompletion;

public interface ChallengeCompletionRepository extends CrudRepository<ChallengeCompletion, Integer> {
    Iterable<ChallengeCompletion> findAllByChallengeId(int challengeId);
    Iterable<ChallengeCompletion> findAllByTeamId(int teamId);
}
