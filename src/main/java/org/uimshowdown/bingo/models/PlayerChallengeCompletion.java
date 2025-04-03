package org.uimshowdown.bingo.models;

import java.util.Objects;

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
@Table(name = "player_challenge_completions")
public class PlayerChallengeCompletion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_completion_id")
    private ChallengeCompletion challengeCompletion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relay_component_id", nullable = true)
    private ChallengeRelayComponent relayComponent;

    @Column(name = "screenshot_url", length = 512)
    private String screenshotUrl;

    @Column
    private Double seconds;

    public Integer getId() {
        return id;
    }

    public ChallengeCompletion getChallengeCompletion() {
        return challengeCompletion;
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

    public Double getSeconds() {
        return seconds;
    }

    public void setChallengeCompletion(ChallengeCompletion challengeCompletion) {
        this.challengeCompletion = challengeCompletion;
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

    public void setSeconds(Double seconds) {
        this.seconds = seconds;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if ((obj instanceof PlayerChallengeCompletion) == false) {
            return false;
        }

        PlayerChallengeCompletion otherPlayerChallengeCompletion = (PlayerChallengeCompletion) obj;
        return Integer.compare(id, otherPlayerChallengeCompletion.getId()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, screenshotUrl, seconds);
    }
}
