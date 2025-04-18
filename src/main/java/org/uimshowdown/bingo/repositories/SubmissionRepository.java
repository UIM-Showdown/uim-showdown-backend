package org.uimshowdown.bingo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.Submission;

public interface SubmissionRepository extends CrudRepository<Submission, Integer> {
    Iterable<Submission> findAllByPlayerId(int playerId);
    Iterable<Submission> findAllByState(Submission.State state);
}
