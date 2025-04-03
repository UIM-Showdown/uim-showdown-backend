package org.uimshowdown.bingo.models;

import java.util.Objects;
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
    private Integer id;

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
    private Integer teamSize;

    @Column
    @Enumerated(EnumType.STRING)
    private Type type;

    public Integer getId() {
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

    public Integer getTeamSize() {
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

    public void setTeamSize(Integer teamSize) {
        this.teamSize = teamSize;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if ((obj instanceof Challenge) == false) {
            return false;
        }

        Challenge otherChallenge = (Challenge) obj;
        return Integer.compare(id, otherChallenge.getId()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, name, teamSize, type);
    }
    
}
