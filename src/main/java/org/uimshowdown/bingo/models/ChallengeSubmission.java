package org.uimshowdown.bingo.models;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@PrimaryKeyJoinColumn(name = "submission_id")
@Table(name = "challenge_submissions")
public class ChallengeSubmission extends Submission {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_relay_component_id")
    private ChallengeRelayComponent relayComponent;

    @Column
    private Double seconds;

    public Challenge getChallenge() {
        return challenge;
    }

    public ChallengeRelayComponent getRelayComponent() {
        return relayComponent;
    }

    public Double getSeconds() {
        return seconds;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public void setRelayComponent(ChallengeRelayComponent challengeRelayComponent) {
        this.relayComponent = challengeRelayComponent;
    }

    public void setSeconds(Double seconds) {
        this.seconds = seconds;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if ((obj instanceof ChallengeSubmission) == false) {
            return false;
        }

        ChallengeSubmission otherChallengeSubmission = (ChallengeSubmission) obj;
        return (
            super.equals(otherChallengeSubmission)
            && Double.compare(seconds, otherChallengeSubmission.getSeconds()) == 0
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), seconds);
    }
}
