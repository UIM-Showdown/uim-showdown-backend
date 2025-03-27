package org.uimshowdown.bingo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.ChallengeCompletion;

public interface ChallengeCompletionRepository extends CrudRepository<ChallengeCompletion, Integer> {
    Iterable<ChallengeCompletion> findAllByChallengeId(Integer challengeId);
    Iterable<ChallengeCompletion> findAllByTeamId(Integer teamId);
}
