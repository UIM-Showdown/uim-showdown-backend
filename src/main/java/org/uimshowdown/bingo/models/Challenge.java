package org.uimshowdown.bingo.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "challenges")
public class Challenge {
    
    public enum Type { RELAY, SPEEDRUN }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private int id;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<ChallengeCompletion> completions = new HashSet<ChallengeCompletion>();

    @Column(length = 512)
    @JsonProperty
    private String description;

    @Column(length = 64)
    @JsonProperty
    private String name;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
    @JsonProperty
    private List<ChallengeRelayComponent> relayComponents = new ArrayList<ChallengeRelayComponent>();

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<ChallengeSubmission> submissions = new HashSet<ChallengeSubmission>();

    @Column(name = "team_size")
    @JsonProperty
    private int teamSize;

    @Column
    @Enumerated(EnumType.STRING)
    @JsonProperty
    private Type type;

    public int getId() {
        return id;
    }

    public Set<ChallengeCompletion> getCompletions() {
        return completions;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public List<ChallengeRelayComponent> getRelayComponents() {
        return relayComponents;
    }
    
    public void setRelayComponents(List<ChallengeRelayComponent> relayComponents) {
        this.relayComponents = relayComponents;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public Type getType() {
        return type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Challenge && ((Challenge) obj).getId() == this.id;
    }
    
}
