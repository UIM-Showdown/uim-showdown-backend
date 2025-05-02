package org.uimshowdown.bingo.models;

import java.util.HashSet;
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

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Contribution> contributions = new HashSet<Contribution>();

    @Column(length = 64, name="discord_name", unique = true)
    @JsonProperty
    private String discordName;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<CollectionLogCompletion> collectionLogCompletions = new HashSet<CollectionLogCompletion>();

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<PlayerChallengeCompletion> playerChallengeCompletions = new HashSet<PlayerChallengeCompletion>();

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<RecordCompletion> recordCompletions = new HashSet<RecordCompletion>();

    @Column(length = 64, unique = true)
    @JsonProperty
    private String rsn;

    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL)
    @JsonIgnore
    private PlayerScoreboard scoreboard;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Submission> submissions = new HashSet<Submission>();

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
        return team.getCaptainRsns().contains(rsn);
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
        for(Contribution contribution : contributions) {
            if(contribution.getContributionMethod().equals(method)) {
                return contribution;
            }
        }
        return null;
    }
    
    @JsonIgnore
    public RecordCompletion getRecordCompletion(Record record) {
        for(RecordCompletion completion : recordCompletions) {
            if(completion.getRecord().equals(record)) {
                return completion;
            }
        }
        return null;
    }
    
    @JsonIgnore
    public PlayerChallengeCompletion getBestPlayerChallengeCompletion(Challenge challenge, ChallengeRelayComponent relayComponent) {
        PlayerChallengeCompletion bestCompletion = null;
        for(PlayerChallengeCompletion completion : playerChallengeCompletions) {
            if(completion.getChallenge().equals(challenge) && ((relayComponent == null && completion.getChallengeRelayComponent() == null) || completion.getChallengeRelayComponent().equals(relayComponent))) {
                if(bestCompletion == null || completion.getSeconds() < bestCompletion.getSeconds()) {
                    bestCompletion = completion;
                }
            }
        }
        return bestCompletion;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Player && ((Player) obj).getId() == this.id;
    }
    
}
