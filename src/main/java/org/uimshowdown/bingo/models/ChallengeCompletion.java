package org.uimshowdown.bingo.models;

import java.sql.Timestamp;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "challenge_completions")
public class ChallengeCompletion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @OneToMany(mappedBy = "challengeCompletion")
    private Set<PlayerChallengeCompletion> playerChallengeCompletions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(name = "completed_at", nullable = true)
    private Timestamp completedAt;

    @Column
    private double seconds;

    public int getId() {
        return id;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public Set<PlayerChallengeCompletion> getPlayerChallengeCompletions() {
        return playerChallengeCompletions;
    }

    public Team getTeam() {
        return team;
    }

    public Timestamp getCompletedAt() {
        return completedAt;
    }

    public double getSeconds() {
        return seconds;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setCompletedAt(Timestamp completedAt) {
        this.completedAt = completedAt;
    }

    public void setSeconds(double seconds) {
        this.seconds = seconds;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof ChallengeCompletion && ((ChallengeCompletion) obj).getId() == this.id;
    }
    
}
