package org.uimshowdown.bingo.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.Record;

public interface RecordRepository extends CrudRepository<Record, Integer> {
    Optional<Record> findBySkill(Player.Skill skill);
    Iterable<Record> findByOrderByIdAsc();
}
