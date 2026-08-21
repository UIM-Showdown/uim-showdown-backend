package org.uimshowdown.bingo.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.uimshowdown.bingo.configuration.CompetitionConfiguration;
import org.uimshowdown.bingo.models.Contribution;
import org.uimshowdown.bingo.models.ContributionMethod;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.Team;
import org.uimshowdown.bingo.models.Tile;
import org.uimshowdown.bingo.repositories.ContributionMethodRepository;
import org.uimshowdown.bingo.repositories.PlayerRepository;
import org.uimshowdown.bingo.repositories.TeamRepository;
import org.uimshowdown.bingo.repositories.TileRepository;
import org.uimshowdown.bingo.services.DataOutputService;
import org.uimshowdown.bingo.services.DiscordService;
import org.uimshowdown.bingo.services.EventDataInitializationService;
import org.uimshowdown.bingo.services.GoogleSheetsService;
import org.uimshowdown.bingo.services.ScoreboardCalculationService;
import org.uimshowdown.bingo.services.StatsService;
import org.uimshowdown.bingo.services.TempleOsrsService;

@RestController
public class AdminController {
    
    @Autowired Environment environment;
    
    @Autowired
    private CompetitionConfiguration competitionConfiguration;
    
    @Autowired
    private TileRepository tileRepository;
    
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
    
    @Autowired
    GoogleSheetsService googleSheetsService;
    
    @Autowired
    StatsService statsService;
    
    @Autowired
    DiscordService discordService;
    
    @Value("${discord.guildId}")
    long guildId;

    @PostMapping("/admin/addPlayer")
    public ResponseEntity<Void> addPlayer(@RequestBody Map<String, Object> requestBody, 
            @RequestParam(defaultValue = "true", required = false) boolean synchronizeTempleComp) throws Exception {
        
        eventDataInitializationService.addPlayer(
                (String) requestBody.get("discordName"),
                (String) requestBody.get("rsn"),
                (String) requestBody.get("teamName")
            );
        
        discordService.addRoleToUser((String) requestBody.get("teamName"), (String) requestBody.get("discordName"));
        
        if(synchronizeTempleComp) {
            templeOsrsService.synchronizeRosters();
        }
        
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/admin/changePlayerTeam")
    public ResponseEntity<Void> changePlayerTeam(@RequestBody Map<String, Object> requestBody,
            @RequestParam(defaultValue = "true", required = false) boolean synchronizeTempleComp) throws Exception {
        
        Player player = playerRepository.findByRsn((String) requestBody.get("rsn")).get();
        Team oldTeam = player.getTeam();
        Team newTeam = teamRepository.findByName((String) requestBody.get("teamName")).get();

        player.setTeam(newTeam);
        playerRepository.save(player);
        
        discordService.removeRoleFromUser(oldTeam.getName(), player.getDiscordName());
        discordService.addRoleToUser(newTeam.getName(), player.getDiscordName());
        
        if(synchronizeTempleComp) {
            templeOsrsService.synchronizeRosters();
        }
        
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/admin/synchronizeTempleComp")
    public ResponseEntity<Void> synchronizeTempleComp() {
        templeOsrsService.synchronizeRosters();
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/admin/changePlayerRsn")
    public ResponseEntity<Void> changePlayerRsn(@RequestBody Map<String, Object> requestBody,
            @RequestParam(defaultValue = "true", required = false) boolean synchronizeTempleComp) {
        Player player = playerRepository.findByRsn((String) requestBody.get("oldRsn")).get();
        player.setRsn((String) requestBody.get("newRsn"));
        playerRepository.save(player);
        
        if(synchronizeTempleComp) {
            templeOsrsService.synchronizeRosters();
        }
        
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/admin/changePlayerDiscordName")
    public ResponseEntity<Void> changePlayerDiscordName(@RequestBody Map<String, Object> requestBody) {
        Player player = playerRepository.findByDiscordName((String) requestBody.get("oldDiscordName")).get();
        player.setDiscordName((String) requestBody.get("newDiscordName"));
        playerRepository.save(player);
        
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/admin/initializeCompetition")
    public ResponseEntity<Object> initializeCompetition() throws Exception {
        if(eventInProgress()) {
            return ResponseEntity.badRequest().body("Event currently in progress");
        }
        eventDataInitializationService.initializeCompetition();
        dataOutputService.initializeTabs();
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/admin/initializeTabs")
    public ResponseEntity<Object> initializeTabs() throws Exception {
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
                contribution.setIsEmpty(false);
                break;
            }
        }
        playerRepository.save(player);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/admin/updateCompetition")
    public ResponseEntity<String> updateCompetition(@RequestParam(defaultValue = "false", required = false) boolean force) throws Exception {
        if(!eventInitialized()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Competition has not been initialized");
        }
        
        if(!force && !eventInProgress()) {
            return ResponseEntity.badRequest().body("Event not currently in progress");
        }
        
        long start = new Date().getTime();
        templeOsrsService.updateCompetition();
        scoreboardCalculationService.calculate();
        dataOutputService.outputData();
        long end = new Date().getTime();
        System.out.println("Performed a competition update in " + (end - start) + " ms");
        
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/admin/reinitializeTile/{name}")
    public ResponseEntity<Void> reinitializeTile(@PathVariable String name) {
        Tile tile = tileRepository.findByName(name).orElse(null);
        if(tile == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tile not found with that name");
        }
        eventDataInitializationService.reinitializeTile(tile.getId());
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/admin/updateCompetitorRole")
    public Map<String, Object> updateCompetitorRole() throws Exception {
        List<Map<String, String>> signupsNotFound = new ArrayList<Map<String, String>>();
        Map<String, Object> result = new HashMap<String, Object>();
        for(Map<String, String> signup : googleSheetsService.getSignups()) {
            String discordName = signup.get("discordName");
            if(!discordService.isUserInServer(discordName)) {
                signupsNotFound.add(signup);
            } else {
                discordService.addRoleToUser("Competitor", discordName);
            }
        }
        result.put("signupsNotFound", signupsNotFound);
        return result;
    }
    
    @PostMapping("/admin/setupDiscordServer")
    public Map<String, Object> setupDiscordServer() throws Exception {
        
        List<String> namesNotFound = new ArrayList<String>();
        List<Team> teams = new ArrayList<Team>();
        for(Team team : teamRepository.findByOrderByIdAsc()) {
            for(Player player : team.getPlayers()) {
                if(!discordService.isUserInServer(player.getDiscordName())) {
                    namesNotFound.add(player.getDiscordName());
                }
            }
            teams.add(team);
        }
        discordService.setupDiscordServer(teams);
        
        
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("namesNotFound", namesNotFound);
        return result;
    }
    
    @PostMapping("/admin/teardownDiscordServer")
    public void teardownDiscordServer() throws Exception {
        List<Team> teams = new ArrayList<Team>();
        for(Team team : teamRepository.findByOrderByIdAsc()) {
            teams.add(team);
        }
        discordService.teardownDiscordServer(teams);
    }
    
    @GetMapping("/admin/stats")
    public Map<String, Object> getStats() throws Exception {
        return statsService.getStatsReport();
    }
    
    private boolean eventInProgress() {
        Date now = new Date();
        return now.after(competitionConfiguration.getStartDatetime()) && now.before(competitionConfiguration.getEndDatetime());
    }
    
    private boolean eventInitialized() {
        return teamRepository.findAll().iterator().hasNext();
    }

}
