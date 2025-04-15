package org.uimshowdown.bingo.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.Team;
import org.uimshowdown.bingo.repositories.PlayerRepository;
import org.uimshowdown.bingo.repositories.TeamRepository;
import org.uimshowdown.bingo.services.EventDataFactory;

@RestController
public class AdminController {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @PostMapping("/admin/addPlayer")
    public ResponseEntity<Void> addPlayer(@RequestBody Map<String, Object> requestBody) {
        Team team = teamRepository.findByName((String) requestBody.get("teamName")).get();
        Player newPlayer = EventDataFactory.createPlayer(
            (String) requestBody.get("discordName"),
            (String) requestBody.get("rsn"),
            (Boolean) requestBody.get("isCaptain"),
            team
        );

        team.getPlayers().add(newPlayer);
        teamRepository.save(team);
        return ResponseEntity.ok().build();
    }
}
