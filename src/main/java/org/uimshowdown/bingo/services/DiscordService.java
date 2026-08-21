package org.uimshowdown.bingo.services;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.Team;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;

@Component
public class DiscordService {
    
    @Value("${discord.guildId}")
    long guildId;
    
    @Autowired JDA discordClient;
    
    /**
     * Sends the specified message to the text channel with the specified name. Does nothing if the channel is not found.
     * @param channelName The text channel name to send a message to
     * @param message The message to send
     */
    public void sendMessage(String channelName, String message) {
        Guild guild = discordClient.getGuildById(guildId);
        if(guild.getTextChannelsByName(channelName, false).isEmpty()) {
            return;
        }
        TextChannel channel = guild.getTextChannelsByName(channelName, false).get(0);
        channel.sendMessage(message).submit();
    }
    
    /**
     * Adds the role with the specified name to the user with the specified username (case-insensitive). 
     * Does nothing if either is not found, or if the user already has the role.
     * @param roleName The role to add
     * @param username The username of the user to add a role to
     */
    public void addRoleToUser(String roleName, String username) {
        Guild guild = discordClient.getGuildById(guildId);
        if(guild.getRolesByName(roleName, false).isEmpty()) {
            return;
        }
        if(guild.getMembersByName(username, true).isEmpty()) {
            return;
        }
        Member member = guild.getMembersByName(username, true).get(0);
        Role role = guild.getRolesByName(roleName, true).get(0);
        guild.addRoleToMember(member, role).submit();
    }
    
    /**
     * Removes the role with the specified name from the user with the specified username (case-insensitive). 
     * Does nothing if either is not found, or if the user does not have the role.
     * @param roleName The role to remove
     * @param username The username of the user to remove a role from
     */
    public void removeRoleFromUser(String roleName, String username) {
        Guild guild = discordClient.getGuildById(guildId);
        if(guild.getRolesByName(roleName, false).isEmpty()) {
            return;
        }
        if(guild.getMembersByName(username, true).isEmpty()) {
            return;
        }
        Member member = guild.getMembersByName(username, true).get(0);
        Role role = guild.getRolesByName(roleName, true).get(0);
        guild.removeRoleFromMember(member, role).submit();
    }
    
    /**
     * Returns true if a user with the specified username (case-insensitive) is in the server, false otherwise
     * @param username The username of the user to check for
     * @return true if the user is in the server, false otherwise
     */
    public boolean isUserInServer(String username) {
        Guild guild = discordClient.getGuildById(guildId);
        return !guild.getMembersByName(username, true).isEmpty();
    }
    
