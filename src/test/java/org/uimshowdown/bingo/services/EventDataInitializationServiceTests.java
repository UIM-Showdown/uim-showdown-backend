package org.uimshowdown.bingo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
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
import org.uimshowdown.bingo.models.CollectionLogChecklistGroup;
import org.uimshowdown.bingo.models.CollectionLogCounterGroup;
import org.uimshowdown.bingo.models.CollectionLogGroup;
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
import org.uimshowdown.bingo.repositories.CollectionLogGroupRepository;
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
import org.uimshowdown.bingo.repositories.SubmissionScreenshotUrlRepository;
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
    
    @Test
    @Transactional
    public void compInit() throws Exception {
        eventDataInitializationService.initializeCompetition();
        
        // Validate tiles/methods
        Tile testTile1 = tileRepository.findByName("Test Tile 1").get();
        Tile testTile2 = tileRepository.findByName("Test Tile 2").get();
        Tile testTile3 = tileRepository.findByName("Test Tile 3").get();
        ContributionMethod pvmMethod = contributionMethodRepository.findByName("Chambers of Xeric").get();
        ContributionMethod skillingMethod = contributionMethodRepository.findByName("Slayer").get();
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
        assertEquals(ContributionMethod.Type.KC, pvmMethod.getContributionMethodType());
        assertEquals(ContributionMethod.Category.PVM, pvmMethod.getContributionMethodCategory());
        assertTrue(Math.abs(pvmMethod.getEhtRate() - 7.5) < 0.0001); // ehtRate == 7.5
        assertEquals("Chambers of Xeric", pvmMethod.getTempleId());
        assertEquals("Slayer", skillingMethod.getName());
        assertEquals(ContributionMethod.Type.XP, skillingMethod.getContributionMethodType());
        assertEquals(ContributionMethod.Category.SKILLING, skillingMethod.getContributionMethodCategory());
        assertTrue(Math.abs(skillingMethod.getEhtRate() - 63000) < 0.0001); // ehtRate == 63000
        assertEquals("Slayer", skillingMethod.getTempleId());
        assertEquals("Master Clues", otherMethod.getName());
        assertEquals(ContributionMethod.Type.KC, otherMethod.getContributionMethodType());
        assertEquals(ContributionMethod.Category.OTHER, otherMethod.getContributionMethodCategory());
        assertTrue(Math.abs(otherMethod.getEhtRate() - 2.4) < 0.0001); // ehtRate == 2.4
        assertEquals("Clue_master", otherMethod.getTempleId());
        assertEquals("Basilisk Knights", submissionMethod.getName());
        assertEquals(ContributionMethod.Type.SUBMISSION, submissionMethod.getContributionMethodType());
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
        assertNull(speedrunChallenge.getRelayComponents());
        
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
        
        // Validate collection log groups
        CollectionLogGroup checklistGroupWithOptions = collectionLogGroupRepository.findByName("Agnostic Apparel").get();
        CollectionLogGroup checklistGroupWithoutOptions = collectionLogGroupRepository.findByName("Beastly Blade").get();
        CollectionLogGroup counterGroup = collectionLogGroupRepository.findByName("Not Just For Butterflies").get();
        CollectionLogItem item1 = collectionLogItemRepository.findByName("Any Blessed D'hide Coif").get();
        CollectionLogItem item2 = collectionLogItemRepository.findByName("Any Blessed D'hide Body").get();
        CollectionLogItem item3 = collectionLogItemRepository.findByName("Holy elixir").get();
        CollectionLogItem item4 = collectionLogItemRepository.findByName("Arcane sigil").get();
        CollectionLogItem item5 = collectionLogItemRepository.findByName("Elysian sigil").get();
        CollectionLogItem item6 = collectionLogItemRepository.findByName("Jar of chemicals").get();
        CollectionLogItem item7 = collectionLogItemRepository.findByName("Jar of darkness").get();
        CollectionLogItem item8 = collectionLogItemRepository.findByName("Jar of decay").get();
        assertNotNull(checklistGroupWithOptions);
        assertNotNull(checklistGroupWithoutOptions);
        assertNotNull(counterGroup);
        assertNotNull(item1);
        assertNotNull(item2);
        assertNotNull(item3);
        assertNotNull(item4);
        assertNotNull(item5);
        assertNotNull(item6);
        assertNotNull(item7);
        assertNotNull(item8);
        
        // Validate group/item/option properties are populated correctly
        assertEquals("Agnostic Apparel", checklistGroupWithOptions.getName());
        assertEquals(CollectionLogGroup.Type.CHECKLIST, checklistGroupWithOptions.getType());
        int[] bonusPointThresholds1 = ((CollectionLogChecklistGroup) checklistGroupWithOptions).getBonusPointThresholds();
        assertEquals(2, bonusPointThresholds1.length);
        assertEquals(2, bonusPointThresholds1[0]);
        assertEquals(3, bonusPointThresholds1[1]);
        assertEquals("Bonus point at: 2 items, 3 items", checklistGroupWithOptions.getDescription());
        assertTrue(checklistGroupWithOptions.getItems().contains(item1));
        assertTrue(checklistGroupWithOptions.getItems().contains(item2));
        assertEquals("Any Blessed D'hide Coif", item1.getName());
        assertEquals(2, item1.getPoints());
        assertEquals(checklistGroupWithOptions, item1.getGroup());
        Set<String> expectedItemNames1 = new HashSet<String>();
        expectedItemNames1.add("Ancient coif");
        expectedItemNames1.add("Armadyl coif");
        assertEquals(expectedItemNames1, item1.getItemOptionNames());
        assertEquals("Any Blessed D'hide Body", item2.getName());
        assertEquals(2, item2.getPoints());
        assertEquals(checklistGroupWithOptions, item2.getGroup());
        Set<String> expectedItemNames2 = new HashSet<String>();
        expectedItemNames2.add("Ancient d'hide body");
        expectedItemNames2.add("Armadyl d'hide body");
        assertEquals(expectedItemNames2, item2.getItemOptionNames());
        assertEquals("Beastly Blade", checklistGroupWithoutOptions.getName());
        assertEquals(CollectionLogGroup.Type.CHECKLIST, checklistGroupWithoutOptions.getType());
        int[] bonusPointThresholds2 = ((CollectionLogChecklistGroup) checklistGroupWithoutOptions).getBonusPointThresholds();
        assertEquals(2, bonusPointThresholds2.length);
        assertEquals(2, bonusPointThresholds2[0]);
        assertEquals(3, bonusPointThresholds2[1]);
        assertEquals("Bonus point at: 2 items, 3 items", checklistGroupWithoutOptions.getDescription());
        assertTrue(checklistGroupWithoutOptions.getItems().contains(item3));
        assertTrue(checklistGroupWithoutOptions.getItems().contains(item4));
        assertTrue(checklistGroupWithoutOptions.getItems().contains(item5));
        assertEquals("Holy elixir", item3.getName());
        assertEquals(1, item3.getPoints());
        assertEquals(checklistGroupWithoutOptions, item3.getGroup());
        assertEquals(new HashSet<String>(), item3.getItemOptionNames());
        assertEquals("Arcane sigil", item4.getName());
        assertEquals(2, item4.getPoints());
        assertEquals(checklistGroupWithoutOptions, item4.getGroup());
        assertEquals(new HashSet<String>(), item4.getItemOptionNames());
        assertEquals("Elysian sigil", item5.getName());
        assertEquals(3, item5.getPoints());
        assertEquals(checklistGroupWithoutOptions, item5.getGroup());
        assertEquals(new HashSet<String>(), item5.getItemOptionNames());
        assertEquals("Not Just For Butterflies", counterGroup.getName());
        assertEquals(CollectionLogGroup.Type.COUNTER, counterGroup.getType());
        int[] pointValues = ((CollectionLogCounterGroup) counterGroup).getCounterPointValues();
        assertEquals(2, pointValues.length);
        assertEquals(4, pointValues[0]);
        assertEquals(3, pointValues[1]);
        assertNull(counterGroup.getDescription());
        assertTrue(counterGroup.getItems().contains(item6));
        assertTrue(counterGroup.getItems().contains(item7));
        assertTrue(counterGroup.getItems().contains(item8));
        assertEquals("Jar of chemicals", item6.getName());
        assertEquals(-1, item6.getPoints());
        assertEquals(counterGroup, item6.getGroup());
        assertEquals(new HashSet<String>(), item6.getItemOptionNames());
        assertEquals("Jar of darkness", item7.getName());
        assertEquals(-1, item7.getPoints());
        assertEquals(counterGroup, item7.getGroup());
        assertEquals(new HashSet<String>(), item7.getItemOptionNames());
        assertEquals("Jar of decay", item8.getName());
        assertEquals(-1, item8.getPoints());
        assertEquals(counterGroup, item8.getGroup());
        assertEquals(new HashSet<String>(), item8.getItemOptionNames());        
    }

}
