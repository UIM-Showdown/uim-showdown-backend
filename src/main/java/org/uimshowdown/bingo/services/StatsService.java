package org.uimshowdown.bingo.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.uimshowdown.bingo.models.Contribution;
import org.uimshowdown.bingo.models.ContributionMethod;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.Submission;
import org.uimshowdown.bingo.repositories.ContributionMethodRepository;
import org.uimshowdown.bingo.repositories.PlayerRepository;
import org.uimshowdown.bingo.repositories.SubmissionRepository;

@Component
public class StatsService {
    
    @Autowired ContributionMethodRepository contributionMethodRepository;
    @Autowired SubmissionRepository submissionRepository;
    @Autowired PlayerRepository playerRepository;
    
    public Map<String, Object> getStatsReport() {
        Map<String, Object> stats = new HashMap<String, Object>();
        
        // Total contribution for each method, split by type
        Map<ContributionMethod, Integer> allUnitsContributed = getAllUnitsContributed();
        List<ContributionMethod> methodsSorted = new ArrayList<ContributionMethod>(allUnitsContributed.keySet());
        methodsSorted.sort((m1, m2) -> allUnitsContributed.get(m2) - allUnitsContributed.get(m1));
        
        List<String> templeBossKCs = new ArrayList<String>();
        List<String> templeSkillXPs = new ArrayList<String>();
        List<String> submissionKCs = new ArrayList<String>();
        List<String> submissionItemDrops = new ArrayList<String>();
        List<String> submissionOthers = new ArrayList<String>();
        for(ContributionMethod method : methodsSorted) {
            if(method.getContributionMethodType() == ContributionMethod.Type.TEMPLE_KC) {                
                templeBossKCs.add(method.getName() + ": " + allUnitsContributed.get(method));
            }
            if(method.getContributionMethodType() == ContributionMethod.Type.TEMPLE_XP) {                
                templeSkillXPs.add(method.getName() + ": " + allUnitsContributed.get(method));
            }
            if(method.getContributionMethodType() == ContributionMethod.Type.SUBMISSION_KC) {                
                submissionKCs.add(method.getName() + ": " + allUnitsContributed.get(method));
            }
            if(method.getContributionMethodType() == ContributionMethod.Type.SUBMISSION_ITEM_DROP) {                
                submissionItemDrops.add(method.getName() + ": " + allUnitsContributed.get(method));
            }
            if(method.getContributionMethodType() == ContributionMethod.Type.SUBMISSION_OTHER) {                
                submissionOthers.add(method.getName() + ": " + allUnitsContributed.get(method));
            }
        }
        stats.put("templeBossKCs", templeBossKCs);
        stats.put("templeSkillXPs", templeSkillXPs);
        stats.put("submissionKCs", submissionKCs);
        stats.put("submissionItemDrops", submissionItemDrops);
        stats.put("submissionOthers", submissionOthers);
        
        // Total EHT for each method, split by type
        Map<ContributionMethod, Double> allEHTContributed = getAllEHTContributed();
        methodsSorted = new ArrayList<ContributionMethod>(allEHTContributed.keySet());
        methodsSorted.sort((m1, m2) -> {
            if(allEHTContributed.get(m2) - allEHTContributed.get(m1) < 0) {
                return -1;
            } else {
                return 1;
            }
        });
        List<String> templeBossEHTs = new ArrayList<String>();
        List<String> templeSkillEHTs = new ArrayList<String>();
        List<String> bossSubmissionEHTs = new ArrayList<String>();
        List<String> itemDropSubmissionEHTs = new ArrayList<String>();
        List<String> otherSubmissionEHTs = new ArrayList<String>();
        for(ContributionMethod method : methodsSorted) {
            if(method.getContributionMethodType() == ContributionMethod.Type.TEMPLE_KC) {                
                templeBossEHTs.add(method.getName() + ": " + allEHTContributed.get(method));
            }
            if(method.getContributionMethodType() == ContributionMethod.Type.TEMPLE_XP) {                
                templeSkillEHTs.add(method.getName() + ": " + allEHTContributed.get(method));
            }
            if(method.getContributionMethodType() == ContributionMethod.Type.SUBMISSION_KC) {                
                bossSubmissionEHTs.add(method.getName() + ": " + allEHTContributed.get(method));
            }
            if(method.getContributionMethodType() == ContributionMethod.Type.SUBMISSION_ITEM_DROP) {                
                itemDropSubmissionEHTs.add(method.getName() + ": " + allEHTContributed.get(method));
            }
            if(method.getContributionMethodType() == ContributionMethod.Type.SUBMISSION_OTHER) {                
                otherSubmissionEHTs.add(method.getName() + ": " + allEHTContributed.get(method));
            }
        }
        stats.put("templeBossEHTs", templeBossEHTs);
        stats.put("templeSkillEHTs", templeSkillEHTs);
        stats.put("bossSubmissionEHTs", bossSubmissionEHTs);
        stats.put("itemDropSubmissionEHTs", itemDropSubmissionEHTs);
        stats.put("otherSubmissionEHTs", otherSubmissionEHTs);
        
        // Players with most submissions
        Map<Player, Integer> numberOfSubmissions = getNumberOfSubmissions();
        List<Player> playersSorted = new ArrayList<Player>(numberOfSubmissions.keySet());
        playersSorted.sort((p1, p2) -> numberOfSubmissions.get(p2) - numberOfSubmissions.get(p1));
        List<String> playerSubmissions = new ArrayList<String>();
        for(Player player : playersSorted) {
            playerSubmissions.add(player.getRsn() + ": " + numberOfSubmissions.get(player));
        }
        stats.put("numberOfSubmissions", numberOfSubmissions);
        
        // Players with most denied submissions
        Map<Player, Integer> deniedSubmissions = getNumberOfDeniedSubmissions();
        playersSorted = new ArrayList<Player>(deniedSubmissions.keySet());
        playersSorted.sort((p1, p2) -> deniedSubmissions.get(p2) - deniedSubmissions.get(p1));
        List<String> playerDeniedSubmissions = new ArrayList<String>();
        for(Player player : playersSorted) {
            playerDeniedSubmissions.add(player.getRsn() + ": " + deniedSubmissions.get(player));
        }
        stats.put("deniedSubmissions", playerDeniedSubmissions);
        
        // Total submissions by type
        stats.put("totalSubmisisonsByType", getTotalSubmissions());
        
        // Top contributions
        stats.put("topContributions", getTopContributions());
        
        return stats;
    }
    
