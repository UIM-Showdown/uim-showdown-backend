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
import org.uimshowdown.bingo.models.ContributionSubmission;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.Submission;
import org.uimshowdown.bingo.models.Team;
import org.uimshowdown.bingo.models.Tile;
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
        Map<Player, Double> numberOfSubmissions = getNumberOfSubmissions();
        List<Player> playersSorted = new ArrayList<Player>(numberOfSubmissions.keySet());
        playersSorted.sort((p1, p2) -> {
            if(Math.abs(numberOfSubmissions.get(p2) - numberOfSubmissions.get(p1)) < 0.00000001) {
                return 0;  
            } else if(numberOfSubmissions.get(p2) - numberOfSubmissions.get(p1) < 0) {
                return -1;
            } else {
                return 1;
            }
        });
        List<String> playerSubmissions = new ArrayList<String>();
        for(Player player : playersSorted) {
            playerSubmissions.add(player.getRsn() + ": " + Math.round(numberOfSubmissions.get(player)));
        }
        stats.put("numberOfSubmissions", playerSubmissions);
        
        // Players with most denied submissions
        Map<Player, Double> deniedSubmissions = getNumberOfDeniedSubmissions();
        playersSorted = new ArrayList<Player>(deniedSubmissions.keySet());
        playersSorted.sort((p1, p2) -> {
            if(Math.abs(deniedSubmissions.get(p2) - deniedSubmissions.get(p1)) < 0.00000001) {
                return 0;  
            } else if(deniedSubmissions.get(p2) - deniedSubmissions.get(p1) < 0) {
                return -1;
            } else {
                return 1;
            }
        });
        List<String> playerDeniedSubmissions = new ArrayList<String>();
        for(Player player : playersSorted) {
            playerDeniedSubmissions.add(player.getRsn() + ": " + Math.round(deniedSubmissions.get(player)));
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
        
        Map<String, Map<String, Double>> teamPointsByCategory = getTeamPointsByCategory();
        
        // Team proportions for category
        Map<String, Map<String, String>> categoryProportionsByTeam = new HashMap<String, Map<String, String>>();
        for(String teamName : teamPointsByCategory.keySet()) {
            Map<String, String> categoryProportions = new HashMap<String, String>();
            Map<String, Double> pointsByCategory = teamPointsByCategory.get(teamName);
            double pvmPoints = pointsByCategory.get("PVM");
            double skillingPoints = pointsByCategory.get("Skilling");
            double otherPoints = pointsByCategory.get("Other");
            double totalPoints = pvmPoints + skillingPoints + otherPoints;
            categoryProportions.put("PVM", String.format("%.2f", pvmPoints / totalPoints));
            categoryProportions.put("Skilling", String.format("%.2f", skillingPoints / totalPoints));
            categoryProportions.put("Other", String.format("%.2f", otherPoints / totalPoints));
            categoryProportionsByTeam.put(teamName, categoryProportions);
        }
        stats.put("categoryProportionsByTeam", categoryProportionsByTeam);
        
        // Team rankings for category
        Map<String, List<String>> categoryRankingsByTeam = new HashMap<String, List<String>>();
        List<String> teamNames = new ArrayList<String>();
        for(Team team : teamRepository.findAll()) {
            teamNames.add(team.getName());
        }
        teamNames.sort((t1, t2) -> { // Descending by PVM points
            if(teamPointsByCategory.get(t2).get("PVM") - teamPointsByCategory.get(t1).get("PVM") < 0) {
                return -1;
            } else {
                return 1;
            }
        });
        List<String> pvmRankings = new ArrayList<String>();
        for(String teamName : teamNames) {
            pvmRankings.add(teamName + ": " + String.format("%.2f", teamPointsByCategory.get(teamName).get("PVM")));
        }
        categoryRankingsByTeam.put("PVM", pvmRankings);
        teamNames.sort((t1, t2) -> { // Descending by Skilling points
            if(teamPointsByCategory.get(t2).get("Skilling") - teamPointsByCategory.get(t1).get("Skilling") < 0) {
                return -1;
            } else {
                return 1;
            }
        });
        List<String> skillingRankings = new ArrayList<String>();
        for(String teamName : teamNames) {
            skillingRankings.add(teamName + ": " + String.format("%.2f", teamPointsByCategory.get(teamName).get("Skilling")));
        }
        categoryRankingsByTeam.put("Skilling", skillingRankings);
        teamNames.sort((t1, t2) -> { // Descending by Other points
            if(teamPointsByCategory.get(t2).get("Other") - teamPointsByCategory.get(t1).get("Other") < 0) {
                return -1;
            } else {
                return 1;
            }
        });
        List<String> otherRankings = new ArrayList<String>();
        for(String teamName : teamNames) {
            otherRankings.add(teamName + ": " + String.format("%.2f", teamPointsByCategory.get(teamName).get("Other")));
        }
        categoryRankingsByTeam.put("Other", otherRankings);
        stats.put("categoryRankingsByTeam", categoryRankingsByTeam);
        
        // Diversity score
        Map<String, Double> diversityScores = getDiversityScores();
        List<String> rsns = new ArrayList<String>(diversityScores.keySet());
        rsns.sort((r1, r2) -> { // Descending by diversity score
            if(Math.abs(diversityScores.get(r2) - diversityScores.get(r1)) < 0.0000000001) {
                return 0;
            } else if(diversityScores.get(r2) - diversityScores.get(r1) < 0) {
                return -1;
            } else {
                return 1;
            }
        });
        List<String> diversityRankings = new ArrayList<String>();
        for(String rsn : rsns) {
            diversityRankings.add(rsn + ": " + String.format("%.2f", diversityScores.get(rsn)));
        }
        stats.put("diversityScores", diversityRankings);
        
        // Approvals by approver
        Map<String, Double> approvalsByApprover = getApprovalsByApprover();
        List<String> approvers = new ArrayList<String>(approvalsByApprover.keySet());
        approvers.sort((a1, a2) -> {
            if(Math.abs(approvalsByApprover.get(a2) - approvalsByApprover.get(a1)) < 0.00000001) {
                return 0;
            }else if(approvalsByApprover.get(a2) - approvalsByApprover.get(a1) < 0) {
                return -1;
            } else {
                return 1;
            }
        });
        List<String> approverRankings = new ArrayList<String>();
        for(String approver : approvers) {
            approverRankings.add(approver + ": " + Math.round(approvalsByApprover.get(approver)));
        }
        stats.put("approvalsByApprover", approverRankings);
        
        // Self-approvals by approver
        Map<String, Double> selfApprovalsByApprover = getSelfApprovalsByApprover();
        approvers = new ArrayList<String>(selfApprovalsByApprover.keySet());
        approvers.sort((a1, a2) -> {
            if(Math.abs(selfApprovalsByApprover.get(a2) - selfApprovalsByApprover.get(a1)) < 0.00000001) {
                return 0;
            }else if(selfApprovalsByApprover.get(a2) - selfApprovalsByApprover.get(a1) < 0) {
                return -1;
            } else {
                return 1;
            }
        });
        List<String> selfApproverRankings = new ArrayList<String>();
        for(String approver : approvers) {
            selfApproverRankings.add(approver + ": " + Math.round(selfApprovalsByApprover.get(approver)));
        }
        stats.put("selfApprovalsByApprover", selfApproverRankings);
        
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
    
    public Map<Player, Double> getNumberOfSubmissions() {
        Map<Player, Double> numberOfSubmissions = new HashMap<Player, Double>();
        for(Player player : playerRepository.findAll()) {
            double submissions = 0.0;
            for(Submission submission : player.getSubmissions()) {
                double val = 1.0;
                // Handle multi-submissions
                if(submission.getType() == Submission.Type.CONTRIBUTION) {
                    String methodName = ((ContributionSubmission) submission).getContributionMethod().getName();
                    if(methodName.contains("MTA")) {
                        val /= 4.0;
                    }
                    if(methodName.contains("LMS")) {
                        val /= 2.0;
                    }
                    if(methodName.contains("Doom of Mokhaiotl")) {
                        val /= 9.0;
                    }
                }
                submissions += val;
            }
            if(submissions > 0) {
                numberOfSubmissions.put(player, submissions);
            }
        }
        return numberOfSubmissions;
    }
    
    public Map<Player, Double> getNumberOfDeniedSubmissions() {
        Map<Player, Double> numberOfDeniedSubmissions = new HashMap<Player, Double>();
        for(Player player : playerRepository.findAll()) {
            double denied = 0.0;
            for(Submission submission : player.getSubmissions()) {
                if(submission.getState() == Submission.State.DENIED) {
                    double val = 1.0;
                    // Handle multi-submissions
                    if(submission.getType() == Submission.Type.CONTRIBUTION) {
                        String methodName = ((ContributionSubmission) submission).getContributionMethod().getName();
                        if(methodName.contains("MTA")) {
                            val /= 4.0;
                        }
                        if(methodName.contains("LMS")) {
                            val /= 2.0;
                        }
                        if(methodName.contains("Doom of Mokhaiotl")) {
                            val /= 9.0;
                        }
                    }
                    denied += val;
                }
            }
            if(denied > 0) {                
                numberOfDeniedSubmissions.put(player, denied);
            }
        }
        return numberOfDeniedSubmissions;
    }
    
    public Map<Submission.Type, Double> getTotalSubmissions() {
        Map<Submission.Type, Double> totalSubmissions = new HashMap<Submission.Type, Double>();
        totalSubmissions.put(Submission.Type.RECORD, 0.0);
        totalSubmissions.put(Submission.Type.CHALLENGE, 0.0);
        totalSubmissions.put(Submission.Type.CONTRIBUTION, 0.0);
        totalSubmissions.put(Submission.Type.CONTRIBUTION_INCREMENT, 0.0);
        totalSubmissions.put(Submission.Type.CONTRIBUTION_PURCHASE, 0.0);
        totalSubmissions.put(Submission.Type.COLLECTION_LOG, 0.0);
        for(Submission submission : submissionRepository.findAll()) {
            Submission.Type type = submission.getType();
            double val = 1.0;
            // Handle multi-submissions
            if(submission.getType() == Submission.Type.CONTRIBUTION) {
                String methodName = ((ContributionSubmission) submission).getContributionMethod().getName();
                if(methodName.contains("MTA")) {
                    val /= 4.0;
                }
                if(methodName.contains("LMS")) {
                    val /= 2.0;
                }
                if(methodName.contains("Doom of Mokhaiotl")) {
                    val /= 9.0;
                }
            }
            totalSubmissions.put(type, totalSubmissions.get(type) + val);
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
    
    public Map<String, Map<String, Double>> getTeamPointsByCategory() {
        Map<String, Map<String, Double>> teamPointsByCategory = new HashMap<String, Map<String, Double>>();
        for(Team team : teamRepository.findAll()) {
            Map<String, Double> points = new HashMap<String, Double>();
            double pvmPoints = 0.0;
            double skillingPoints = 0.0;
            double otherPoints = 0.0;
            for(Player player : team.getPlayers()) {
                pvmPoints += player.getScoreboard().getPvmTileContribution();
                skillingPoints += player.getScoreboard().getSkillingTileContribution();
                otherPoints += player.getScoreboard().getOtherTileContribution();
            }
            points.put("PVM", pvmPoints);
            points.put("Skilling", skillingPoints);
            points.put("Other", otherPoints);
            teamPointsByCategory.put(team.getName(), points);
        }
        return teamPointsByCategory;
    }
    
    public Map<String, Double> getDiversityScores() {
        Map<String, Double> diversityScores = new HashMap<String, Double>();
        for(Player player : playerRepository.findAll()) {
            Map<Tile, Double> ehtByTile = new HashMap<Tile, Double>();
            for(Contribution contribution : player.getContributions()) {
                Tile tile = contribution.getContributionMethod().getTile();
                if(ehtByTile.get(tile) != null) {
                    ehtByTile.put(tile, ehtByTile.get(tile) + contribution.getEHTValue());
                } else {
                    ehtByTile.put(tile, contribution.getEHTValue());
                }
            }
            int tilesWith1EHT = 0;
            int totalTiles = 0;
            for(Tile tile : ehtByTile.keySet()) {
                totalTiles++;
                if(ehtByTile.get(tile) > 1.0) {
                    tilesWith1EHT++;
                }
            }
            diversityScores.put(player.getRsn(), player.getScoreboard().getTotalTileContribution() * tilesWith1EHT / totalTiles);
        }
        return diversityScores;
    }
    
    public Map<String, Double> getApprovalsByApprover() {
        Map<String, Double> approvalsByApprover = new HashMap<String, Double>();
        for(Submission submission : submissionRepository.findAll()) {
            double val = 1.0;
            // Handle multi-submissions
            if(submission.getType() == Submission.Type.CONTRIBUTION) {
                String methodName = ((ContributionSubmission) submission).getContributionMethod().getName();
                if(methodName.contains("MTA")) {
                    val /= 4.0;
                }
                if(methodName.contains("LMS")) {
                    val /= 2.0;
                }
                if(methodName.contains("Doom of Mokhaiotl")) {
                    val /= 9.0;
                }
            }
            String approver = submission.getReviewer();
            if(approver != null) {
                if(approvalsByApprover.get(approver) != null) {
                    approvalsByApprover.put(approver, approvalsByApprover.get(approver) + val);
                } else {
                    approvalsByApprover.put(approver, val);
                }
            }
        }
        return approvalsByApprover;
    }
    
    public Map<String, Double> getSelfApprovalsByApprover() {
        Map<String, String> approverAliases = new HashMap<String, String>();
        approverAliases.put("trout#waitlister", "trout cooker");
        approverAliases.put("Flashcards #CHOOM", "Flashcards");
        approverAliases.put("DerekMK #BOOTY", "DerekMK");
        approverAliases.put("finance king", "finance king");
        approverAliases.put("Heiligtree #TOP3", "Heiligtree");
        approverAliases.put("Zyb #CHOOM", "Zyb");
        approverAliases.put("#WW UIM Dr4g0n", "UIM Dr4g0n");
        approverAliases.put("Shy #MLG", "shv");
        approverAliases.put("Mitaka Asa", "Mitaka Asa");
        approverAliases.put("U 42 #MLH", null);
        approverAliases.put("And Theatre of Mind", "Mitaka Asa");
        approverAliases.put("naternaut #mlg", "trout cooker");
        approverAliases.put("Dan (bingo_doer) #GG", "bingo_doer");
        approverAliases.put("McKennon #LL", "McKennon");
        approverAliases.put("Theatre of Mind", "RinkoLover");
        Map<String, Double> approvalsByApprover = new HashMap<String, Double>();
        for(Submission submission : submissionRepository.findAll()) {
            double val = 1.0;
            // Handle multi-submissions
            if(submission.getType() == Submission.Type.CONTRIBUTION) {
                String methodName = ((ContributionSubmission) submission).getContributionMethod().getName();
                if(methodName.contains("MTA")) {
                    val /= 4.0;
                }
                if(methodName.contains("LMS")) {
                    val /= 2.0;
                }
                if(methodName.contains("Doom of Mokhaiotl")) {
                    val /= 9.0;
                }
            }
            String approver = submission.getReviewer();
            if(approver != null && submission.getPlayer().getRsn().equals(approverAliases.get(approver))) {
                if(approvalsByApprover.get(approver) != null) {
                    approvalsByApprover.put(approver, approvalsByApprover.get(approver) + val);
                } else {
                    approvalsByApprover.put(approver, val);
                }
            }
        }
        return approvalsByApprover;
    }

}
