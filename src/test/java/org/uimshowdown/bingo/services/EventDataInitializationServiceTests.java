package org.uimshowdown.bingo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.uimshowdown.bingo.TestUtils;
import org.uimshowdown.bingo.configuration.CompetitionConfiguration;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.Team;
import org.uimshowdown.bingo.repositories.ChallengeCompletionRepository;
import org.uimshowdown.bingo.repositories.ChallengeRelayComponentRepository;
import org.uimshowdown.bingo.repositories.ChallengeRepository;
import org.uimshowdown.bingo.repositories.CollectionLogCompletionRepository;
import org.uimshowdown.bingo.repositories.CollectionLogGroupRepository;
import org.uimshowdown.bingo.repositories.CollectionLogItemRepository;
import org.uimshowdown.bingo.repositories.ContributionMethodRepository;
import org.uimshowdown.bingo.repositories.ContributionRepository;
import org.uimshowdown.bingo.repositories.PlayerChallengeCompletionRepository;
import org.uimshowdown.bingo.repositories.PlayerRepository;
import org.uimshowdown.bingo.repositories.PlayerScoreboardRepository;
import org.uimshowdown.bingo.repositories.RecordCompletionRepository;
import org.uimshowdown.bingo.repositories.RecordHandicapRepository;
import org.uimshowdown.bingo.repositories.RecordRepository;
import org.uimshowdown.bingo.repositories.SharedTestVariables;
import org.uimshowdown.bingo.repositories.SubmissionRepository;
import org.uimshowdown.bingo.repositories.SubmissionScreenshotUrlRepository;
import org.uimshowdown.bingo.repositories.TeamRepository;
import org.uimshowdown.bingo.repositories.TeamScoreboardRepository;
import org.uimshowdown.bingo.repositories.TileProgressRepository;
import org.uimshowdown.bingo.repositories.TileRepository;
import org.uimshowdown.bingo.services.EventDataInitializationService;

@SpringBootTest
public class EventDataInitializationServiceTests {
    
    @Autowired
    private TestUtils testUtils;
    
    @Autowired
    private CompetitionConfiguration competitionConfiguration;
    
    @Autowired
    private EventDataInitializationService eventDataInitializationService;
    
    @Autowired ChallengeCompletionRepository challengeCompletionRepository;
    @Autowired ChallengeRelayComponentRepository challengeRelayComponentRepository;
    @Autowired ChallengeRepository challengeRepository;
    @Autowired CollectionLogCompletionRepository collectionLogCompletionRepository;
    @Autowired CollectionLogGroupRepository collectionLogGroupRepository;
    @Autowired CollectionLogItemRepository collectionLogItemRepository;
    @Autowired ContributionMethodRepository contributionMethodRepository;
    @Autowired ContributionRepository contributionRepository;
    @Autowired PlayerChallengeCompletionRepository playerChallengeCompletionRepository;
    @Autowired PlayerRepository playerRepository;
    @Autowired PlayerScoreboardRepository playerScoreboardRepository;
    @Autowired RecordCompletionRepository recordCompletionRepository;
    @Autowired RecordHandicapRepository recordHandicapRepository;
    @Autowired RecordRepository recordRepository;
    @Autowired SubmissionRepository submissionRepository;
    @Autowired SubmissionScreenshotUrlRepository submissionScreenshotUrlRepository;
    @Autowired TeamRepository teamRepository;
    @Autowired TeamScoreboardRepository teamScoreboardRepository;
    @Autowired TileProgressRepository tileProgressRepository;
    @Autowired TileRepository tileRepository;
    
    @BeforeEach
    public void setUp() {
        testUtils.resetDB();
    }
    
    @AfterEach
    public void tearDown() {
        testUtils.resetDB();
    }
    
    @Test
    @Transactional
    public void addTeamAndPlayers() throws Exception {
        eventDataInitializationService.addTeam("test-team", "TT", "FFFFFF");
        
        Team team = teamRepository.findByName("test-team").get();
        assertEquals("test-team", team.getName());
        assertEquals("TT", team.getAbbreviation());
        assertEquals("FFFFFF", team.getColor());
        assertEquals(null, team.getPlayers());
        assertEquals(null, team.getCaptains());
        
        eventDataInitializationService.addPlayer("test-discordname1", "test-rsn1", true, "test-team");
        eventDataInitializationService.addPlayer("test-discordname2", "test-rsn2", false, "test-team");
        
        Player player1 = playerRepository.findByRsn("test-rsn1").get();
        assertEquals("test-discordname1", player1.getDiscordName());
        assertEquals("test-rsn1", player1.getRsn());
        assertEquals(team, player1.getTeam());
        Player player2 = playerRepository.findByRsn("test-rsn2").get();
        assertEquals("test-discordname2", player2.getDiscordName());
        assertEquals("test-rsn2", player2.getRsn());
        assertEquals(team, player2.getTeam());
        
        team = teamRepository.findByName("test-team").get();
        HashSet<Player> expectedPlayers = new HashSet<Player>();
        expectedPlayers.add(player1);
        expectedPlayers.add(player2);
        HashSet<Player> expectedCaptains = new HashSet<Player>();
        expectedCaptains.add(player1);
        assertEquals(expectedPlayers, team.getPlayers());
        assertEquals(expectedCaptains, team.getCaptains());
    }
    
    

}
