package org.uimshowdown.bingo.configuration;

import java.util.EnumSet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

@Configuration
public class DiscordClientConfiguration {
    
    @Value("${discord.token}")
    private String token;
    
    @Value("${discord.guildId}")
    long guildId;
    
    @Bean
    public JDA discordClient() throws InterruptedException {
        JDA client = JDABuilder.createLight(token)
            .enableIntents(EnumSet.of(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT))
            .setMemberCachePolicy(MemberCachePolicy.ALL)
            .build()
            .awaitReady();
        client.getGuildById(guildId).loadMembers();
        return client;
    }

}
