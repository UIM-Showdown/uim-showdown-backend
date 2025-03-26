package org.uimshowdown.bingo.models;

import java.util.Objects;
import java.util.Set;

import org.uimshowdown.bingo.enums.ChallengeType;

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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 512)
    private String description;

    @Column(length = 64)
    private String name;

    @OneToMany(mappedBy = "challenge")
    private Set<ChallengeRelayComponent> relayComponents;

    @Column(name = "team_size")
    private Integer teamSize;

    @Column
    @Enumerated(EnumType.STRING)
    private ChallengeType type;

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public Integer getTeamSize() {
        return teamSize;
    }

    public ChallengeType getType() {
        return type;
    }

    public Challenge setDescription(String description) {
        this.description = description;
        return this;
    }

    public Challenge setName(String name) {
        this.name = name;
        return this;
    }

    public Challenge setTeamSize(Integer teamSize) {
        this.teamSize = teamSize;
        return this;
    }

    public Challenge setType(ChallengeType challengeType) {
        type = challengeType;
        return this;
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
        return (
            getId() instanceof Integer ? getId().equals(otherChallenge.getId()) : getId() == otherChallenge.getId()
            && getDescription() instanceof String ? getDescription().equals(otherChallenge.getDescription()) : getDescription() == otherChallenge.getDescription()
            && getName() instanceof String ? getName().equals(otherChallenge.getName()) : getName() == otherChallenge.getName()
            && getTeamSize() instanceof Integer ? getTeamSize().equals(otherChallenge.getTeamSize()) : getTeamSize() == otherChallenge.getTeamSize()
            && getType() instanceof ChallengeType ? getType().equals(otherChallenge.getType()) : getType() == otherChallenge.getType()
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, name, teamSize, type);
    }
}
