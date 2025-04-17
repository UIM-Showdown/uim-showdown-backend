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
import org.uimshowdown.bingo.services.EventDataInitializationService;
import org.uimshowdown.bingo.services.TempleOsrsService;

@RestController
public class AdminController {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;
    
    @Autowired
    private EventDataInitializationService eventDataInitializationService;

    @Autowired
    private TempleOsrsService templeOsrsService;

    @PostMapping("/admin/addPlayer")
    public ResponseEntity<Void> addPlayer(@RequestBody Map<String, Object> requestBody) {
        eventDataInitializationService.addPlayer(
            (String) requestBody.get("discordName"),
            (String) requestBody.get("rsn"),
            (Boolean) requestBody.get("isCaptain"),
            (String) requestBody.get("teamName")
        );
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/admin/changePlayerTeam")
    public ResponseEntity<Void> changePlayerTeam(@RequestBody Map<String, Object> requestBody) {
        Player player = playerRepository.findByRsn((String) requestBody.get("rsn")).get();
        Team newTeam = teamRepository.findByName((String) requestBody.get("teamName")).get();

        player.setTeam(newTeam);
        playerRepository.save(player);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/admin/renamePlayer")
    public ResponseEntity<Void> renamePlayer(@RequestBody Map<String, Object> requestBody) {
        Player player = playerRepository.findByRsn((String) requestBody.get("oldRsn")).get();
        player.setRsn((String) requestBody.get("newRsn"));
        playerRepository.save(player);
        
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/admin/addTeam")
    public ResponseEntity<Void> addTeam(@RequestBody Map<String, Object> requestBody) {
        eventDataInitializationService.addTeam(
            (String) requestBody.get("name"),
            (String) requestBody.get("abbreviation"),
            (String) requestBody.get("color")
        );
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/admin/pullTemple")
    public ResponseEntity<Void> pullTemple() {
        templeOsrsService.updateCompetition();
        return ResponseEntity.ok().build();
    }

}
