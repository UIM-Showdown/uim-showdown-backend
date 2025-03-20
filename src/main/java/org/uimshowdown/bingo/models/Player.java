package org.uimshowdown.bingo.models;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Boolean captain;

    @OneToMany(mappedBy = "player")
    private Set<Contribution> contributions;

    @Column(length = 64, name="discord_name", unique = true)
    private String discordName;

    @Column(length = 64, unique = true)
    private String rsn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Set<Contribution> getContributions() {
        return contributions;
    }

    public String getDiscordName() {
        return discordName;
    }

    public Integer getId() {
        return id;
    }

    public String getRsn() {
        return rsn;
    }

    public Team getTeam() {
        return team;
    }

    public Boolean isCaptain() {
        return captain;
    }

    public Player setCaptainStatus(Boolean isCaptain) {
        captain = isCaptain;
        return this;
    }

    public Player setDiscordName(String discordName) {
        this.discordName = discordName;
        return this;
    }

    public Player setRsn(String rsn) {
        this.rsn = rsn;
        return this;
    }

    public Player setTeam(Team team) {
        this.team = team;
        return this;
    }
}
