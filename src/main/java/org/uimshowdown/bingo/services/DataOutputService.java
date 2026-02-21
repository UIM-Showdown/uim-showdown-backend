package org.uimshowdown.bingo.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.uimshowdown.bingo.configuration.CompetitionConfiguration;
import org.uimshowdown.bingo.models.Challenge;
import org.uimshowdown.bingo.models.ChallengeLeaderboardEntry;
import org.uimshowdown.bingo.models.ChallengeRelayComponent;
import org.uimshowdown.bingo.models.CollectionLogCompletion;
import org.uimshowdown.bingo.models.CollectionLogItem;
import org.uimshowdown.bingo.models.ContributionMethod;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.PlayerChallengeCompletion;
import org.uimshowdown.bingo.models.PlayerScoreboard;
import org.uimshowdown.bingo.models.Record;
import org.uimshowdown.bingo.models.RecordCompletion;
import org.uimshowdown.bingo.models.RecordLeaderboardEntry;
import org.uimshowdown.bingo.models.Team;
import org.uimshowdown.bingo.models.TeamScoreboard;
import org.uimshowdown.bingo.models.Tile;
import org.uimshowdown.bingo.models.TileProgress;
import org.uimshowdown.bingo.repositories.ChallengeLeaderboardEntryRepository;
import org.uimshowdown.bingo.repositories.ChallengeRelayComponentRepository;
import org.uimshowdown.bingo.repositories.ChallengeRepository;
import org.uimshowdown.bingo.repositories.CollectionLogCompletionRepository;
import org.uimshowdown.bingo.repositories.CollectionLogItemRepository;
import org.uimshowdown.bingo.repositories.ContributionMethodRepository;
import org.uimshowdown.bingo.repositories.PlayerScoreboardRepository;
import org.uimshowdown.bingo.repositories.RecordLeaderboardEntryRepository;
import org.uimshowdown.bingo.repositories.RecordRepository;
import org.uimshowdown.bingo.repositories.TeamRepository;
import org.uimshowdown.bingo.repositories.TeamScoreboardRepository;
import org.uimshowdown.bingo.repositories.TileRepository;

import com.google.api.services.sheets.v4.model.ValueRange;

@Component
public class DataOutputService {
    
    @Autowired CompetitionConfiguration competitionConfiguration;
    
    @Autowired GoogleSheetsService googleSheetsService;
    
    @Autowired TeamRepository teamRepository;
    @Autowired TileRepository tileRepository;
    @Autowired RecordRepository recordRepository;
    @Autowired ChallengeRepository challengeRepository;
    @Autowired RecordLeaderboardEntryRepository recordLeaderboardEntryRepository;
    @Autowired ChallengeLeaderboardEntryRepository challengeLeaderboardEntryRepository;
    @Autowired PlayerScoreboardRepository playerScoreboardRepository;
    @Autowired TeamScoreboardRepository teamScoreboardRepository;
    @Autowired CollectionLogCompletionRepository collectionLogCompletionRepository;
    @Autowired ChallengeRelayComponentRepository challengeRelayComponentRepository;
    @Autowired ContributionMethodRepository contributionMethodRepository;
    @Autowired CollectionLogItemRepository collectionLogItemRepository;
    
