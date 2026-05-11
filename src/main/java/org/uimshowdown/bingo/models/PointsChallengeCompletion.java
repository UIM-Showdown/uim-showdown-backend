package org.uimshowdown.bingo.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "points_challenge_completions")
public class PointsChallengeCompletion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public int getId() {
        return id;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public Set<PlayerPointsChallengeCompletion> getPlayerPointsChallengeCompletions() {
        Set<PlayerPointsChallengeCompletion> completions = new HashSet<PlayerPointsChallengeCompletion>();
        for(Player player : team.getPlayers()) {
            for(PlayerPointsChallengeCompletion completion : player.getPlayerPointsChallengeCompletions()) {
                if(completion.getChallenge().equals(this.challenge)) {
                    completions.add(completion);
                }
            }
        }
        return completions;
    }

    public Team getTeam() {
        return team;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
    
    public boolean isComplete() {
       return getPoints() > 0;
    }
    
    /**
     * Returns the point value for the challenge, or -1 if the challenge is incomplete.
     * 
     * @return
     */
    public int getPoints() {
        Set<PlayerPointsChallengeCompletion> playerPointsChallengeCompletions = getPlayerPointsChallengeCompletions();
        if(challenge.getTeamSize() > playerPointsChallengeCompletions.size()) { // Not enough individual submissions
            return -1;
        }
        // A player can only have one player challenge completion for a points challenge, so all our player completions 
        // are from separate players. This means we can do a hack here by returning the X'th highest point value, where 
        // X is the team size. This accounts for having a lower but "complete" point value, and a higher but "incomplete" point value
        // where not everyone has submitted (in which case the X'th highest point value is a part of the "complete" one).
        PlayerPointsChallengeCompletion[] completions = playerPointsChallengeCompletions.toArray(new PlayerPointsChallengeCompletion[0]);
        Integer[] pointValues = new Integer[completions.length];
        for(int i = 0; i < completions.length; i++) {
            pointValues[i] = completions[i].getPoints();
        }
        Arrays.sort(pointValues, Collections.reverseOrder());
        return pointValues[challenge.getTeamSize() - 1];
    }
    
    /**
     * Returns the list of players whose player completions were used in this completion.
     * 
     * See getPoints() for details on how these are determined.
     * @return
     */
    public List<Player> getPlayers() {
        Set<PlayerPointsChallengeCompletion> playerPointsChallengeCompletions = getPlayerPointsChallengeCompletions();
        // Get all players with a player challenge completion that is the best "complete" point value we have, up to 
        // the team size
        if(challenge.getTeamSize() > playerPointsChallengeCompletions.size()) { // Not enough individual submissions
            return null;
        }
        int points = this.getPoints();
        List<Player> players = new ArrayList<Player>();
        for(PlayerPointsChallengeCompletion completion : playerPointsChallengeCompletions) {
            if(completion.getPoints() == points && players.size() < challenge.getTeamSize()) { // This is one of the players who got that time
                players.add(completion.getPlayer());
            }
        }
        return players;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof PointsChallengeCompletion && ((PointsChallengeCompletion) obj).getId() == this.id;
    }
    
}
