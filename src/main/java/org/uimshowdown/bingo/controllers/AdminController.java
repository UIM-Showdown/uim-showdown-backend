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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
import org.uimshowdown.bingo.services.EventDataInitializationService;
import org.uimshowdown.bingo.services.GoogleSheetsService;
import org.uimshowdown.bingo.services.ScoreboardCalculationService;
import org.uimshowdown.bingo.services.TempleOsrsService;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

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
    JDA discordClient;
    
    @Value("${discord.guildId}")
    long guildId;

    @PostMapping("/admin/addPlayer")
    public ResponseEntity<Void> addPlayer(@RequestBody Map<String, Object> requestBody) throws Exception {
        eventDataInitializationService.addPlayer(
            (String) requestBody.get("discordName"),
            (String) requestBody.get("rsn"),
            (String) requestBody.get("teamName")
        );
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/admin/changePlayerTeam")
    public ResponseEntity<Void> changePlayerTeam(@RequestBody Map<String, Object> requestBody) {
        Player player = playerRepository.findByRsn((String) requestBody.get("rsn")).get();
        Team oldTeam = player.getTeam();
        Team newTeam = teamRepository.findByName((String) requestBody.get("teamName")).get();

        player.setTeam(newTeam);
        playerRepository.save(player);
        
        Guild guild = discordClient.getGuildById(guildId);
        Role oldTeamRole = guild.getRolesByName(oldTeam.getName(), false).get(0);
        Role newTeamRole = guild.getRolesByName(newTeam.getName(), false).get(0);
        List<Member> members = guild.loadMembers().get();
        Member member = null;
        for(Member m : members) {
            if(m.getUser().getName().equalsIgnoreCase(player.getDiscordName())) {
                member = m;
            }
        }
        if(member != null) {
            guild.removeRoleFromMember(member, oldTeamRole).complete();
            guild.addRoleToMember(member, newTeamRole).complete();
        }
        
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/admin/renamePlayer")
    public ResponseEntity<Void> renamePlayer(@RequestBody Map<String, Object> requestBody) {
        Player player = playerRepository.findByRsn((String) requestBody.get("oldRsn")).get();
        player.setRsn((String) requestBody.get("newRsn"));
        playerRepository.save(player);
        
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/admin/initializeCompetition")
    public ResponseEntity<Object> initializeCompetition() throws Exception {
        Date now = new Date();
        if(now.after(competitionConfiguration.getStartDatetime()) && now.before(competitionConfiguration.getEndDatetime())) {
            return ResponseEntity.badRequest().body("Event currently in progress");
        }
        eventDataInitializationService.initializeCompetition();
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
        if(!teamRepository.findAll().iterator().hasNext()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Competition has not been initialized");
        }
        
        templeOsrsService.updateCompetition();
        scoreboardCalculationService.calculate();
        dataOutputService.outputData();
        
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
        Guild guild = discordClient.getGuildById(guildId);
        Role competitorRole = guild.getRolesByName("Competitor", false).get(0);
        List<String> namesNotFound = new ArrayList<String>();
        Map<String, Object> result = new HashMap<String, Object>();
        List<Member> members = guild.loadMembers().get();
        for(String discordName : googleSheetsService.getSignupDiscordNames()) {
            Member member = null;
            for(Member m : members) {
                if(m.getUser().getName().equalsIgnoreCase(discordName)) {
                    member = m;
                }
            }
            if(member != null) {
                guild.addRoleToMember(member, competitorRole).complete();
            } else {
                namesNotFound.add(discordName);
            }
        }
        result.put("namesNotFound", namesNotFound);
        return result;
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
        if(!teamRepository.findAll().iterator().hasNext()) {
            return; // Comp has not been initialized; do nothing
        }
        for(String profile : environment.getActiveProfiles()) {
            if(profile.equals("test")) { // We're in the middle of JUnit tests
                return;
            }
        }
        long start = new Date().getTime();
        updateCompetition();
        long end = new Date().getTime();
        System.out.println("Completed a scheduled update in " + (end - start) + " ms");
    }
    
    /**
     * Automatically calls the updateCompetitorRole() endpoint method at the top of every hour.
     */
    @Scheduled(cron = "0 0 * * * *")
    @Transactional(readOnly=true)
    public void updateCompetitorRoleScheduled() throws Exception {
        if(!teamRepository.findAll().iterator().hasNext()) {
            return; // Comp has not been initialized; do nothing
        }
        for(String profile : environment.getActiveProfiles()) {
            if(profile.equals("test")) { // We're in the middle of JUnit tests
                return;
            }
        }
        long start = new Date().getTime();
        updateCompetitorRole();
        long end = new Date().getTime();
        System.out.println("Updated competitor role in " + (end - start) + " ms");
    }

}
