package org.uimshowdown.bingo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.uimshowdown.bingo.TestUtils;
import org.uimshowdown.bingo.configuration.CompetitionConfiguration;
import org.uimshowdown.bingo.models.Challenge;
import org.uimshowdown.bingo.models.ChallengeRelayComponent;
import org.uimshowdown.bingo.models.CollectionLogItem;
import org.uimshowdown.bingo.models.ContributionMethod;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.Record;
import org.uimshowdown.bingo.models.RecordHandicap;
import org.uimshowdown.bingo.models.Team;
import org.uimshowdown.bingo.models.Tile;
import org.uimshowdown.bingo.repositories.ChallengeCompletionRepository;
import org.uimshowdown.bingo.repositories.ChallengeRelayComponentRepository;
import org.uimshowdown.bingo.repositories.ChallengeRepository;
import org.uimshowdown.bingo.repositories.CollectionLogCompletionRepository;
import org.uimshowdown.bingo.repositories.CollectionLogItemRepository;
import org.uimshowdown.bingo.repositories.ContributionMethodRepository;
import org.uimshowdown.bingo.repositories.ContributionRepository;
import org.uimshowdown.bingo.repositories.ItemOptionRepository;
import org.uimshowdown.bingo.repositories.PlayerChallengeCompletionRepository;
import org.uimshowdown.bingo.repositories.PlayerRepository;
import org.uimshowdown.bingo.repositories.PlayerScoreboardRepository;
import org.uimshowdown.bingo.repositories.RecordCompletionRepository;
import org.uimshowdown.bingo.repositories.RecordHandicapRepository;
import org.uimshowdown.bingo.repositories.RecordRepository;
import org.uimshowdown.bingo.repositories.SubmissionRepository;
import org.uimshowdown.bingo.repositories.TeamRepository;
import org.uimshowdown.bingo.repositories.TeamScoreboardRepository;
import org.uimshowdown.bingo.repositories.TileProgressRepository;
import org.uimshowdown.bingo.repositories.TileRepository;

