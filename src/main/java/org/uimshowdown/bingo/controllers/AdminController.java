package org.uimshowdown.bingo.controllers;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
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
    
    @Autowired Environment environment;
    
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
        if(!contributionMethodRepository.findAll().iterator().hasNext()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Competition has not been initialized");
        }
        if(!teamRepository.findAll().iterator().hasNext()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Teams have not been created");
        }
        
        templeOsrsService.updateCompetition();
        scoreboardCalculationService.calculate();
        dataOutputService.outputData();
        
        return ResponseEntity.ok().build();
    }
    
    /**
     * Automatically calls the updateCompetition() endpoint method at the top of every minute.
     * 
     * This method is annotated with "Transactional" because it's not in a request context and therefore doesn't 
     * automatically have a transaction created. The "readOnly" value is a misnomer - it won't fail upon trying to write;
     * it just signals to the transaction manager that certain optimizations can be taken that work best when it's not writing 
     * (which makes it significantly faster anyway even though we're doing writes).
     * @throws Exception
     */
    @Scheduled(cron = "0 * * * * *")
    @Transactional(readOnly=true)
    public void updateCompetitionScheduled() throws Exception {
        for(String profile : environment.getActiveProfiles()) {
            if(profile.equals("test")) { // We're in the middle of JUnit tests
                return;
            }
        }
        try {            
            updateCompetition();
        } catch(ResponseStatusException e) {} // This means the comp isn't initialized, so we don't need to worry about this failing
    }

}