    public void outputData() throws Exception {
        List<ValueRange> rowLists = new ArrayList<ValueRange>();
        rowLists.add(googleSheetsService.createUpdateRequest("unf_LastUpdated", getLastUpdatedRows()));
        rowLists.add(googleSheetsService.createUpdateRequest("unf_StandingsMainLeaderboard", getMainLeaderboardRows()));
        rowLists.add(googleSheetsService.createUpdateRequest("unf_StandingsRecordLeaderboard", getRecordLeaderboardRows()));
        rowLists.add(googleSheetsService.createUpdateRequest("unf_StandingsChallengeLeaderboard", getChallengeLeaderboardRows()));
        rowLists.add(googleSheetsService.createUpdateRequest("unf_StandingsMVPLeaderboard", getMVPLeaderboardRows()));
        rowLists.add(googleSheetsService.createUpdateRequest("unf_MVPDetails", getMVPDetailsRows()));
        rowLists.add(googleSheetsService.createUpdateRequest("unf_MVPExtras", getMVPExtrasRows()));
        for(Team team : teamRepository.findAll()) {
            rowLists.add(googleSheetsService.createUpdateRequest("unf_BoardDetails" + team.getAbbreviation(), getBoardDetailsRows(team)));
            rowLists.add(googleSheetsService.createUpdateRequest("unf_RecordDetails" + team.getAbbreviation(), getRecordDetailsRows(team)));
            rowLists.add(googleSheetsService.createUpdateRequest("unf_ChallengeDetails" + team.getAbbreviation(), getChallengeDetailsRows(team)));
            rowLists.add(googleSheetsService.createUpdateRequest("unf_MVPRace" + team.getAbbreviation(), getMVPRaceRows(team)));
            rowLists.add(googleSheetsService.createUpdateRequest("unf_RelayComponents" + team.getAbbreviation(), getRelayComponentsRows(team)));
            rowLists.add(googleSheetsService.createUpdateRequest("unf_ProgressOverview" + team.getAbbreviation(), getProgressOverviewRows(team)));
            rowLists.add(googleSheetsService.createUpdateRequest("unf_Progress" + team.getAbbreviation(), getProgressRows(team)));
            rowLists.add(googleSheetsService.createUpdateRequest("unf_ClogItems" + team.getAbbreviation(), getClogItems(team)));
            rowLists.add(googleSheetsService.createUpdateRequest("unf_PetsAndJars" + team.getAbbreviation(), getPetsAndJars(team)));
        }
        
        // We need to clear the tabs before writing to them because some tabs (especially leaderboards) might end up with 
        // fewer values than before.
        //
        // There's not an easy way to include "clear tab" requests in the same batch update as "update values" requests, 
        // and if we do a "clear tab" batch request beforehand, everything will look blank to players while it's updating.
        // Because of this, we're doing a bit of a hack by filling the tab with blank rows before adding the actual values.
        //
        // Basically, we're clearing the full range of what we're about to update, along with 10 extra rows of the same length as 
        // the data.
        
        List<ValueRange> updates = new ArrayList<ValueRange>();
        for(ValueRange rowList : rowLists) {
            int numColumns = rowList.getValues().get(0).size();
            int numRows = rowList.getValues().size();
            updates.add(googleSheetsService.createUpdateRequest(rowList.getRange(), generateBlankRows(numColumns, numRows + 10)));
            updates.add(rowList);
        }
        googleSheetsService.executeBatchUpdate(updates);
    }
    
    public void initializeTabs() throws Exception {
        googleSheetsService.initializeTabs();
    }
    
    private List<List<Object>> getLastUpdatedRows() {
        List<List<Object>> rows = new ArrayList<List<Object>>();
        List<Object> row = new ArrayList<Object>();
        row.add("Last Updated");
        row.add(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + " UTC");
        rows.add(row);
        return rows;
    }
    