    public Map<ContributionMethod, Integer> getAllUnitsContributed() {
        Map<ContributionMethod, Integer> allUnitsContributed = new HashMap<ContributionMethod, Integer>();
        for(ContributionMethod method : contributionMethodRepository.findAll()) {
            int unitsContributed = 0;
            for(Contribution contribution : method.getPlayerContributions()) {
                unitsContributed += contribution.getUnitsContributed();
            }
            allUnitsContributed.put(method, unitsContributed);
        }
        return allUnitsContributed;
    }
    
    public Map<ContributionMethod, Double> getAllEHTContributed() {
        Map<ContributionMethod, Double> allEHTContributed = new HashMap<ContributionMethod, Double>();
        for(ContributionMethod method : contributionMethodRepository.findAll()) {
            double ehtContributed = 0.0;
            for(Contribution contribution : method.getPlayerContributions()) {
                ehtContributed += (double) contribution.getUnitsContributed() / method.getEhtRate();
            }
            allEHTContributed.put(method, ehtContributed);
        }
        return allEHTContributed;
    }
    
    public Map<Player, Integer> getNumberOfSubmissions() {
        Map<Player, Integer> numberOfSubmissions = new HashMap<Player, Integer>();
        for(Player player : playerRepository.findAll()) {
            numberOfSubmissions.put(player, player.getSubmissions().size());
        }
        return numberOfSubmissions;
    }
    
    public Map<Player, Integer> getNumberOfDeniedSubmissions() {
        Map<Player, Integer> numberOfDeniedSubmissions = new HashMap<Player, Integer>();
        for(Player player : playerRepository.findAll()) {
            int denied = 0;
            for(Submission submission : player.getSubmissions()) {
                if(submission.getState() == Submission.State.DENIED) {
                    denied++;
                }
            }
            numberOfDeniedSubmissions.put(player, denied);
        }
        return numberOfDeniedSubmissions;
    }
    
    public Map<Submission.Type, Integer> getTotalSubmissions() {
        Map<Submission.Type, Integer> totalSubmissions = new HashMap<Submission.Type, Integer>();
        totalSubmissions.put(Submission.Type.CHALLENGE, 0);
        totalSubmissions.put(Submission.Type.COLLECTION_LOG, 0);
        totalSubmissions.put(Submission.Type.CONTRIBUTION, 0);
        totalSubmissions.put(Submission.Type.RECORD, 0);
        totalSubmissions.put(Submission.Type.UNRANKED_STARTING_VALUE, 0);
        for(Submission submission : submissionRepository.findAll()) {
            Submission.Type type = submission.getType();
            totalSubmissions.put(type, totalSubmissions.get(type) + 1);
        }
        return totalSubmissions;
    }
    
    public Map<ContributionMethod, String> getTopContributions() {
        Map<ContributionMethod, String> topContributions = new HashMap<ContributionMethod, String>();
        for(ContributionMethod method : contributionMethodRepository.findAll()) {
            int topContribution = 0;
            Player topPlayer = null;
            for(Contribution contribution : method.getPlayerContributions()) {
                if(contribution.getUnitsContributed() > topContribution) {
                    topContribution = contribution.getUnitsContributed();
                    topPlayer = contribution.getPlayer();
                }
            }
            topContributions.put(method, topContribution + " by " + topPlayer.getRsn());
        }
        return topContributions;
    }

}
