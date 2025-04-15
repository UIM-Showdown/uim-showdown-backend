package org.uimshowdown.bingo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.Team;
import org.uimshowdown.bingo.repositories.TeamRepository;

@Component
public class EventDataInitializationService {
    
    @Autowired
    private TeamRepository teamRepository;
    
    public void addPlayer(String discordName, String rsn, Boolean isCaptain, String teamName) {
        Team team = teamRepository.findByName(teamName).get();
        Player player = new Player();
        player.setCaptainStatus(isCaptain);
        player.setDiscordName(discordName);
        player.setRsn(rsn);
        player.setTeam(team);
        team.getPlayers().add(player);
        teamRepository.save(team);
    }
    
}