    /**
     * Performs the following setup actions in the Discord server, for each team:
     * * Creates the team role if it does not already exist
     * * Creates the team category if it does not already exist
     * * Creates the default team channels if they do not already exist
     * * Adds the team role to the team's players if they do not already have the role
     * @param teams A list of Team objects to perform setup actions for
     */
    public void setupDiscordServer(List<Team> teams) {
        Guild guild = discordClient.getGuildById(guildId);
        Role eventStaffRole = guild.getRolesByName("Event staff", false).get(0);
        Role technicalLeadRole = guild.getRolesByName("Technical Lead", false).get(0);
        Role captainRole = guild.getRolesByName("Captain", false).get(0);
        Role cheerleaderRole = guild.getRolesByName("Cheerleader", false).get(0);
        Role screenshotApproverRole = guild.getRolesByName("Screenshot Approver", false).get(0);
        Role defaultRole = guild.getPublicRole();
        
        for(Team team : teams) {
            
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
                    .addRolePermissionOverride(technicalLeadRole.getIdLong(), Arrays.asList(Permission.VIEW_CHANNEL, Permission.ADMINISTRATOR), null)
                    .addRolePermissionOverride(teamRole.getIdLong(), Arrays.asList(Permission.VIEW_CHANNEL, Permission.MESSAGE_ATTACH_FILES), null)
                    .addRolePermissionOverride(cheerleaderRole.getIdLong(), null, Arrays.asList(Permission.VIEW_CHANNEL))
                    .addRolePermissionOverride(screenshotApproverRole.getIdLong(), null, Arrays.asList(Permission.VIEW_CHANNEL))
                    .addRolePermissionOverride(captainRole.getIdLong(), Arrays.asList(Permission.PIN_MESSAGES, Permission.MANAGE_CHANNEL), null)
                    .complete();
            }
            
            // Create announcements text channel
            String announcementsTextChannelName = team.getAbbreviation().toLowerCase() + "-announcements";
            if(guild.getTextChannelsByName(announcementsTextChannelName, false).isEmpty()) {
                teamCategory.createTextChannel(announcementsTextChannelName)
                    .addRolePermissionOverride(defaultRole.getIdLong(), null, Arrays.asList(Permission.VIEW_CHANNEL))
                    .addRolePermissionOverride(eventStaffRole.getIdLong(), Arrays.asList(Permission.VIEW_CHANNEL, Permission.ADMINISTRATOR), null)
                    .addRolePermissionOverride(technicalLeadRole.getIdLong(), Arrays.asList(Permission.VIEW_CHANNEL, Permission.ADMINISTRATOR), null)
                    .addRolePermissionOverride(teamRole.getIdLong(), Arrays.asList(Permission.VIEW_CHANNEL), Arrays.asList(Permission.MESSAGE_SEND))
                    .addRolePermissionOverride(cheerleaderRole.getIdLong(), null, Arrays.asList(Permission.VIEW_CHANNEL))
                    .addRolePermissionOverride(screenshotApproverRole.getIdLong(), null, Arrays.asList(Permission.VIEW_CHANNEL))
                    .addRolePermissionOverride(captainRole.getIdLong(), Arrays.asList(Permission.MESSAGE_SEND, Permission.PIN_MESSAGES, Permission.MANAGE_CHANNEL), null)
                    .complete();
            }
            
            // Create tier-ups text channel
            String tierUpsTextChannelName = team.getAbbreviation().toLowerCase() + "-tier-ups";
            if(guild.getTextChannelsByName(tierUpsTextChannelName, false).isEmpty()) {
                teamCategory.createTextChannel(tierUpsTextChannelName)
                    .addRolePermissionOverride(defaultRole.getIdLong(), null, Arrays.asList(Permission.VIEW_CHANNEL))
                    .addRolePermissionOverride(eventStaffRole.getIdLong(), Arrays.asList(Permission.VIEW_CHANNEL, Permission.ADMINISTRATOR), null)
                    .addRolePermissionOverride(technicalLeadRole.getIdLong(), Arrays.asList(Permission.VIEW_CHANNEL, Permission.ADMINISTRATOR), null)
                    .addRolePermissionOverride(teamRole.getIdLong(), Arrays.asList(Permission.VIEW_CHANNEL), Arrays.asList(Permission.MESSAGE_SEND))
                    .addRolePermissionOverride(cheerleaderRole.getIdLong(), null, Arrays.asList(Permission.VIEW_CHANNEL))
                    .addRolePermissionOverride(screenshotApproverRole.getIdLong(), null, Arrays.asList(Permission.VIEW_CHANNEL))
                    .addRolePermissionOverride(captainRole.getIdLong(), null, Arrays.asList(Permission.MESSAGE_SEND, Permission.PIN_MESSAGES, Permission.MANAGE_CHANNEL))
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
                .addRolePermissionOverride(technicalLeadRole.getIdLong(), Arrays.asList(Permission.VIEW_CHANNEL, Permission.ADMINISTRATOR), null)
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
                    .addRolePermissionOverride(technicalLeadRole.getIdLong(), Arrays.asList(Permission.VIEW_CHANNEL, Permission.ADMINISTRATOR), null)
                    .addRolePermissionOverride(teamRole.getIdLong(), Arrays.asList(Permission.VIEW_CHANNEL), null)
                    .addRolePermissionOverride(cheerleaderRole.getIdLong(), null, Arrays.asList(Permission.VIEW_CHANNEL))
                    .addRolePermissionOverride(screenshotApproverRole.getIdLong(), Arrays.asList(Permission.VIEW_CHANNEL), null)
                    .addRolePermissionOverride(captainRole.getIdLong(), null, Arrays.asList(Permission.PIN_MESSAGES, Permission.MANAGE_CHANNEL))
                    .complete();
            }
            
            // Assign team role to players
            for(Player player : team.getPlayers()) {
                this.addRoleToUser(team.getName(), player.getDiscordName());
            }
        }
    }
    
    /**
     * Performs the following teardown actions in the Discord server, for each team:
     * * Deletes all channels in the team category
     * * Deletes the team category, if it exists
     * * Deletes the team role, if it exists
     * * Removes the Competitor and Captain roles from all users
     * @param teams A list of Team objects to perform teardown actions for
     */
    public void teardownDiscordServer(List<Team> teams) {
        Guild guild = discordClient.getGuildById(guildId);
        
        // Delete team channels and role
        for(Team team : teams) {
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
            this.removeRoleFromUser("Competitor", member.getUser().getName());
            this.removeRoleFromUser("Captain", member.getUser().getName());
        }
    }
}
