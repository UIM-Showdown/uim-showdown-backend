package org.uimshowdown.bingo.models;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "teams")
public class Team {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private int id;

    @Column(length = 8)
    @JsonProperty
    private String abbreviation;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<SpeedChallengeCompletion> speedChallengeCompletions = new HashSet<SpeedChallengeCompletion>();
    
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<PointsChallengeCompletion> pointsChallengeCompletions = new HashSet<PointsChallengeCompletion>();

    @Column(length = 6)
    @JsonProperty
    private String color;

    @Column(length = 128, unique = true)
    @JsonProperty
    private String name;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    @JsonProperty
    private Set<Player> players = new HashSet<Player>();

    @OneToOne(mappedBy = "team", cascade = CascadeType.ALL)
    @JsonIgnore
    private TeamScoreboard scoreboard;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<TileProgress> tileProgresses = new HashSet<TileProgress>();
    
    @Column(name = "captain_rsns_csv", length = 256)
    @JsonIgnore
    private String captainsCSV = "";

    public String getAbbreviation() {
        return abbreviation;
    }

    public Set<SpeedChallengeCompletion> getSpeedChallengeCompletions() {
        return speedChallengeCompletions;
    }
    
    public Set<PointsChallengeCompletion> getPointsChallengeCompletions() {
        return pointsChallengeCompletions;
    }

    public String getColor() {
        return color;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public Set<TileProgress> getTileProgresses() {
        return tileProgresses;
    }
    
    public void setTileProgresses(Set<TileProgress> tileProgresses) {
        this.tileProgresses = tileProgresses;
    }
    
    public TeamScoreboard getScoreboard() {
        return scoreboard;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void setSpeedChallengeCompletions(Set<SpeedChallengeCompletion> speedChallengeCompletions) {
        this.speedChallengeCompletions = speedChallengeCompletions;
    }
    
    public void setPointsChallengeCompletions(Set<PointsChallengeCompletion> pointsChallengeCompletions) {
        this.pointsChallengeCompletions = pointsChallengeCompletions;
    }
    
    public void setScoreboard(TeamScoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }
    
    @JsonProperty("captains")
    public List<String> getCaptainRsns() {
        return Arrays.asList(captainsCSV.split(","));
    }
    
    public void setCaptainRsns(List<String> captainRsns) {
        this.captainsCSV = String.join(",", captainRsns);
    }
    
    @JsonIgnore
    public RecordCompletion getBestRecordCompletion(Record record) {
        RecordCompletion best = null;
        for(Player player : players) {
            for(RecordCompletion completion : player.getRecordCompletions()) {
                if(completion.record.equals(record)) {
                    if(best == null || completion.getValue() > best.getValue()) {
                        best = completion;
                    }
                }
            }
        }
        return best;
    }
    
    @JsonIgnore
    public SpeedChallengeCompletion getSpeedChallengeCompletion(Challenge challenge) {
        for(SpeedChallengeCompletion completion : speedChallengeCompletions) {
            if(completion.getChallenge().equals(challenge)) {
                return completion;
            }
        }
        return null;
    }
    
    @JsonIgnore
    public PointsChallengeCompletion getPointsChallengeCompletion(Challenge challenge) {
        for(PointsChallengeCompletion completion : pointsChallengeCompletions) {
            if(completion.getChallenge().equals(challenge)) {
                return completion;
            }
        }
        return null;
    }
    
    @JsonIgnore
    public TileProgress getTileProgress(Tile tile) {
        for(TileProgress tileProgress : tileProgresses) {
            if(tileProgress.getTile().equals(tile)) {
                return tileProgress;
            }
        }
        return null;
    }
    
    @JsonIgnore
    public Set<CollectionLogCompletion> getCollectionLogCompletions() {
        Set<CollectionLogCompletion> completions = new HashSet<CollectionLogCompletion>();
        for(Player player : players) {
            completions.addAll(player.getCollectionLogCompletions());
        }
        return completions;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Team && ((Team) obj).getId() == this.id;
    }
    
}
