package org.uimshowdown.bingo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.ChallengeSubmission;

public interface ChallengeSubmissionRepository extends CrudRepository<ChallengeSubmission, Integer> {
    Iterable<ChallengeSubmission> findAllByChallengeId(Integer challengeId);
    Iterable<ChallengeSubmission> findAllByRelayComponentId(Integer relayComponentId);
    Iterable<ChallengeSubmission> findAllBySubmissionId(Integer submissionId);
}
