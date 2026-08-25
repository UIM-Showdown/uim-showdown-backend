package org.uimshowdown.bingo.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.RecordHandicap;

public interface RecordHandicapRepository extends CrudRepository<RecordHandicap, Integer> {
    Iterable<RecordHandicap> findAllByRecordId(int recordId);
    Optional<RecordHandicap> findByName(String name);
}
