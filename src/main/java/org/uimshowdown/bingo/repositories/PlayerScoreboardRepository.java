package org.uimshowdown.bingo.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.PlayerScoreboard;

public interface PlayerScoreboardRepository extends CrudRepository<PlayerScoreboard, Integer> {
    Optional<PlayerScoreboard> findByPlayerId(int playerId);
}
