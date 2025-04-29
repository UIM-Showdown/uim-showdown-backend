package org.uimshowdown.bingo.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Collections2;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public int getId() {
        return id;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public Set<PlayerChallengeCompletion> getPlayerChallengeCompletions() {
        Set<PlayerChallengeCompletion> completions = new HashSet<PlayerChallengeCompletion>();
        for(Player player : team.getPlayers()) {
            for(PlayerChallengeCompletion completion : player.getPlayerChallengeCompletions()) {
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
       return getSeconds() > 0;
    }
    
    private boolean hasRelayComponent(ChallengeRelayComponent component) {
        for(PlayerChallengeCompletion completion : this.getPlayerChallengeCompletions()) {
            if(component.equals(completion.getChallengeRelayComponent())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Returns the completion time for the challenge in seconds, or -1.0 if the challenge is incomplete.
     * 
     * For a team speedrun, this is the fastest time for which there are X different players with submissions of that time 
     * or faster (to account for float accuracy or attempts where not everyone has submitted yet), where X is the team size.
     * 
     * For a relay, this is the fastest sum of X different submissions from distinct players with distinct relay components, 
     * where X is the team size.
     * 
     * @return
     */
    public double getSeconds() {
        Set<PlayerChallengeCompletion> playerChallengeCompletions = getPlayerChallengeCompletions();
        if(challenge.getType() == Challenge.Type.SPEEDRUN) {
            if(challenge.getTeamSize() > playerChallengeCompletions.size()) { // Not enough individual submissions
                return -1.0;
            }
            // A player can only have one player challenge completion for a team speedrun (non-relay), so all our player completions 
            // are from separate players. This means we can do a hack here by returning the time of the X'th fastest time, where 
            // X is the team size. This accounts for having a slower but "complete" time, and a faster but "incomplete" time where 
            // not everyone has submitted (in which case the X'th fastest time is a part of the "complete" one).
            PlayerChallengeCompletion[] completions = playerChallengeCompletions.toArray(new PlayerChallengeCompletion[0]);
            double[] times = new double[completions.length];
            for(int i = 0; i < completions.length; i++) {
                times[i] = completions[i].getSeconds();
            }
            Arrays.sort(times);
            return times[challenge.getTeamSize() - 1];
        } else if(challenge.getType() == Challenge.Type.RELAY){
            for(ChallengeRelayComponent component : challenge.getRelayComponents()) {
                if(!hasRelayComponent(component)) { // Missing a relay component
                    return -1.0;
                }
            }
            // We have to find the lowest sum of X different player challenge completion times with distinct players and relay components, 
            // where X is the team size. This is a hack to just check all of the different combinations.
            //
            // Basically we get every possible ordering of completions and take the first X elements of each ordering, then 
            // see if that subset is valid as a relay completion. Then, find the minimum sum of all valid subsets.
            double fastestSum = -1.0;
            Collection<List<PlayerChallengeCompletion>> orderings = Collections2.permutations(playerChallengeCompletions);
            for(List<PlayerChallengeCompletion> ordering : orderings) {
                List<PlayerChallengeCompletion> subset = ordering.subList(0, challenge.getTeamSize());
                boolean subsetIsValid = true;
                for(PlayerChallengeCompletion completion1 : subset) {
                    for(PlayerChallengeCompletion completion2 : subset) {
                        if(!completion1.equals(completion2) && completion1.getPlayer().equals(completion2.getPlayer())) {
                            subsetIsValid = false;
                        }
                        if(!completion1.equals(completion2) && completion1.getChallengeRelayComponent().equals(completion2.getChallengeRelayComponent())) {
                            subsetIsValid = false;
                        }
                    }
                }
                if(subsetIsValid) {
                    double sum = 0;
                    for(PlayerChallengeCompletion completion : subset) {
                        sum += completion.getSeconds();
                    }
                    if(fastestSum < 0 || sum < fastestSum) {
                        fastestSum = sum;
                    }
                }
            }
            // It's possible that fastestSum can still be unchanged at this point, in which case there aren't enough unique players for the relay to be complete.
            // In this case, fastestSum is still -1, so we're still returning the right thing.
            return fastestSum;
        }
        return -1.0;
    }
    
    /**
     * Returns the list of players whose player completions were used in this completion.
     * 
     * See getSeconds() for details on how these are determined.
     * @return
     */
    public List<Player> getPlayers() {
        Set<PlayerChallengeCompletion> playerChallengeCompletions = getPlayerChallengeCompletions();
        if(challenge.getType() == Challenge.Type.SPEEDRUN) {
            if(challenge.getTeamSize() > playerChallengeCompletions.size()) { // Not enough individual submissions
                return null;
            }
            // A player can only have one player challenge completion for a team speedrun (non-relay), so all our player completions 
            // are from separate players. This means we can do a hack here by returning the time of the X'th fastest time, where 
            // X is the team size. This accounts for having a slower but "complete" time, and a faster but "incomplete" time where 
            // not everyone has submitted (in which case the X'th fastest time is a part of the "complete" one).
            PlayerChallengeCompletion[] completions = playerChallengeCompletions.toArray(new PlayerChallengeCompletion[0]);
            double[] times = new double[completions.length];
            for(int i = 0; i < completions.length; i++) {
                times[i] = completions[i].getSeconds();
            }
            Arrays.sort(times);
            List<Player> players = new ArrayList<Player>();
            for(PlayerChallengeCompletion completion : playerChallengeCompletions) {
                if(Math.abs(completion.getSeconds() - times[challenge.getTeamSize() - 1]) < 0.001) { // This is one of the players who got that time
                    players.add(completion.getPlayer());
                }
            }
        } else if(challenge.getType() == Challenge.Type.RELAY){
            for(ChallengeRelayComponent component : challenge.getRelayComponents()) {
                if(!hasRelayComponent(component)) { // Missing a relay component
                    return null;
                }
            }
            // We have to find the lowest sum of X different player challenge completion times with distinct players and relay components, 
            // where X is the team size. This is a hack to just check all of the different combinations.
            //
            // Basically we get every possible ordering of completions and take the first X elements of each ordering, then 
            // see if that subset is valid as a relay completion. Then, find the minimum sum of all valid subsets.
            double fastestSum = -1.0;
            List<PlayerChallengeCompletion> fastestSubset = null;
            Collection<List<PlayerChallengeCompletion>> orderings = Collections2.permutations(playerChallengeCompletions);
            for(List<PlayerChallengeCompletion> ordering : orderings) {
                List<PlayerChallengeCompletion> subset = ordering.subList(0, challenge.getTeamSize());
                boolean subsetIsValid = true;
                for(PlayerChallengeCompletion completion1 : subset) {
                    for(PlayerChallengeCompletion completion2 : subset) {
                        if(!completion1.equals(completion2) && completion1.getPlayer().equals(completion2.getPlayer())) {
                            subsetIsValid = false;
                        }
                        if(!completion1.equals(completion2) && completion1.getChallengeRelayComponent().equals(completion2.getChallengeRelayComponent())) {
                            subsetIsValid = false;
                        }
                    }
                }
                if(subsetIsValid) {
                    double sum = 0;
                    for(PlayerChallengeCompletion completion : subset) {
                        sum += completion.getSeconds();
                    }
                    if(fastestSum < 0 || sum < fastestSum) {
                        fastestSum = sum;
                        fastestSubset = subset;
                    }
                }
            }
            if(fastestSubset == null) {
                return null;
            }
            List<Player> players = new ArrayList<Player>();
            for(PlayerChallengeCompletion completion : fastestSubset) {
                players.add(completion.getPlayer());
            }
            return players;
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof ChallengeCompletion && ((ChallengeCompletion) obj).getId() == this.id;
    }
    
}