@SpringBootTest
@ActiveProfiles("test")
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
    @Autowired TeamRepository teamRepository;
    @Autowired TeamScoreboardRepository teamScoreboardRepository;
    @Autowired TileProgressRepository tileProgressRepository;
    @Autowired TileRepository tileRepository;
    @Autowired ItemOptionRepository itemOptionRepository;
    
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
    public void addPlayer() throws Exception {
        Team team = new Team();
        team.setName("test-team");
        team.setAbbreviation("TT");
        team.setColor("FFFFFF");
        List<String> captainRsns = new ArrayList<String>();
        captainRsns.add("test-rsn1");
        team.setCaptainRsns(captainRsns);
        
        teamRepository.save(team);
        
        eventDataInitializationService.addPlayer("test-discordname1", "test-rsn1", "test-team");
        eventDataInitializationService.addPlayer("test-discordname2", "test-rsn2", "test-team");
        
        Player player1 = playerRepository.findByRsn("test-rsn1").get();
        assertEquals("test-discordname1", player1.getDiscordName());
        assertEquals("test-rsn1", player1.getRsn());
        assertEquals(team, player1.getTeam());
        assertTrue(player1.isCaptain());
        Player player2 = playerRepository.findByRsn("test-rsn2").get();
        assertEquals("test-discordname2", player2.getDiscordName());
        assertEquals("test-rsn2", player2.getRsn());
        assertEquals(team, player2.getTeam());
        assertFalse(player2.isCaptain());
        
        team = teamRepository.findByName("test-team").get();
        HashSet<Player> expectedPlayers = new HashSet<Player>();
        expectedPlayers.add(player1);
        expectedPlayers.add(player2);
        HashSet<Player> expectedCaptains = new HashSet<Player>();
        expectedCaptains.add(player1);
        assertEquals(expectedPlayers, team.getPlayers());
    }
    
    @Test
    @Transactional
    public void compInit() throws Exception {
        eventDataInitializationService.initializeCompetition();
        
        // Validate tiles/methods
        Tile testTile1 = tileRepository.findByName("Test Tile 1").get();
        Tile testTile2 = tileRepository.findByName("Test Tile 2").get();
        Tile testTile3 = tileRepository.findByName("Test Tile 3").get();
        ContributionMethod pvmMethod = contributionMethodRepository.findByName("Chambers of Xeric").get();
        ContributionMethod skillingMethod = contributionMethodRepository.findByName("Cooking").get();
        ContributionMethod otherMethod = contributionMethodRepository.findByName("Master Clues").get();
        ContributionMethod submissionMethod = contributionMethodRepository.findByName("Basilisk Knights").get();
        assertNotNull(testTile1);
        assertNotNull(testTile2);
        assertNotNull(testTile3);
        assertNotNull(pvmMethod);
        assertNotNull(skillingMethod);
        assertNotNull(otherMethod);
        assertNotNull(submissionMethod);
        
        // Validate tile/method properties are populated correctly
        assertEquals("Test Tile 1", testTile1.getName());
        assertEquals("TT1", testTile1.getAbbreviation());
        assertTrue(testTile1.getContributionMethods().contains(pvmMethod));
        assertTrue(testTile1.getContributionMethods().contains(skillingMethod));
        assertTrue(testTile1.getContributionMethods().contains(otherMethod));
        assertTrue(testTile1.getContributionMethods().contains(submissionMethod));
        assertEquals("Chambers of Xeric", pvmMethod.getName());
        assertEquals(ContributionMethod.Type.TEMPLE_KC, pvmMethod.getContributionMethodType());
        assertEquals(ContributionMethod.Category.PVM, pvmMethod.getContributionMethodCategory());
        assertTrue(Math.abs(pvmMethod.getEhtRate() - 7.5) < 0.0001); // ehtRate == 7.5
        assertEquals("Chambers of Xeric", pvmMethod.getTempleId());
        assertEquals("Cooking", skillingMethod.getName());
        assertEquals(ContributionMethod.Type.TEMPLE_XP, skillingMethod.getContributionMethodType());
        assertEquals(ContributionMethod.Category.SKILLING, skillingMethod.getContributionMethodCategory());
        assertTrue(Math.abs(skillingMethod.getEhtRate() - 400000) < 0.0001); // ehtRate == 63000
        assertEquals("Cooking", skillingMethod.getTempleId());
        assertEquals("Master Clues", otherMethod.getName());
        assertEquals(ContributionMethod.Type.TEMPLE_KC, otherMethod.getContributionMethodType());
        assertEquals(ContributionMethod.Category.OTHER, otherMethod.getContributionMethodCategory());
        assertTrue(Math.abs(otherMethod.getEhtRate() - 2.4) < 0.0001); // ehtRate == 2.4
        assertEquals("Clue_master", otherMethod.getTempleId());
        assertEquals("Basilisk Knights", submissionMethod.getName());
        assertEquals(ContributionMethod.Type.SUBMISSION_KC, submissionMethod.getContributionMethodType());
        assertEquals(ContributionMethod.Category.PVM, submissionMethod.getContributionMethodCategory());
        assertTrue(Math.abs(submissionMethod.getEhtRate() - 93) < 0.0001); // ehtRate == 93
        assertEquals(null, submissionMethod.getTempleId());
        
        // Validate tile/method points
        int ehtPerTier = competitionConfiguration.getEhtPerTier();
        int tilePointsPerTier = testTile1.getPointsPerTier();
        String tilePointsString = String.valueOf(tilePointsPerTier);
        int tileSigFigs = tilePointsString.length() - (int) tilePointsString.chars().filter(ch -> ch == '0').count();
        assertTrue(tileSigFigs <= competitionConfiguration.getSigFigsForTilePointsPerTier());
        for(ContributionMethod method : testTile1.getContributionMethods()) {
            double trueContributionPerTier = (double) ehtPerTier * method.getEhtRate();
            double roundedContributionPerTier = (double) tilePointsPerTier / (double) method.getTilePointsPerContribution();
            double roundingError = Math.abs(trueContributionPerTier - roundedContributionPerTier) / trueContributionPerTier;
            assertTrue(roundingError < competitionConfiguration.getMaxRoundingErrorForContributionPoints());
            String methodPointsString = String.valueOf(method.getTilePointsPerContribution());
            int methodSigFigs = methodPointsString.length() - (int) methodPointsString.chars().filter(ch -> ch == '0').count();
            assertTrue(methodSigFigs <= competitionConfiguration.getSigFigsForContributionPoints());
        }
        
        // Validate challenges
        Challenge speedrunChallenge = challengeRepository.findByName("Barbarian Assault").get();
        Challenge relayChallenge = challengeRepository.findByName("Desert Treasure 2 Relay").get();
        ChallengeRelayComponent component1 = challengeRelayComponentRepository.findByName("The Whisperer").get();
        ChallengeRelayComponent component2 = challengeRelayComponentRepository.findByName("The Leviathan").get();
        ChallengeRelayComponent component3 = challengeRelayComponentRepository.findByName("Vardorvis").get();
        ChallengeRelayComponent component4 = challengeRelayComponentRepository.findByName("Duke Sucellus").get();
        assertNotNull(speedrunChallenge);
        assertNotNull(relayChallenge);
        assertNotNull(component1);
        assertNotNull(component2);
        assertNotNull(component3);
        assertNotNull(component4);
        
        // Validate challenge/relay component properties are populated correctly
        assertEquals("Barbarian Assault", speedrunChallenge.getName());
        assertEquals(5, speedrunChallenge.getTeamSize());
        assertEquals("Complete a full game of Barbarian Assault as quickly as possible! All players must be on your team.", speedrunChallenge.getDescription());
        assertEquals(Challenge.Type.SPEEDRUN, speedrunChallenge.getType());
        assertEquals("Desert Treasure 2 Relay", relayChallenge.getName());
        assertEquals(4, relayChallenge.getTeamSize());
        assertEquals("Kill each Desert Treasure 2 boss as quickly as possible! Each boss must be killed by a different player.", relayChallenge.getDescription());
        assertEquals(Challenge.Type.RELAY, relayChallenge.getType());
        assertEquals("The Whisperer", component1.getName());
        assertEquals(relayChallenge, component1.getChallenge());
        assertEquals("The Leviathan", component2.getName());
        assertEquals(relayChallenge, component2.getChallenge());
        assertEquals("Vardorvis", component3.getName());
        assertEquals(relayChallenge, component3.getChallenge());
        assertEquals("Duke Sucellus", component4.getName());
        assertEquals(relayChallenge, component4.getChallenge());
        assertTrue(relayChallenge.getRelayComponents().contains(component1));
        assertTrue(relayChallenge.getRelayComponents().contains(component2));
        assertTrue(relayChallenge.getRelayComponents().contains(component3));
        assertTrue(relayChallenge.getRelayComponents().contains(component4));
        assertTrue(speedrunChallenge.getRelayComponents().isEmpty());
        
        // Validate records
        Record recordWithHandicaps = recordRepository.findBySkill(Player.Skill.WOODCUTTING).get();
        RecordHandicap handicap1 = recordHandicapRepository.findByName("Dragon axe").get();
        RecordHandicap handicap2 = recordHandicapRepository.findByName("Dragon 2h axe").get();
        assertNotNull(recordWithHandicaps);
        assertNotNull(handicap1);
        assertNotNull(handicap2);
        
        // Validate record/handicap properties are populated correctly
        assertEquals(Player.Skill.WOODCUTTING, recordWithHandicaps.getSkill());
        assertEquals("Get as much woodcutting XP as you can in 6 hours!", recordWithHandicaps.getDescription());
        assertEquals("Dragon axe", handicap1.getName());
        assertTrue(Math.abs(handicap1.getMultiplier() - 1.13) < 0.0001); // multiplier == 1.13
        assertEquals(recordWithHandicaps, handicap1.getRecord());
        assertEquals("Dragon 2h axe", handicap2.getName());
        assertTrue(Math.abs(handicap2.getMultiplier() - 1.13) < 0.0001); // multiplier == 1.13
        assertEquals(recordWithHandicaps, handicap2.getRecord());
        assertTrue(recordWithHandicaps.getHandicaps().contains(handicap1));
        assertTrue(recordWithHandicaps.getHandicaps().contains(handicap2));
        
        // Validate collection log Items
        CollectionLogItem item1 = collectionLogItemRepository.findByName("Any Blessed D'hide Coif").get();
        CollectionLogItem item2 = collectionLogItemRepository.findByName("Holy elixir").get();
        CollectionLogItem item3 = collectionLogItemRepository.findByName("Jar of chemicals").get();
        CollectionLogItem item4 = collectionLogItemRepository.findByName("Phoenix").get();
        
        assertNotNull(item1);
        assertNotNull(item2);
        assertNotNull(item3);
        assertNotNull(item4);
        
        // Validate item/option properties are populated correctly
        assertEquals("Any Blessed D'hide Coif", item1.getName());
        assertEquals(2, item1.getPoints());
        assertEquals(CollectionLogItem.Type.NORMAL, item1.getType());
        Set<String> expectedItemNames1 = new HashSet<String>();
        expectedItemNames1.add("Ancient coif");
        expectedItemNames1.add("Armadyl coif");
        assertEquals(expectedItemNames1, item1.getItemOptionNames());
        
        assertEquals("Holy elixir", item2.getName());
        assertEquals(1, item2.getPoints());
        assertEquals(CollectionLogItem.Type.NORMAL, item2.getType());
        
        assertEquals("Jar of chemicals", item3.getName());
        assertEquals(0, item3.getPoints());
        assertEquals(CollectionLogItem.Type.JAR, item3.getType());
        
        assertEquals("Phoenix", item4.getName());
        assertEquals(0, item4.getPoints());
        assertEquals(CollectionLogItem.Type.PET, item4.getType());
    }

}
