package org.uimshowdown.bingo.controllers;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.uimshowdown.bingo.models.Team;
import org.uimshowdown.bingo.repositories.TeamRepository;

@RestController
public class TeamController {
    
    @Autowired
    private TeamRepository teamRepository;
    
    @GetMapping("/teams")
    public Set<Team> getTeams() {
        Set<Team> teams = new HashSet<Team>();
        for(Team team : teamRepository.findAll()) {
            teams.add(team);
        }
        return teams;
    }

}
