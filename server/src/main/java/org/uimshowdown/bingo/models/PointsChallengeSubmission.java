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
@DiscriminatorValue("CHALLENGE_POINTS")
public class PointsChallengeSubmission extends Submission {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    @JsonProperty
    private Challenge challenge;

    @Column
    @JsonProperty
    private int points;
    
    @OneToOne(mappedBy = "submission", cascade = CascadeType.ALL)
    private PlayerSpeedChallengeCompletion completion;

    public Challenge getChallenge() {
        return challenge;
    }

    public int getPoints() {
        return points;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public void setPoints(int points) {
        this.points = points;
    }
    
    @Override
    public void setType(Submission.Type type) throws IllegalArgumentException {
        if(type != Submission.Type.CHALLENGE_POINTS) {
            throw new IllegalArgumentException("Challenge submission type must be set to 'CHALLENGE_POINTS'");
        }
        
        super.setType(type);
    }

    public PlayerSpeedChallengeCompletion getCompletion() {
        return completion;
    }

    public void setCompletion(PlayerSpeedChallengeCompletion completion) {
        this.completion = completion;
    }
    
}