    private List<List<Object>> getMainLeaderboardRows() {
        List<List<Object>> rows = new ArrayList<List<Object>>();
        
        // Add title row
        List<Object> titleRow = new ArrayList<Object>();
        titleRow.addAll(Arrays.asList(
                "place",
                "teamName",
                "captains",
                "mvp",
                "eventPoints",
                "eventPointsFromTileComp",
                "eventPointsFromGroupBonuses",
                "eventPointsFromRecordsAndChallenges",
                "eventPointsFromCollectionLog"));
        for(Tile tile : tileRepository.findByOrderByIdAsc()) {
            titleRow.add("tier: " + tile.getName());
        }
        rows.add(titleRow);
        
        // Add team scoreboards
        List<TeamScoreboard> scoreboards = new ArrayList<TeamScoreboard>();
        for(TeamScoreboard scoreboard : teamScoreboardRepository.findAll()) {
            if(scoreboard.getTeam().getName().equals(competitionConfiguration.getWaitlistTeamName())) {
                // Waitlist team doesn't go on the leaderboard
                continue;
            }
            scoreboards.add(scoreboard);
        }
        scoreboards.sort((s1, s2) -> s2.getEventPoints() - s1.getEventPoints()); // Descending
        int i = 1;
        for(TeamScoreboard scoreboard : scoreboards) {
            Team team = scoreboard.getTeam();
            String captains = "";
            for(String captain : team.getCaptainRsns()) {
                captains += captain + " & ";
            }
            if(!captains.equals("")) {                
                captains = captains.substring(0, captains.length() - 3);
            } else {
                captains = "None";
            }
            List<Player> players = new ArrayList<Player>(team.getPlayers());
            players.sort((p1, p2) -> { // Descending, with a 0 case to account for floating point math
                if(Math.abs(p2.getScoreboard().getTotalTileContribution() - p1.getScoreboard().getTotalTileContribution()) < 0.00000001) {
                    return 0;
                } else if(p2.getScoreboard().getTotalTileContribution() - p1.getScoreboard().getTotalTileContribution() < 0) {
                    return -1;
                } else {
                    return 1;
                }
               }
            );
            String mvp;
            if(!players.isEmpty()) {
                mvp = players.get(0).getRsn();
            } else {                
                mvp = "None";
            }
            List<Object> row = new ArrayList<Object>();
            row.add(i++);
            row.add(team.getName());
            row.add(captains);
            row.add(mvp);
            row.add(scoreboard.getEventPoints());
            row.add(scoreboard.getEventPointsFromTiles());
            row.add(scoreboard.getEventPointsFromGroups());
            row.add(scoreboard.getEventPointsFromRecordsAndChallenges());
            row.add(scoreboard.getEventPointsFromCollectionLogItems());
            for(Tile tile : tileRepository.findByOrderByIdAsc()) {
                row.add(team.getTileProgress(tile).getTier());
            }
            rows.add(row);
        }
        return rows;
    }
    
    private List<List<Object>> getRecordLeaderboardRows() {
        List<List<Object>> rows = new ArrayList<List<Object>>();
        
        // Add title row
        List<Object> titleRow = new ArrayList<Object>();
        titleRow.add("place");
        for(Record record : recordRepository.findByOrderByIdAsc()) {
            titleRow.addAll(Arrays.asList(
                    "rsn: " + record.getName(),
                    "team: " + record.getName(),
                    "value: " + record.getName(),
                    "points: " + record.getName()));
        }
        rows.add(titleRow);
        
        // Add record leaderboard entries
        for(int place = 1; place <= teamRepository.count(); place++) {
            List<Object> row = new ArrayList<Object>();
            row.add(place);
            for(Record record : recordRepository.findByOrderByIdAsc()) {
                List<RecordLeaderboardEntry> entries = new ArrayList<RecordLeaderboardEntry>();
                for(RecordLeaderboardEntry entry : recordLeaderboardEntryRepository.findValidEntriesByRecordNameOrderByPlaceAsc(record.getName())) {
                    entries.add(entry);
                }
                if(entries.size() >= place) {
                    RecordLeaderboardEntry entry = entries.get(place - 1);
                    row.add(entry.getPlayerName());
                    row.add(entry.getTeamScoreboard().getTeam().getAbbreviation());
                    row.add(entry.getValue());
                    row.add(entry.getPoints());
                } else {
                    row.add("");
                    row.add("");
                    row.add("");
                    row.add("");
                }
            }
            rows.add(row);
        }
        return rows;
    }
    
    private List<List<Object>> getChallengeLeaderboardRows() {
        List<List<Object>> rows = new ArrayList<List<Object>>();
        
        // Add title row
        List<Object> titleRow = new ArrayList<Object>();
        titleRow.add("place");
        for(Challenge challenge : challengeRepository.findByOrderByIdAsc()) {
            titleRow.addAll(Arrays.asList(
                    "players: " + challenge.getName(),
                    "team: " + challenge.getName(),
                    "time: " + challenge.getName(),
                    "points: " + challenge.getName()));
        }
        rows.add(titleRow);
        
        // Add record leaderboard entries
        for(int place = 1; place <= teamRepository.count(); place++) {
            List<Object> row = new ArrayList<Object>();
            row.add(place);
            for(Challenge challenge : challengeRepository.findByOrderByIdAsc()) {
                List<ChallengeLeaderboardEntry> entries = new ArrayList<ChallengeLeaderboardEntry>();
                for(ChallengeLeaderboardEntry entry : challengeLeaderboardEntryRepository.findValidEntriesByChallengeNameOrderByPlaceAsc(challenge.getName())) {
                    entries.add(entry);
                }
                if(entries.size() >= place) {
                    ChallengeLeaderboardEntry entry = entries.get(place - 1);
                    row.add(entry.getPlayerNames());
                    row.add(entry.getTeamScoreboard().getTeam().getAbbreviation());
                    row.add(entry.getSeconds() / 86400.0); // Convert to sheets format - unit is one day
                    row.add(entry.getPoints());
                } else {
                    row.add("");
                    row.add("");
                    row.add("");
                    row.add("");
                }
            }
            rows.add(row);
        }
        return rows;
    }
    
