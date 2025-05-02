package org.uimshowdown.bingo.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
@DiscriminatorValue("CHALLENGE")
public class ChallengeSubmission extends Submission {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    @JsonProperty
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_relay_component_id")
    @JsonProperty
    private ChallengeRelayComponent relayComponent;

    @Column
    @JsonProperty
    private double seconds;
    
    @OneToOne(mappedBy = "submission", cascade = CascadeType.ALL)
    private PlayerChallengeCompletion completion;

    public Challenge getChallenge() {
        return challenge;
    }

    public ChallengeRelayComponent getRelayComponent() {
        return relayComponent;
    }

    public double getSeconds() {
        return seconds;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public void setRelayComponent(ChallengeRelayComponent challengeRelayComponent) {
        this.relayComponent = challengeRelayComponent;
    }

    public void setSeconds(double seconds) {
        this.seconds = seconds;
    }
    
    @Override
    public void setType(Submission.Type type) throws IllegalArgumentException {
        if(type != Submission.Type.CHALLENGE) {
            throw new IllegalArgumentException("Challenge submission type must be set to 'CHALLENGE'");
        }
        
        super.setType(type);
    }

    public PlayerChallengeCompletion getCompletion() {
        return completion;
    }

    public void setCompletion(PlayerChallengeCompletion completion) {
        this.completion = completion;
    }
    
}
