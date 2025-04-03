package org.uimshowdown.bingo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.UnrankedStartingValueSubmission;

public interface UnrankedStartingValueSubmissionRepository extends CrudRepository<UnrankedStartingValueSubmission, Integer> {
    Iterable<UnrankedStartingValueSubmission> findAllByContributionMethodId(int contributionMethodId);
}