    private List<List<Object>> getMVPLeaderboardRows() {
        List<List<Object>> rows = new ArrayList<List<Object>>();
        
        // Add title row
        List<Object> titleRow = new ArrayList<Object>();
        titleRow.add("place");
        titleRow.add("player");
        titleRow.add("points");
        titleRow.add("team");
        rows.add(titleRow);
        
        // Add leaderboard
        List<Team> teamsUsed = new ArrayList<Team>();
        int place = 1;
        for(PlayerScoreboard scoreboard : playerScoreboardRepository.findByOrderByTotalTileContributionDesc()) {
            if(scoreboard.getPlayer().getTeam().getName().equals(competitionConfiguration.getWaitlistTeamName())) {
                continue; // Waitlisted players don't go on the scoreboard
            }
            List<Object> row = new ArrayList<Object>();
            if(!teamsUsed.contains(scoreboard.getPlayer().getTeam())) {
                teamsUsed.add(scoreboard.getPlayer().getTeam());
                row.add(place++);
                row.add(scoreboard.getPlayer().getRsn());
                row.add(scoreboard.getTotalTileContribution());
                row.add(scoreboard.getPlayer().getTeam().getAbbreviation());
                rows.add(row);
            }
        }
        return rows;
    }
    
    private List<List<Object>> getMVPDetailsRows() {
        List<List<Object>> rows = new ArrayList<List<Object>>();
        
        // Add title row
        List<Object> titleRow = new ArrayList<Object>();
        titleRow.add("place");
        titleRow.add("player: Tile Contribution");
        titleRow.add("points: Tile Contribution");
        titleRow.add("team: Tile Contribution");
        titleRow.add("player: Luckiest Player");
        titleRow.add("points: Luckiest Player");
        titleRow.add("team: Luckiest Player");
        titleRow.add("player: Skilling Tiles");
        titleRow.add("points: Skilling Tiles");
        titleRow.add("team: Skilling Tiles");
        titleRow.add("player: PVM Tiles");
        titleRow.add("points: PVM Tiles");
        titleRow.add("team: PVM Tiles");
        titleRow.add("player: Other Tiles");
        titleRow.add("points: Other Tiles");
        titleRow.add("team: Other Tiles");
        rows.add(titleRow);
        
        // Add leaderboard
        int place = 1;
        for(PlayerScoreboard scoreboard : playerScoreboardRepository.findByOrderByTotalTileContributionDesc()) {
            if(scoreboard.getPlayer().getTeam().getName().equals(competitionConfiguration.getWaitlistTeamName())) {
                continue; // Waitlisted players don't go on the scoreboard
            }
            List<Object> row = new ArrayList<Object>();
            row.add(place++);
            row.add(scoreboard.getPlayer().getRsn());
            row.add(scoreboard.getTotalTileContribution());
            row.add(scoreboard.getPlayer().getTeam().getAbbreviation());
            rows.add(row);
        }
        int index = 1;
        for(PlayerScoreboard scoreboard : playerScoreboardRepository.findByOrderByCollectionLogPointsDesc()) {
            if(scoreboard.getPlayer().getTeam().getName().equals(competitionConfiguration.getWaitlistTeamName())) {
                continue; // Waitlisted players don't go on the scoreboard
            }
            List<Object> row = rows.get(index++);
            row.add(scoreboard.getPlayer().getRsn());
            row.add(scoreboard.getCollectionLogPoints());
            row.add(scoreboard.getPlayer().getTeam().getAbbreviation());
        }
        index = 1;
        for(PlayerScoreboard scoreboard : playerScoreboardRepository.findByOrderBySkillingTileContributionDesc()) {
            if(scoreboard.getPlayer().getTeam().getName().equals(competitionConfiguration.getWaitlistTeamName())) {
                continue; // Waitlisted players don't go on the scoreboard
            }
            List<Object> row = rows.get(index++);
            row.add(scoreboard.getPlayer().getRsn());
            row.add(scoreboard.getSkillingTileContribution());
            row.add(scoreboard.getPlayer().getTeam().getAbbreviation());
        }
        index = 1;
        for(PlayerScoreboard scoreboard : playerScoreboardRepository.findByOrderByPvmTileContributionDesc()) {
            if(scoreboard.getPlayer().getTeam().getName().equals(competitionConfiguration.getWaitlistTeamName())) {
                continue; // Waitlisted players don't go on the scoreboard
            }
            List<Object> row = rows.get(index++);
            row.add(scoreboard.getPlayer().getRsn());
            row.add(scoreboard.getPvmTileContribution());
            row.add(scoreboard.getPlayer().getTeam().getAbbreviation());
        }
        index = 1;
        for(PlayerScoreboard scoreboard : playerScoreboardRepository.findByOrderByOtherTileContributionDesc()) {
            if(scoreboard.getPlayer().getTeam().getName().equals(competitionConfiguration.getWaitlistTeamName())) {
                continue; // Waitlisted players don't go on the scoreboard
            }
            List<Object> row = rows.get(index++);
            row.add(scoreboard.getPlayer().getRsn());
            row.add(scoreboard.getOtherTileContribution());
            row.add(scoreboard.getPlayer().getTeam().getAbbreviation());
        }
        return rows;
    }
    
