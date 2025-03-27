package org.uimshowdown.bingo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "challenge_relay_components")
public class ChallengeRelayComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @Column(length = 64)
    private String name;

    public Integer getId() {
        return id;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public String getName() {
        return name;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if ((obj instanceof ChallengeRelayComponent) == false) {
            return false;
        }

        ChallengeRelayComponent otherChallengeRelayComponent = (ChallengeRelayComponent) obj;
        return (
            getId() instanceof Integer ? getId().equals(otherChallengeRelayComponent.getId()) : getId() == otherChallengeRelayComponent.getId()
            && getName() instanceof String ? getName().equals(otherChallengeRelayComponent.getName()) : getName() == otherChallengeRelayComponent.getName()
        );
    }
}
