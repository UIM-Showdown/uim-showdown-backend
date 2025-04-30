package org.uimshowdown.bingo.controllers;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.uimshowdown.bingo.configuration.CompetitionConfiguration;
import org.uimshowdown.bingo.models.Contribution;
import org.uimshowdown.bingo.models.ContributionMethod;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.Team;
import org.uimshowdown.bingo.repositories.ContributionMethodRepository;
import org.uimshowdown.bingo.repositories.PlayerRepository;
import org.uimshowdown.bingo.repositories.TeamRepository;
import org.uimshowdown.bingo.services.DataOutputService;
import org.uimshowdown.bingo.services.EventDataInitializationService;
import org.uimshowdown.bingo.services.ScoreboardCalculationService;
import org.uimshowdown.bingo.services.TempleOsrsService;

@RestController
public class AdminController {
    
    @Autowired
    private CompetitionConfiguration competitionConfiguration;
    
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;
    
    @Autowired
    private ContributionMethodRepository contributionMethodRepository;
    
    @Autowired
    private EventDataInitializationService eventDataInitializationService;

    @Autowired
    private TempleOsrsService templeOsrsService;
    
    @Autowired
    private ScoreboardCalculationService scoreboardCalculationService;
    
    @Autowired
    DataOutputService dataOutputService;

    @PostMapping("/admin/addPlayer")
    public ResponseEntity<Void> addPlayer(@RequestBody Map<String, Object> requestBody) throws Exception {
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
    public ResponseEntity<Void> addTeam(@RequestBody Map<String, Object> requestBody) throws Exception {
        eventDataInitializationService.addTeam(
            (String) requestBody.get("name"),
            (String) requestBody.get("abbreviation"),
            (String) requestBody.get("color")
        );
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/admin/initializeCompetition")
    public ResponseEntity<Object> initializeCompetition() {
        Date now = new Date();
        if(now.after(competitionConfiguration.getStartDatetime()) && now.before(competitionConfiguration.getEndDatetime())) {
            return ResponseEntity.badRequest().body("Event currently in progress");
        }
        eventDataInitializationService.initializeCompetition();
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/admin/initializeDataOutputSheet")
    public ResponseEntity<Void> initializeDataOutputSheet() throws Exception {
        dataOutputService.initializeTabs();
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/admin/setStaffAdjustment")
    public ResponseEntity<Void> setStaffAdjustment(@RequestBody Map<String, Object> requestBody) {
        Player player = playerRepository.findByRsn((String) requestBody.get("rsn")).get();
        ContributionMethod method = contributionMethodRepository.findByName((String) requestBody.get("contributionMethodName")).get();
        for(Contribution contribution : player.getContributions()) {
            if(contribution.getContributionMethod().equals(method)) {
                contribution.setStaffAdjustment((int) requestBody.get("adjustment"));
                break;
            }
        }
        playerRepository.save(player);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/admin/updateCompetition")
    public ResponseEntity<Void> updateCompetition() throws Exception {
        long templeDuration = 0;
        long calculateDuration = 0;
        long outputDuration = 0;
        long start = 0;
        long end = 0;
        
        start = new Date().getTime();
        templeOsrsService.updateCompetition();
        end = new Date().getTime();
        templeDuration = end - start;
        System.out.println("Temple pull complete in " + templeDuration + " ms");
        
        start = new Date().getTime();
        scoreboardCalculationService.calculate();
        end = new Date().getTime();
        calculateDuration = end - start;
        System.out.println("Calculation complete in " + calculateDuration + " ms");
        
        start = new Date().getTime();
        dataOutputService.outputData();
        end = new Date().getTime();
        outputDuration = end - start;
        System.out.println("Output complete in " + outputDuration + " ms");
        
        System.out.println("Full competition update complete in " + (templeDuration + calculateDuration + outputDuration) + " ms");
        return ResponseEntity.ok().build();
    }

}