    private List<List<Object>> getMVPExtrasRows() {
        List<List<Object>> rows = new ArrayList<List<Object>>();
        
        // Add title row
        List<Object> titleRow = new ArrayList<Object>();
        titleRow.add("name: Pets Counter");
        titleRow.add("value: Pets Counter");
        titleRow.add("name: Mega Counter");
        titleRow.add("value: Mega Counter");
        rows.add(titleRow);
        
        // Add leaderboard
        Map<String, Integer> petCounts = new HashMap<String, Integer>();
        Map<String, Integer> megaCounts = new HashMap<String, Integer>();
        List<String> megarareNames = Arrays.asList("Twisted bow", "Scythe of vitur (uncharged)", "Tumeken's shadow (uncharged)");
        for(String name : megarareNames) {
            megaCounts.put(name, 0);
        }
        for(CollectionLogCompletion completion : collectionLogCompletionRepository.findAll()) {
            if(completion.getPlayer().getTeam().getName().equals(competitionConfiguration.getWaitlistTeamName())) {
                continue; // Clog completions from waitlisted players don't count
            }
            String itemName = completion.getItem().getName();
            if(completion.getItem().getType() == CollectionLogItem.Type.PET) {
                if(petCounts.get(itemName) != null) {
                    petCounts.put(itemName, petCounts.get(itemName) + 1);
                } else {
                    petCounts.put(itemName,  1);
                }
            }
            if(megarareNames.contains(itemName)) {
                megaCounts.put(itemName, megaCounts.get(itemName) + 1);
            }
        }
        List<String> itemNames = new ArrayList<String>();
        itemNames.addAll(petCounts.keySet());
        itemNames.sort((i1, i2) -> petCounts.get(i2) - petCounts.get(i1)); // Descending
        for(String itemName : itemNames) {
            List<Object> row = new ArrayList<Object>();
            row.add(itemName);
            row.add(petCounts.get(itemName));
            rows.add(row);
        }
        while(rows.size() < 4) { // Account for cases where fewer than 3 pets have been submitted, so we don't get index errors
            List<Object> row = new ArrayList<Object>();
            row.add("");
            row.add("");
            rows.add(row);
        }
        rows.get(1).add("Twisted Bow");
        rows.get(1).add(megaCounts.get("Twisted bow"));
        rows.get(2).add("Scythe of Vitur");
        rows.get(2).add(megaCounts.get("Scythe of vitur (uncharged)"));
        rows.get(3).add("Tumeken's Shadow");
        rows.get(3).add(megaCounts.get("Tumeken's shadow (uncharged)"));
        
        return rows;
    }
    
