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
@Table(name = "player_challenge_completions")
public class PlayerPointsChallengeCompletion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @Column(name = "screenshot_url", length = 512)
    private String screenshotUrl;

    @Column
    private int points;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id")
    private PointsChallengeSubmission submission;

    public int getId() {
        return id;
    }

    public PointsChallengeCompletion getChallengeCompletion() {
        return player.getTeam().getPointsChallengeCompletion(challenge);
    }

    public Player getPlayer() {
        return player;
    }

    public String getScreenshotUrl() {
        return screenshotUrl;
    }

    public int getPoints() {
        return points;
    }
    
    public Challenge getChallenge() {
        return challenge;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setScreenshotUrl(String screenshotUrl) {
        this.screenshotUrl = screenshotUrl;
    }

    public void setPoints(int points) {
        this.points = points;
    }
    
    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public PointsChallengeSubmission getSubmission() {
        return submission;
    }

    public void setSubmission(PointsChallengeSubmission submission) {
        this.submission = submission;
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
        return obj != null && obj instanceof PlayerPointsChallengeCompletion && ((PlayerPointsChallengeCompletion) obj).getId() == this.id;
    }
    
}
