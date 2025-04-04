package org.uimshowdown.bingo.models;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import jakarta.persistence.Table;

@Entity
@Table(name = "challenge_relay_components")
public class ChallengeRelayComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    @JsonIgnore
    private Challenge challenge;

    @Column(length = 64)
    @JsonProperty
    private String name;

    @OneToMany(mappedBy = "relayComponent")
    @JsonIgnore
    private Set<PlayerChallengeCompletion> playerChallengeCompletions;

    @OneToMany(mappedBy = "relayComponent")
    @JsonIgnore
    private Set<ChallengeSubmission> submissions;

    public int getId() {
        return id;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public String getName() {
        return name;
    }

    public Set<PlayerChallengeCompletion> getPlayerChallengeCompletions() {
        return playerChallengeCompletions;
    }

    public Set<ChallengeSubmission> getSubmissions() {
        return submissions;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(Object obj) {
        return obj != null && obj instanceof ChallengeRelayComponent && ((ChallengeRelayComponent) obj).getId() == this.id;
    }
}
