package org.uimshowdown.bingo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 64, name="discord_name", unique = true)
    private String discordName;

    @Column(length = 64, unique = true)
    private String rsn;

    public String getDiscordName() {
        return discordName;
    }

    public Integer getId() {
        return id;
    }

    public String getRsn() {
        return rsn;
    }

    public void setDiscordName(String discordName) {
        this.discordName = discordName;
    }

    public void setRsn(String rsn) {
        this.rsn = rsn;
    }
}