    private List<List<Object>> getBoardDetailsRows(Team team) {
        List<List<Object>> rows = new ArrayList<List<Object>>();
        int[] pointsPerTier = competitionConfiguration.getEventPointsPerTier();
        
        // Add title row
        List<Object> titleRow = new ArrayList<Object>();
        titleRow.add("tile");
        titleRow.add("tier");
        titleRow.add("pointsFromTile");
        titleRow.add("percentToNextTier");
        rows.add(titleRow);
        
        // Add detail rows
        for(Tile tile : tileRepository.findByOrderByIdAsc()) {
            TileProgress progress = team.getTileProgress(tile);
            List<Object> row = new ArrayList<Object>();
            row.add(tile.getName());
            row.add(progress.getTier());
            if(progress.getTier() == 0) {
                row.add(0);
            } else {
                int points = 0;
                int indexForPoints = progress.getTier() - 1;
                if(indexForPoints > pointsPerTier.length - 1) {
                    indexForPoints = pointsPerTier.length - 1;
                }
                for(int i = 0; i <= indexForPoints; i++) {
                    points += pointsPerTier[i];
                }
                row.add(points);
            }
            row.add(progress.getPercentageToNextTier());
            rows.add(row);
        }
        return rows;
    }
    
    private List<List<Object>> getRecordDetailsRows(Team team) {
        List<List<Object>> rows = new ArrayList<List<Object>>();
        
        List<Record> records = new ArrayList<Record>();
        for(Record record : recordRepository.findByOrderByIdAsc()) {
            records.add(record);
        }
        
        // Add title row
        List<Object> titleRow = new ArrayList<Object>();
        titleRow.add("place");
        for(Record record : records) {
            titleRow.add("player: " + record.getName());
            titleRow.add("value: " + record.getName());
        }
        rows.add(titleRow);
        
        // Add leaderboard
        List<Player> players = new ArrayList<Player>(team.getPlayers());
        for(int place = 1; place <= players.size(); place++) {
            List<Object> row = new ArrayList<Object>();
            row.add(place);
            for(Record record : records) {
                players.sort((p1, p2) -> { // Descending
                    if(p1.getRecordCompletion(record) == null) {
                        return 1;
                    }
                    if(p2.getRecordCompletion(record) == null) {
                        return -1;
                    }
                    return p2.getRecordCompletion(record).getValue() - p1.getRecordCompletion(record).getValue();
                });
                RecordCompletion completion = players.get(place - 1).getRecordCompletion(record);
                if(completion != null) {                    
                    row.add(completion.getPlayer().getRsn());
                    row.add(completion.getValue());
                } else {
                    row.add("");
                    row.add("");
                }
            }
            rows.add(row);
        }
        return rows;
    }
    
    private List<List<Object>> getChallengeDetailsRows(Team team) {
        List<List<Object>> rows = new ArrayList<List<Object>>();
        
        // Add title row
        List<Object> titleRow = new ArrayList<Object>();
        titleRow.add("challenge");
        titleRow.add("players");
        titleRow.add("time");
        rows.add(titleRow);
        
        // Add leaderboard
        if(!team.getName().equals(competitionConfiguration.getWaitlistTeamName())) { // Waitlist team doesn't have challenge leaderboard entries            
            for(Challenge challenge : challengeRepository.findByOrderByIdAsc()) {
                List<Object> row = new ArrayList<Object>();
                row.add(challenge.getName());
                if(team.getScoreboard().getChallengeLeaderboardEntry(challenge).getSeconds() < 0) {
                    row.add("");
                    row.add("");
                } else {
                    row.add(team.getScoreboard().getChallengeLeaderboardEntry(challenge).getPlayerNames());
                    row.add(team.getScoreboard().getChallengeLeaderboardEntry(challenge).getSeconds() / 86400.0); // Convert to sheets format - unit is one day
                }
                rows.add(row);
            }
        }
        
        return rows;
    }
    
