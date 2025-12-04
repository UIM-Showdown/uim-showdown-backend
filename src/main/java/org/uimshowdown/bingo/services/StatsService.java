package org.uimshowdown.bingo.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.uimshowdown.bingo.models.CollectionLogCompletion;
import org.uimshowdown.bingo.models.CollectionLogItem;
import org.uimshowdown.bingo.models.Contribution;
import org.uimshowdown.bingo.models.ContributionMethod;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.Submission;
import org.uimshowdown.bingo.models.Team;
import org.uimshowdown.bingo.repositories.CollectionLogItemRepository;
import org.uimshowdown.bingo.repositories.ContributionMethodRepository;
import org.uimshowdown.bingo.repositories.PlayerRepository;
import org.uimshowdown.bingo.repositories.SubmissionRepository;
import org.uimshowdown.bingo.repositories.TeamRepository;

@Component
public class StatsService {
    
    @Autowired ContributionMethodRepository contributionMethodRepository;
    @Autowired SubmissionRepository submissionRepository;
    @Autowired PlayerRepository playerRepository;
    @Autowired TeamRepository teamRepository;
    @Autowired CollectionLogItemRepository collectionLogItemRepository;
    
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
                templeBossEHTs.add(method.getName() + ": " + String.format("%.2f", allEHTContributed.get(method)));
            }
            if(method.getContributionMethodType() == ContributionMethod.Type.TEMPLE_XP) {                
                templeSkillEHTs.add(method.getName() + ": " + String.format("%.2f", allEHTContributed.get(method)));
            }
            if(method.getContributionMethodType() == ContributionMethod.Type.SUBMISSION_KC) {                
                bossSubmissionEHTs.add(method.getName() + ": " + String.format("%.2f", allEHTContributed.get(method)));
            }
            if(method.getContributionMethodType() == ContributionMethod.Type.SUBMISSION_ITEM_DROP) {                
                itemDropSubmissionEHTs.add(method.getName() + ": " + String.format("%.2f", allEHTContributed.get(method)));
            }
            if(method.getContributionMethodType() == ContributionMethod.Type.SUBMISSION_OTHER) {                
                otherSubmissionEHTs.add(method.getName() + ": " + String.format("%.2f", allEHTContributed.get(method)));
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
        stats.put("numberOfSubmissions", playerSubmissions);
        
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
        
        // Pets by team
        stats.put("petsByTeam", getPetsByTeam());
        
        // Jars by team
        stats.put("jarsByTeam", getJarsByTeam());
        
        // Purples by team
        stats.put("purplesByTeam", getPurplesByTeam());
        
        // Items only obtained by one team
        stats.put("itemsOnlyObtainedByOneTeam", getItemsOnlyObtainedByOneTeam());
        
        // Items not obtained
        stats.put("itemsNotObtained", getItemsNotObtained());
        
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
            if(player.getSubmissions().size() > 0) {                
                numberOfSubmissions.put(player, player.getSubmissions().size());
            }
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
            if(denied > 0) {                
                numberOfDeniedSubmissions.put(player, denied);
            }
        }
        return numberOfDeniedSubmissions;
    }
    
    public Map<Submission.Type, Integer> getTotalSubmissions() {
        Map<Submission.Type, Integer> totalSubmissions = new HashMap<Submission.Type, Integer>();
        totalSubmissions.put(Submission.Type.CHALLENGE, 0);
        totalSubmissions.put(Submission.Type.COLLECTION_LOG, 0);
        totalSubmissions.put(Submission.Type.CONTRIBUTION, 0);
        totalSubmissions.put(Submission.Type.RECORD, 0);
        for(Submission submission : submissionRepository.findAll()) {
            Submission.Type type = submission.getType();
            totalSubmissions.put(type, totalSubmissions.get(type) + 1);
        }
        return totalSubmissions;
    }
    
    public Map<String, String> getTopContributions() {
        Map<String, String> topContributions = new HashMap<String, String>();
        for(ContributionMethod method : contributionMethodRepository.findAll()) {
            int topContribution = 0;
            Player topPlayer = null;
            for(Contribution contribution : method.getPlayerContributions()) {
                if(contribution.getUnitsContributed() > topContribution) {
                    topContribution = contribution.getUnitsContributed();
                    topPlayer = contribution.getPlayer();
                }
            }
            if(topContribution > 0) {                
                topContributions.put(method.getName(), topContribution + " by " + topPlayer.getRsn());
            }
        }
        return topContributions;
    }
    
    public Map<String, List<String>> getPetsByTeam() {
        Map<String, List<String>> petsByTeam = new HashMap<String, List<String>>();
        for(Team team : teamRepository.findByOrderByIdAsc()) {
            List<String> pets = new ArrayList<String>();
            for(CollectionLogCompletion completion : team.getCollectionLogCompletions()) {
                if(completion.getItem().getType() == CollectionLogItem.Type.PET) {
                    pets.add(completion.getItem().getName());
                }
            }
            petsByTeam.put(team.getName(), pets);
        }
        return petsByTeam;
    }
    
    public Map<String, List<String>> getJarsByTeam() {
        Map<String, List<String>> jarsByTeam = new HashMap<String, List<String>>();
        for(Team team : teamRepository.findByOrderByIdAsc()) {
            List<String> pets = new ArrayList<String>();
            for(CollectionLogCompletion completion : team.getCollectionLogCompletions()) {
                if(completion.getItem().getType() == CollectionLogItem.Type.JAR) {
                    pets.add(completion.getItem().getName());
                }
            }
            jarsByTeam.put(team.getName(), pets);
        }
        return jarsByTeam;
    }
    
    public Map<String, List<String>> getPurplesByTeam() {
        String[] purples = new String[] {
          "Dexterous prayer scroll",
          "Arcane prayer scroll",
          "Twisted buckler",
          "Dragon hunter crossbow",
          "Dinh's bulwark",
          "Ancestral hat",
          "Ancestral robe top",
          "Ancestral robe bottoms",
          "Dragon claws",
          "Elder maul",
          "Kodai insignia",
          "Twisted bow",
          "Avernic defender hilt",
          "Ghrazi rapier",
          "Sanguinesti staff (uncharged)",
          "Justiciar faceguard",
          "Justiciar chestguard",
          "Justiciar legguards",
          "Scythe of vitur (uncharged)",
          "Osmumten's fang",
          "Lightbearer",
          "Elidinis' ward",
          "Masori mask",
          "Masori body",
          "Masori chaps",
          "Tumeken's shadow (uncharged)"
        };
        
        Map<String, List<String>> purplesByTeam = new HashMap<String, List<String>>();
        for(Team team : teamRepository.findByOrderByIdAsc()) {
            List<String> purplesOnTeam = new ArrayList<String>();
            for(CollectionLogCompletion completion : team.getCollectionLogCompletions()) {
                for(String purple : purples) {
                    if(purple.equals(completion.getItem().getName())) {
                        purplesOnTeam.add(completion.getItem().getName());
                    }
                }
            }
            purplesByTeam.put(team.getName(), purplesOnTeam);
        }
        return purplesByTeam;
    }
    
    public Map<String, String> getItemsOnlyObtainedByOneTeam() {
        Map<String, String> itemsOnlyObtainedByOneTeam = new HashMap<String, String>();
        for(CollectionLogItem item : collectionLogItemRepository.findAll()) {
            if(item.getType() != CollectionLogItem.Type.NORMAL) {
                continue;
            }
            int teams = 0;
            Team teamFound = null;
            for(Team team : teamRepository.findAll()) {
                for(CollectionLogCompletion completion : team.getCollectionLogCompletions()) {
                    if(completion.getItem().equals(item)) {
                        teams++;
                        teamFound = team;
                        break;
                    }
                }
            }
            if(teams == 1) {
                itemsOnlyObtainedByOneTeam.put(item.getName(), teamFound.getName());
            }
        }
        return itemsOnlyObtainedByOneTeam;
    }
    
    public List<String> getItemsNotObtained() {
        List<String> itemsNotObtained = new ArrayList<String>();
        for(CollectionLogItem item : collectionLogItemRepository.findAll()) {
            if(item.getType() != CollectionLogItem.Type.NORMAL) {
                continue;
            }
            boolean found = false;
            for(Team team : teamRepository.findAll()) {
                for(CollectionLogCompletion completion : team.getCollectionLogCompletions()) {
                    if(completion.getItem().equals(item)) {
                        found = true;
                        break;
                    }
                }
                if(found) {
                    break;
                }
            }
            if(!found) {
                itemsNotObtained.add(item.getName());
            }
        }
        return itemsNotObtained;
    }

}
