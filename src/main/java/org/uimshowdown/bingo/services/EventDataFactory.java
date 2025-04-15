package org.uimshowdown.bingo.services;

import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.Team;

public class EventDataFactory {
    public static Player createPlayer(String discordName, String rsn, Boolean isCaptain, Team team) {
        Player player = new Player();
        player.setCaptainStatus(isCaptain);
        player.setDiscordName(discordName);
        player.setRsn(rsn);
        player.setTeam(team);
        return player;
    }
}
