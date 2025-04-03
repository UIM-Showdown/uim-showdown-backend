package org.uimshowdown.bingo.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.TeamScoreboard;

public interface TeamScoreboardRepository extends CrudRepository<TeamScoreboard, Integer> {
    Optional<TeamScoreboard> findByTeamId(Integer teamId);
}
