package org.uimshowdown.bingo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;

@Entity
@Table(name = "player_speed_challenge_completions")
public class PlayerSpeedChallengeCompletion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relay_component_id", nullable = true)
    private ChallengeRelayComponent relayComponent;

    @Column(name = "screenshot_url", length = 512)
    private String screenshotUrl;

    @Column
    private double seconds;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id")
    private SpeedChallengeSubmission submission;

    public int getId() {
        return id;
    }

    public SpeedChallengeCompletion getChallengeCompletion() {
        return player.getTeam().getSpeedChallengeCompletion(challenge);
    }

    public Player getPlayer() {
        return player;
    }

    public ChallengeRelayComponent getChallengeRelayComponent() {
        return relayComponent;
    }

    public String getScreenshotUrl() {
        return screenshotUrl;
    }

    public double getSeconds() {
        return seconds;
    }
    
    public Challenge getChallenge() {
        return challenge;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setRelayComponent(ChallengeRelayComponent challengeRelayComponent) {
        relayComponent = challengeRelayComponent;
    }

    public void setScreenshotUrl(String screenshotUrl) {
        this.screenshotUrl = screenshotUrl;
    }

    public void setSeconds(double seconds) {
        this.seconds = seconds;
    }
    
    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public SpeedChallengeSubmission getSubmission() {
        return submission;
    }

    public void setSubmission(SpeedChallengeSubmission submission) {
        this.submission = submission;
    }

    public ChallengeRelayComponent getRelayComponent() {
        return relayComponent;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    @PreRemove
    private void preRemove() {
        if(submission != null) { 
            submission.setCompletion(null);
            submission = null;
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof PlayerSpeedChallengeCompletion && ((PlayerSpeedChallengeCompletion) obj).getId() == this.id;
    }
    
}
