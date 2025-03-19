package org.uimshowdown.bingo.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.uimshowdown.bingo.models.Player;

public interface PlayerRepository extends CrudRepository<Player, Integer> {
    Optional<Player> findByDiscordName(String discordName);
    Optional<Player> findByRsn(String rsn);
}
