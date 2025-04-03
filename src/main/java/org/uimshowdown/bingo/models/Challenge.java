package org.uimshowdown.bingo.models;

import java.util.Set;

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
    private int id;

    @OneToMany(mappedBy = "challenge")
    private Set<ChallengeCompletion> completions;

    @Column(length = 512)
    private String description;

    @Column(length = 64)
    private String name;

    @OneToMany(mappedBy = "challenge")
    private Set<ChallengeRelayComponent> relayComponents;

    @OneToMany(mappedBy = "challenge")
    private Set<ChallengeSubmission> submissions;

    @Column(name = "team_size")
    private int teamSize;

    @Column
    @Enumerated(EnumType.STRING)
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

    public Set<ChallengeRelayComponent> getRelayComponents() {
        return relayComponents;
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
