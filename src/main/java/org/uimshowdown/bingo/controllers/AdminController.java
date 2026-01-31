package org.uimshowdown.bingo.controllers;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.uimshowdown.bingo.services.StatsService;
import org.uimshowdown.bingo.services.TempleOsrsService;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;

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
    JDA discordClient;
    
    @Value("${discord.guildId}")
    long guildId;

    @PostMapping("/admin/addPlayer")
    public ResponseEntity<Void> addPlayer(@RequestBody Map<String, Object> requestBody) throws Exception {
        Guild guild = discordClient.getGuildById(guildId);
        
        eventDataInitializationService.addPlayer(
            (String) requestBody.get("discordName"),
            (String) requestBody.get("rsn"),
            (String) requestBody.get("teamName")
        );
        
        if(!guild.getRolesByName((String) requestBody.get("teamName"), true).isEmpty() && !guild.getMembersByName((String) requestBody.get("discordName"), true).isEmpty()) {
            Member member = guild.getMembersByName((String) requestBody.get("discordName"), true).get(0);
            Role teamRole = guild.getRolesByName((String) requestBody.get("teamName"), true).get(0);
            guild.addRoleToMember(member, teamRole).submit();
        }
        
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
        if(!guild.getRolesByName(oldTeam.getName(), false).isEmpty() && !guild.getRolesByName(newTeam.getName(), false).isEmpty()) {
            Role oldTeamRole = guild.getRolesByName(oldTeam.getName(), false).get(0);
            Role newTeamRole = guild.getRolesByName(newTeam.getName(), false).get(0);
            Member member = guild.getMembersByName(player.getDiscordName(), true).get(0);
            guild.removeRoleFromMember(member, oldTeamRole).submit();
            guild.addRoleToMember(member, newTeamRole).submit();
        }
        
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/admin/changePlayerRsn")
    public ResponseEntity<Void> changePlayerRsn(@RequestBody Map<String, Object> requestBody) {
        Player player = playerRepository.findByRsn((String) requestBody.get("oldRsn")).get();
        player.setRsn((String) requestBody.get("newRsn"));
        playerRepository.save(player);
        
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
                contribution.setIsEmpty(false);
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
        Guild guild = discordClient.getGuildById(guildId);
        Role competitorRole = guild.getRolesByName("Competitor", false).get(0);
        List<Map<String, String>> signupsNotFound = new ArrayList<Map<String, String>>();
        Map<String, Object> result = new HashMap<String, Object>();
        for(Map<String, String> signup : googleSheetsService.getSignups()) {
            String discordName = signup.get("discordName");
            if(guild.getMembersByName(discordName, true).isEmpty()) {
                signupsNotFound.add(signup);
            } else {
                Member member = guild.getMembersByName(discordName, true).get(0);
                if(!member.getRoles().contains(competitorRole)) {                    
                    guild.addRoleToMember(member, competitorRole).complete();
                }
            }
        }
        result.put("signupsNotFound", signupsNotFound);
        return result;
    }
    
    @PostMapping("/admin/setupDiscordServer")
    public Map<String, Object> setupDiscordServer() throws Exception {
        Guild guild = discordClient.getGuildById(guildId);
        Role eventStaffRole = guild.getRolesByName("Event staff", false).get(0);
        Role captainRole = guild.getRolesByName("Captain", false).get(0);
        Role cheerleaderRole = guild.getRolesByName("Cheerleader", false).get(0);
        Role screenshotApproverRole = guild.getRolesByName("Screenshot Approver", false).get(0);
        Role defaultRole = guild.getPublicRole();
        List<String> namesNotFound = new ArrayList<String>();
        
        for(Team team : teamRepository.findByOrderByIdAsc()) {
            
            // Create team role
            Role teamRole = null;
            if(!guild.getRolesByName(team.getName(), false).isEmpty()) {
                teamRole = guild.getRolesByName(team.getName(), false).get(0);
            } else {
                teamRole = guild.createRole().setName(team.getName()).setColor(Color.decode("#" + team.getColor())).setMentionable(true).complete();
            }
            
            // Create team category
            Category teamCategory = null;
            if(!guild.getCategoriesByName(team.getAbbreviation(), false).isEmpty()) {
                teamCategory = guild.getCategoriesByName(team.getAbbreviation(), false).get(0);
            } else {
                teamCategory = guild.createCategory(team.getAbbreviation())
                    .addRolePermissionOverride(defaultRole.getIdLong(), null, Arrays.asList(Permission.VIEW_CHANNEL))
                    .addRolePermissionOverride(eventStaffRole.getIdLong(), Arrays.asList(Permission.VIEW_CHANNEL, Permission.ADMINISTRATOR), null)
                    .addRolePermissionOverride(teamRole.getIdLong(), Arrays.asList(Permission.VIEW_CHANNEL, Permission.MESSAGE_ATTACH_FILES), null)
                    .addRolePermissionOverride(cheerleaderRole.getIdLong(), null, Arrays.asList(Permission.VIEW_CHANNEL))
                    .addRolePermissionOverride(screenshotApproverRole.getIdLong(), null, Arrays.asList(Permission.VIEW_CHANNEL))
                    .addRolePermissionOverride(captainRole.getIdLong(), Arrays.asList(Permission.PIN_MESSAGES), null)
                    .complete();
            }
            
            // Create announcements text channel
            String announcementsTextChannelName = team.getAbbreviation().toLowerCase() + "-announcements";
            if(guild.getTextChannelsByName(announcementsTextChannelName, false).isEmpty()) {
                teamCategory.createTextChannel(announcementsTextChannelName)
                    .addRolePermissionOverride(defaultRole.getIdLong(), null, Arrays.asList(Permission.VIEW_CHANNEL))
                    .addRolePermissionOverride(eventStaffRole.getIdLong(), Arrays.asList(Permission.VIEW_CHANNEL, Permission.ADMINISTRATOR), null)
                    .addRolePermissionOverride(teamRole.getIdLong(), Arrays.asList(Permission.VIEW_CHANNEL), Arrays.asList(Permission.MESSAGE_SEND))
                    .addRolePermissionOverride(cheerleaderRole.getIdLong(), null, Arrays.asList(Permission.VIEW_CHANNEL))
                    .addRolePermissionOverride(screenshotApproverRole.getIdLong(), null, Arrays.asList(Permission.VIEW_CHANNEL))
                    .addRolePermissionOverride(captainRole.getIdLong(), Arrays.asList(Permission.MESSAGE_SEND, Permission.PIN_MESSAGES), null)
                    .complete();
            }
            
            // Create tier-ups text channel
            String tierUpsTextChannelName = team.getAbbreviation().toLowerCase() + "-tier-ups";
            if(guild.getTextChannelsByName(tierUpsTextChannelName, false).isEmpty()) {
                teamCategory.createTextChannel(tierUpsTextChannelName)
                    .addRolePermissionOverride(defaultRole.getIdLong(), null, Arrays.asList(Permission.VIEW_CHANNEL))
                    .addRolePermissionOverride(eventStaffRole.getIdLong(), Arrays.asList(Permission.VIEW_CHANNEL, Permission.ADMINISTRATOR), null)
                    .addRolePermissionOverride(teamRole.getIdLong(), Arrays.asList(Permission.VIEW_CHANNEL), Arrays.asList(Permission.MESSAGE_SEND))
                    .addRolePermissionOverride(cheerleaderRole.getIdLong(), null, Arrays.asList(Permission.VIEW_CHANNEL))
                    .addRolePermissionOverride(screenshotApproverRole.getIdLong(), null, Arrays.asList(Permission.VIEW_CHANNEL))
                    .addRolePermissionOverride(captainRole.getIdLong(), null, Arrays.asList(Permission.MESSAGE_SEND, Permission.PIN_MESSAGES))
                    .complete();
            }
            
            // Create general text channel
            String generalTextChannelName = team.getAbbreviation().toLowerCase() + "-general";
            if(guild.getTextChannelsByName(generalTextChannelName, false).isEmpty()) {
                teamCategory.createTextChannel(generalTextChannelName).complete();
            }
            
            // Create general voice channel
            String generalVoiceChannelName = team.getAbbreviation().toLowerCase() + "-general";
            if(guild.getVoiceChannelsByName(generalVoiceChannelName, false).isEmpty()) {
                teamCategory.createVoiceChannel(generalVoiceChannelName)
                .addRolePermissionOverride(defaultRole.getIdLong(), null, Arrays.asList(Permission.VIEW_CHANNEL))
                .addRolePermissionOverride(eventStaffRole.getIdLong(), Arrays.asList(Permission.VIEW_CHANNEL, Permission.ADMINISTRATOR), null)
                .addRolePermissionOverride(teamRole.getIdLong(), Arrays.asList(Permission.VIEW_CHANNEL), null)
                .addRolePermissionOverride(cheerleaderRole.getIdLong(), Arrays.asList(Permission.VIEW_CHANNEL), null)
                .addRolePermissionOverride(screenshotApproverRole.getIdLong(), null, Arrays.asList(Permission.VIEW_CHANNEL))
                .addRolePermissionOverride(captainRole.getIdLong(), null, null)
                .complete();
            }
            
            // Create bot submissions text channel
            String botSubmissionsTextChannelName = team.getAbbreviation().toLowerCase() + "-bot-submissions";
            if(guild.getTextChannelsByName(botSubmissionsTextChannelName, false).isEmpty()) {
                teamCategory.createTextChannel(botSubmissionsTextChannelName)
                    .addRolePermissionOverride(defaultRole.getIdLong(), null, Arrays.asList(Permission.VIEW_CHANNEL))
                    .addRolePermissionOverride(eventStaffRole.getIdLong(), Arrays.asList(Permission.VIEW_CHANNEL, Permission.ADMINISTRATOR), null)
                    .addRolePermissionOverride(teamRole.getIdLong(), Arrays.asList(Permission.VIEW_CHANNEL), null)
                    .addRolePermissionOverride(cheerleaderRole.getIdLong(), null, Arrays.asList(Permission.VIEW_CHANNEL))
                    .addRolePermissionOverride(screenshotApproverRole.getIdLong(), Arrays.asList(Permission.VIEW_CHANNEL), null)
                    .addRolePermissionOverride(captainRole.getIdLong(), null, Arrays.asList(Permission.PIN_MESSAGES))
                    .complete();
            }
            
            // Assign team role to players
            for(Player player : team.getPlayers()) {
                if(guild.getMembersByName(player.getDiscordName(), true).isEmpty()) {
                    namesNotFound.add(player.getDiscordName());
                } else {
                    Member member = guild.getMembersByName(player.getDiscordName(), true).get(0);
                    if(!member.getRoles().contains(teamRole)) {                        
                        guild.addRoleToMember(member, teamRole).complete();
                    }
                }
            }
            
        }
        
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("namesNotFound", namesNotFound);
        return result;
    }
    
    @PostMapping("/admin/teardownDiscordServer")
    public void teardownDiscordServer() throws Exception {
        Guild guild = discordClient.getGuildById(guildId);
        Role captainRole = guild.getRolesByName("Captain", false).get(0);
        Role competitorRole = guild.getRolesByName("Competitor", false).get(0);
        
        // Delete team channels and role
        for(Team team : teamRepository.findByOrderByIdAsc()) {
            if(!guild.getCategoriesByName(team.getAbbreviation(), false).isEmpty()) {
                Category teamCategory = guild.getCategoriesByName(team.getAbbreviation(), false).get(0);
                for(GuildChannel channel : teamCategory.getChannels()) {
                    channel.delete().complete();
                }
                teamCategory.delete().complete();
            }
            if(!guild.getRolesByName(team.getName(), false).isEmpty()) {
                Role teamRole = guild.getRolesByName(team.getName(), false).get(0);
                teamRole.delete().complete();
            }
        }
        
        // De-assign competitor and captain role
        List<Member> members = guild.getMembers();
        for(Member member : members) {
            if(member.getRoles().contains(competitorRole)) {
                guild.removeRoleFromMember(member, competitorRole).complete();
            }
            if(member.getRoles().contains(captainRole)) {
                guild.removeRoleFromMember(member, captainRole).complete();
            }
        }
    }
    
    @GetMapping("/admin/stats")
    public Map<String, Object> getStats() throws Exception {
        return statsService.getStatsReport();
    }

}
