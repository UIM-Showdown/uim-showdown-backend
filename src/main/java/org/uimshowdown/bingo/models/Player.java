package org.uimshowdown.bingo.models;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "players")
public class Player {
    public enum Skill {
        ATTACK,
        STRENGTH,
        DEFENCE,
        RANGED,
        PRAYER,
        MAGIC,
        RUNECRAFT,
        HITPOINTS,
        CRAFTING,
        MINING,
        SMITHING,
        FISHING,
        COOKING,
        FIREMAKING,
        WOODCUTTING,
        AGILITY,
        HERBLORE,
        THIEVING,
        FLETCHING,
        SLAYER,
        FARMING,
        CONSTRUCTION,
        HUNTER
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private int id;

    @Column
    @JsonIgnore
    private boolean captain;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Contribution> contributions;

    @Column(length = 64, name="discord_name", unique = true)
    @JsonProperty
    private String discordName;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<CollectionLogCompletion> collectionLogCompletions;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<PlayerChallengeCompletion> playerChallengeCompletions;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<RecordCompletion> recordCompletions;

    @Column(length = 64, unique = true)
    @JsonProperty
    private String rsn;

    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL)
    @JsonIgnore
    private PlayerScoreboard scoreboard;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Submission> submissions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    @JsonIgnore
    private Team team;

    public Set<Contribution> getContributions() {
        return contributions;
    }

    public String getDiscordName() {
        return discordName;
    }

    public int getId() {
        return id;
    }

    public String getRsn() {
        return rsn;
    }

    public Set<CollectionLogCompletion> getCollectionLogCompletions() {
        return collectionLogCompletions;
    }

    public Set<PlayerChallengeCompletion> getPlayerChallengeCompletions() {
        return playerChallengeCompletions;
    }

    public Set<RecordCompletion> getRecordCompletions() {
        return recordCompletions;
    }

    public PlayerScoreboard getScoreboard() {
        return scoreboard;
    }

    public Set<Submission> getSubmissions() {
        return submissions;
    }

    public Team getTeam() {
        return team;
    }

    public boolean isCaptain() {
        return captain;
    }

    public void setCaptainStatus(boolean isCaptain) {
        captain = isCaptain;
    }

    public void setContributions(Set<Contribution> contributions) {
        this.contributions = contributions;
    }
    
    public void setPlayerChallengeCompletions(Set<PlayerChallengeCompletion> playerChallengeCompletions) {
        this.playerChallengeCompletions = playerChallengeCompletions;
    }
    
    public void setRecordCompletions(Set<RecordCompletion> recordCompletions) {
        this.recordCompletions = recordCompletions;
    }
    
    public void setCollectionLogCompletions(Set<CollectionLogCompletion> collectionLogCompletions) {
        this.collectionLogCompletions = collectionLogCompletions;
    }

    public void setDiscordName(String discordName) {
        this.discordName = discordName;
    }

    public void setRsn(String rsn) {
        this.rsn = rsn;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
    
    public void setScoreboard(PlayerScoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }
    
    @JsonIgnore
    public Contribution getContribution(ContributionMethod method) {
        for(Contribution contribution : this.getContributions()) {
            if(contribution.getContributionMethod().equals(method)) {
                return contribution;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Player && ((Player) obj).getId() == this.id;
    }
    
}
