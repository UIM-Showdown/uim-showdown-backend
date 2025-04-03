package org.uimshowdown.bingo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.RecordSubmission;

public interface RecordSubmissionRepository extends CrudRepository<RecordSubmission, Integer> {
    Iterable<RecordSubmission> findAllByRecordId(Integer recordId);
}
