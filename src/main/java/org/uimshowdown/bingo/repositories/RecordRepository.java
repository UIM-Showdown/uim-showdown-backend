package org.uimshowdown.bingo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.Record;

public interface RecordRepository extends CrudRepository<Record, Integer> {
    // No other query methods implemented.
}
