package org.uimshowdown.bingo.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.Record;

public interface RecordRepository extends CrudRepository<Record, Integer> {
    Optional<Record> findBySkill(String name);
}
