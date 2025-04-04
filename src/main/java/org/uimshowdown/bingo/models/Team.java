package org.uimshowdown.bingo.models;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    @OneToMany(mappedBy = "team")
    @JsonIgnore
    private Set<ChallengeCompletion> challengeCompletions;

    @Column(length = 6)
    @JsonProperty
    private String color;

    @Column(length = 128, unique = true)
    @JsonProperty
    private String name;

    @OneToMany(mappedBy = "team")
    @JsonProperty
    private Set<Player> players;

    @OneToOne(mappedBy = "team")
    @JsonIgnore
    private TeamScoreboard scoreboard;

    @OneToMany(mappedBy = "team")
    @JsonIgnore
    private Set<TileProgress> tileProgress;

    public String getAbbreviation() {
        return abbreviation;
    }

    public Set<ChallengeCompletion> getChallengeCompletions() {
        return challengeCompletions;
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

    public Set<TileProgress> getTileProgress() {
        return tileProgress;
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
    
    @JsonProperty("captains")
    public Set<Player> getCaptains() {
        Set<Player> captains = new HashSet<Player>();
        for(Player player : this.players) {
            if(player.isCaptain()) {
                captains.add(player);
            }
        }
        return captains;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Team && ((Team) obj).getId() == this.id;
    }
    
}
