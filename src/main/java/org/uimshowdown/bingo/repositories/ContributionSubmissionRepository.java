package org.uimshowdown.bingo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.ContributionSubmission;

public interface ContributionSubmissionRepository extends CrudRepository<ContributionSubmission, Integer> {
    Iterable<ContributionSubmission> findAllByContributionMethodId(Integer contributionMethodId);
}
