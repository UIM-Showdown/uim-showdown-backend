package org.uimshowdown.bingo.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.Player;

public interface PlayerRepository extends CrudRepository<Player, Integer> {
    Optional<Player> findByDiscordName(String discordName);
    Optional<Player> findByRsn(String rsn);
    
    @Query(value = "SELECT * FROM players WHERE team_id = ?1", nativeQuery = true)
    Set<Player> getTeamRoster(int teamId);
}
