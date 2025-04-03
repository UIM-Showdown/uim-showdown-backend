package org.uimshowdown.bingo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.CollectionLogSubmission;

public interface CollectionLogSubmissionRepository extends CrudRepository<CollectionLogSubmission, Integer> {
    Iterable<CollectionLogSubmission> findAllByItemId(Integer collectionLogItemId);
}