    private List<List<Object>> getMVPRaceRows(Team team) {
        List<List<Object>> rows = new ArrayList<List<Object>>();
        
        // Add title row
        List<Object> titleRow = new ArrayList<Object>();
        titleRow.add("place");
        titleRow.add("player");
        titleRow.add("points");
        rows.add(titleRow);
        
        // Add leaderboard
        List<Player> players = new ArrayList<Player>(team.getPlayers());
        players.sort((p1, p2) -> { // Descending, with a 0 case to account for floating point math
            if(Math.abs(p2.getScoreboard().getTotalTileContribution() - p1.getScoreboard().getTotalTileContribution()) < 0.00000001) {
                return 0;
            } else if(p2.getScoreboard().getTotalTileContribution() - p1.getScoreboard().getTotalTileContribution() < 0) {
                return -1;
            } else {
                return 1;
            }
           }
        );
        for(int place = 1; place <= players.size(); place++) {
            List<Object> row = new ArrayList<Object>();
            row.add(place);
            row.add(players.get(place - 1).getRsn());
            row.add(players.get(place - 1).getScoreboard().getTotalTileContribution());
            rows.add(row);
        }
        return rows;
    }
    
    private List<List<Object>> getRelayComponentsRows(Team team) {
        List<List<Object>> rows = new ArrayList<List<Object>>();
        
        List<ChallengeRelayComponent> components = new ArrayList<ChallengeRelayComponent>();
        for(ChallengeRelayComponent component : challengeRelayComponentRepository.findByOrderByIdAsc()) {
            components.add(component);
        }
        
        // Add title row
        List<Object> titleRow = new ArrayList<Object>();
        titleRow.add("place");
        for(ChallengeRelayComponent component : components) {
            titleRow.add("player: " + component.getName());
            titleRow.add("time: " + component.getName());
        }
        rows.add(titleRow);
        
        // Add leaderboard
        List<Player> players = new ArrayList<Player>(team.getPlayers());
        for(int place = 1; place <= players.size(); place++) {
            List<Object> row = new ArrayList<Object>();
            row.add(place);
            for(ChallengeRelayComponent component : components) {
                players.sort((p1, p2) -> { // Descending, with a 0 case to account for floating point math
                    PlayerChallengeCompletion c1 = p1.getBestPlayerChallengeCompletion(component.getChallenge(), component);
                    PlayerChallengeCompletion c2 = p2.getBestPlayerChallengeCompletion(component.getChallenge(), component);
                    if(c1 == null && c2 == null) {
                        return 0;
                    }
                    if(c1 == null) {
                        return 1;
                    }
                    if(c2 == null) {
                        return -1;
                    }
                    if(Math.abs(c1.getSeconds() - c2.getSeconds()) < 0.00000001) {
                        return 0;
                    }
                    if(c1.getSeconds() - c2.getSeconds() < 0) {
                        return -1;
                    } else {
                        return 1;
                    }
                });
                PlayerChallengeCompletion completion = players.get(place - 1).getBestPlayerChallengeCompletion(component.getChallenge(), component);
                if(completion != null) {                    
                    row.add(completion.getPlayer().getRsn());
                    row.add(completion.getSeconds() / 86400.0); // Convert to sheets format - unit is one day
                } else {
                    row.add("");
                    row.add("");
                }
            }
            rows.add(row);
        }
        return rows;
    }
    
    private List<List<Object>> getProgressOverviewRows(Team team) {
        List<List<Object>> rows = new ArrayList<List<Object>>();
        
        // Add title row
        List<Object> titleRow = new ArrayList<Object>();
        titleRow.add("tile");
        titleRow.add("points");
        titleRow.add("goal");
        rows.add(titleRow);
        
        // Add detail rows
        for(Tile tile : tileRepository.findByOrderByIdAsc()) {
            TileProgress progress = team.getTileProgress(tile);
            List<Object> row = new ArrayList<Object>();
            row.add(tile.getName());
            row.add(progress.getPoints());
            row.add(tile.getPointsPerTier() * (progress.getTier() + 1));
            rows.add(row);
        }
        return rows;
    }
    
