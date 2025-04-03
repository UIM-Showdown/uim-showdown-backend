package org.uimshowdown.bingo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.RecordHandicap;

public interface RecordHandicapRepository extends CrudRepository<RecordHandicap, Integer> {
    Iterable<RecordHandicap> findAllByRecordId(int recordId);
}
