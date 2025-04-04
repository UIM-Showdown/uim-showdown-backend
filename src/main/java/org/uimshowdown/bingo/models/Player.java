package org.uimshowdown.bingo.models;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

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
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private int id;

    @Column
    private boolean captain;

    @OneToMany(mappedBy = "player")
    private Set<Contribution> contributions;

    @Column(length = 64, name="discord_name", unique = true)
    @JsonProperty
    private String discordName;

    @OneToMany(mappedBy = "player")
    private Set<CollectionLogCompletion> collectionLogCompletions;

    @OneToMany(mappedBy = "player")
    private Set<PlayerChallengeCompletion> playerChallengeCompletions;

    @OneToMany(mappedBy = "player")
    private Set<RecordCompletion> recordCompletions;

    @Column(length = 64, unique = true)
    @JsonProperty
    private String rsn;

    @OneToOne(mappedBy = "player")
    private PlayerScoreboard scoreboard;

    @OneToMany(mappedBy = "player")
    private Set<Submission> submissions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
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

    public void setDiscordName(String discordName) {
        this.discordName = discordName;
    }

    public void setRsn(String rsn) {
        this.rsn = rsn;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public boolean equals(Object obj) {
    	return obj != null && obj instanceof Player && ((Player) obj).getId() == this.id;
    }
    
}
