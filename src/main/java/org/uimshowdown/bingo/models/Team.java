package org.uimshowdown.bingo.models;

import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 8)
    private String abbreviation;

    @OneToMany(mappedBy = "team")
    private Set<ChallengeCompletion> challengeCompletions;

    @Column(length = 6)
    private String color;

    @Column(length = 128, unique = true)
    private String name;

    @OneToMany(mappedBy = "team")
    private Set<Player> players;

    public String getAbbreviation() {
        return abbreviation;
    }

    public Set<ChallengeCompletion> getChallengeCompletions() {
        return challengeCompletions;
    }

    public String getColor() {
        return color;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public Set<Player> getPlayers() {
        return players;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if ((obj instanceof Team) == false) {
            return false;
        }

        Team otherTeam = (Team) obj;
        return (
            getId() instanceof Integer ? getId().equals(otherTeam.getId()) : getId() == otherTeam.getId()
            && getAbbreviation() instanceof String ? getAbbreviation().equals(otherTeam.getAbbreviation()) : getAbbreviation() == otherTeam.getAbbreviation()
            && getColor() instanceof String ? getColor().equals(otherTeam.getColor()) : getColor() == otherTeam.getColor()
            && getName() instanceof String ? getName().equals(otherTeam.getName()) : getName() == otherTeam.getName()
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, abbreviation, color, name);
    }
}