    private List<List<Object>> getProgressRows(Team team) {
        List<List<Object>> rows = new ArrayList<List<Object>>();
        List<ContributionMethod> methods = new ArrayList<ContributionMethod>();
        for(ContributionMethod method : contributionMethodRepository.findByOrderByIdAsc()) {
            methods.add(method);
        }
        
        // Add title row
        List<Object> titleRow = new ArrayList<Object>();
        titleRow.add("player");
        for(ContributionMethod method : methods) {
            titleRow.add("contribution: " + method.getName());
        }
        rows.add(titleRow);
        
        // Add detail rows
        List<Player> players = new ArrayList<Player>(team.getPlayers());
        players.sort((p1, p2) -> p1.getId() - p2.getId()); // Ascending
        for(Player player : players) {
            List<Object> row = new ArrayList<Object>();
            row.add(player.getRsn());
            for(ContributionMethod method : methods) {
                row.add(player.getContribution(method).getUnitsContributed());
            }
            rows.add(row);
        }
        return rows;
    }
    
    private List<List<Object>> getClogItems(Team team) {
        List<List<Object>> rows = new ArrayList<List<Object>>();
        List<CollectionLogItem> items = new ArrayList<CollectionLogItem>();
        for(CollectionLogItem item : collectionLogItemRepository.findByOrderByIdAsc()) {
            items.add(item);
        }
        
        // Add title row
        List<Object> titleRow = new ArrayList<Object>();
        titleRow.add("item");
        titleRow.add("collected");
        titleRow.add("points");
        rows.add(titleRow);
        
        // Add detail rows
        for(CollectionLogItem item : items) {
            if(item.getType() == CollectionLogItem.Type.NORMAL) {        
                List<Object> row = new ArrayList<Object>();
                row.add(item.getName());
                boolean hasItem = false;
                for(CollectionLogCompletion completion : team.getCollectionLogCompletions()) {
                    if(completion.getItem().equals(item)) {
                        hasItem = true;
                        break;
                    }
                }
                row.add(hasItem);
                row.add(item.getPoints());
                rows.add(row);
            }
        }
        return rows;
    }
    
    private List<List<Object>> getPetsAndJars(Team team) {
        List<List<Object>> rows = new ArrayList<List<Object>>();
        
        // Add title row
        List<Object> titleRow = new ArrayList<Object>();
        titleRow.add("type");
        titleRow.add("items");
        titleRow.add("points");
        rows.add(titleRow);
        
        // Sum up points from pets - Dupes count here
        int pointsFromPets = 0;
        int petPointIndex = 0;
        int petsCollected = 0;
        for(Player player : team.getPlayers()) {
            for(CollectionLogCompletion completion : player.getCollectionLogCompletions()) {
                if(completion.getItem().getType() == CollectionLogItem.Type.PET) {
                    petsCollected++;
                    pointsFromPets += competitionConfiguration.getPetPointValues()[petPointIndex];
                    if(petPointIndex + 1 < competitionConfiguration.getPetPointValues().length) {
                        petPointIndex++;
                    }
                }
            }
        }
        
        // Sum up points from jars - Dupes count here
        int pointsFromJars = 0;
        int jarPointIndex = 0;
        int jarsCollected = 0;
        for(Player player : team.getPlayers()) {
            for(CollectionLogCompletion completion : player.getCollectionLogCompletions()) {
                if(completion.getItem().getType() == CollectionLogItem.Type.JAR) {
                    jarsCollected++;
                    pointsFromJars += competitionConfiguration.getJarPointValues()[jarPointIndex];
                    if(jarPointIndex + 1 < competitionConfiguration.getJarPointValues().length) {
                        jarPointIndex++;
                    }
                }
            }
        }
        
        // Add pet/jar rows
        List<Object> petRow = new ArrayList<Object>();
        petRow.add("pets");
        petRow.add(petsCollected);
        petRow.add(pointsFromPets);
        List<Object> jarRow = new ArrayList<Object>();
        jarRow.add("jars");
        jarRow.add(jarsCollected);
        jarRow.add(pointsFromJars);
        rows.add(petRow);
        rows.add(jarRow);
        
        return rows;
    }
    
    private List<List<Object>> generateBlankRows(int x, int y) {
        List<List<Object>> rows = new ArrayList<List<Object>>();
        for(int i = 0; i < y; i++) {
            List<Object> row = new ArrayList<Object>();
            for(int j = 0; j < x; j++) {
                row.add("");
            }
            rows.add(row);
        }
        return rows;
    }

}
