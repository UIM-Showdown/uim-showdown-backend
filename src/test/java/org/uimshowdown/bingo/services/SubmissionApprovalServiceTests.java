package org.uimshowdown.bingo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.uimshowdown.bingo.TestUtils;
import org.uimshowdown.bingo.models.Challenge;
import org.uimshowdown.bingo.models.ChallengeCompletion;
import org.uimshowdown.bingo.models.ChallengeRelayComponent;
import org.uimshowdown.bingo.models.ChallengeSubmission;
import org.uimshowdown.bingo.models.CollectionLogCompletion;
import org.uimshowdown.bingo.models.CollectionLogItem;
import org.uimshowdown.bingo.models.CollectionLogSubmission;
import org.uimshowdown.bingo.models.Contribution;
import org.uimshowdown.bingo.models.ContributionMethod;
import org.uimshowdown.bingo.models.ContributionSubmission;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.PlayerChallengeCompletion;
import org.uimshowdown.bingo.models.Record;
import org.uimshowdown.bingo.models.RecordCompletion;
import org.uimshowdown.bingo.models.RecordHandicap;
import org.uimshowdown.bingo.models.RecordSubmission;
import org.uimshowdown.bingo.models.Submission;
import org.uimshowdown.bingo.models.Team;
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
public class SubmissionApprovalServiceTests {
    
    @Autowired
    private TestUtils testUtils;
    
    @Autowired
    private SubmissionApprovalService submissionApprovalService;
    
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
    public void setUp() throws Exception {
        testUtils.resetDB();
        
        CollectionLogItem item = new CollectionLogItem();
        item.setName("Test Item");
        item = collectionLogItemRepository.save(item);
        
        ContributionMethod method = new ContributionMethod();
        method.setName("Test Method");
        method = contributionMethodRepository.save(method);
        
        Challenge relayChallenge = new Challenge();
        relayChallenge.setName("Test Relay Challenge");
        relayChallenge.setTeamSize(2);
        relayChallenge.setType(Challenge.Type.RELAY);
        ChallengeRelayComponent component1 = new ChallengeRelayComponent();
        component1.setName("Component 1");
        component1.setChallenge(relayChallenge);
        ChallengeRelayComponent component2 = new ChallengeRelayComponent();
        component2.setName("Component 2");
        component2.setChallenge(relayChallenge);
        Set<ChallengeRelayComponent> components = new HashSet<ChallengeRelayComponent>();
        components.add(component1);
        components.add(component2);
        relayChallenge.setRelayComponents(components);
        relayChallenge = challengeRepository.save(relayChallenge);
        
        Challenge speedrunChallenge = new Challenge();
        speedrunChallenge.setName("Test Speedrun Challenge");
        speedrunChallenge.setTeamSize(1);
        speedrunChallenge.setType(Challenge.Type.SPEEDRUN);
        speedrunChallenge = challengeRepository.save(speedrunChallenge);
        
        Record recordWithoutHandicap = new Record();
        recordWithoutHandicap.setSkill(Player.Skill.AGILITY);
        recordWithoutHandicap = recordRepository.save(recordWithoutHandicap);
        
        Record recordWithHandicap = new Record();
        recordWithHandicap.setSkill(Player.Skill.WOODCUTTING);
        Set<RecordHandicap> handicaps = new HashSet<RecordHandicap>();
        RecordHandicap handicap = new RecordHandicap();
        handicap.setName("Dragon Axe");
        handicap.setMultiplier(1.13);
        handicap.setRecord(recordWithHandicap);
        handicaps.add(handicap);
        recordWithHandicap.setHandicaps(handicaps);
        recordWithHandicap = recordRepository.save(recordWithHandicap);
        
        Contribution contribution = new Contribution();
        contribution.setContributionMethod(method);
        contribution.setInitialValue(0);
        contribution.setFinalValue(0);
        contribution.setStaffAdjustment(0);
        contribution.setUnrankedStartingValue(-1);
        contribution.setIsEmpty(true);
        
        ChallengeCompletion relayCompletion = new ChallengeCompletion();
        relayCompletion.setChallenge(relayChallenge);
        ChallengeCompletion speedrunCompletion = new ChallengeCompletion();
        speedrunCompletion.setChallenge(speedrunChallenge);
        

        Team team = new Team();
        team.setName("Test Team");
        relayCompletion.setTeam(team);
        speedrunCompletion.setTeam(team);
        Set<ChallengeCompletion> completions = new HashSet<ChallengeCompletion>();
        completions.add(relayCompletion);
        completions.add(speedrunCompletion);
        team.setChallengeCompletions(completions);
        Player player = new Player();
        player.setRsn("Test RSN");
        player.setDiscordName("Test Discord Name");
        player.setTeam(team);
        Set<Player> players = new HashSet<Player>();
        players.add(player);
        team.setPlayers(players);
        contribution.setPlayer(player);
        Set<Contribution> contributions = new HashSet<Contribution>();
        contributions.add(contribution);
        player.setContributions(contributions);
        team = teamRepository.save(team);
    }
    
    @AfterEach
    public void tearDown() {
        testUtils.resetDB();
    }
    
    @Test
    @Transactional
    public void denySubmissionSuccess() throws Exception {
        ContributionMethod method = contributionMethodRepository.findByName("Test Method").get();
        Player player = playerRepository.findByDiscordName("Test Discord Name").get();
        
        ContributionSubmission submission = new ContributionSubmission();
        submission.setContributionMethod(method);
        submission.setPlayer(player);
        submission.setSubmissionState(Submission.State.OPEN);
        submission.setType(Submission.Type.CONTRIBUTION);
        submission.setValue(5);
        submission = submissionRepository.save(submission);
        int id = submission.getId();
        
        submissionApprovalService.denySubmission(id, "Test Reviewer");
        
        submission = (ContributionSubmission) submissionRepository.findById(id).get();
        
        assertEquals(Submission.State.DENIED, submission.getSubmissionState());
        assertEquals("Test Reviewer", submission.getReviewer());
        assertTrue(Math.abs(submission.getReviewedAt().getTime() - new Date().getTime()) < 5000);
    }
    
    @Test
    @Transactional
    public void denySubmissionNoSubmission() throws Exception {
        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> submissionApprovalService.denySubmission(-1, "Test Reviewer"));
        
        assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        assertEquals("Submission not found: -1", e.getReason());
    }
    
    @Test
    @Transactional
    public void denySubmissionAlreadyDenied() throws Exception {
        ContributionMethod method = contributionMethodRepository.findByName("Test Method").get();
        Player player = playerRepository.findByDiscordName("Test Discord Name").get();
        
        ContributionSubmission submission = new ContributionSubmission();
        submission.setContributionMethod(method);
        submission.setPlayer(player);
        submission.setSubmissionState(Submission.State.DENIED);
        submission.setType(Submission.Type.CONTRIBUTION);
        submission.setValue(5);
        submission = submissionRepository.save(submission);
        int id = submission.getId();
        
        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> submissionApprovalService.denySubmission(id, "Test Reviewer"));
        
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        assertEquals("Submission is already denied", e.getReason());
    }
    
    @Test
    @Transactional
    public void denySubmissionAlreadyApproved() throws Exception {
        ContributionMethod method = contributionMethodRepository.findByName("Test Method").get();
        Player player = playerRepository.findByDiscordName("Test Discord Name").get();
        
        ContributionSubmission submission = new ContributionSubmission();
        submission.setContributionMethod(method);
        submission.setPlayer(player);
        submission.setSubmissionState(Submission.State.APPROVED);
        submission.setType(Submission.Type.CONTRIBUTION);
        submission.setValue(5);
        submission = submissionRepository.save(submission);
        int id = submission.getId();
        
        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> submissionApprovalService.denySubmission(id, "Test Reviewer"));
        
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        assertEquals("Submission is already approved", e.getReason());
    }
    
    @Test
    @Transactional
    public void approveRelayChallengeSubmission() throws Exception {
        Challenge challenge = challengeRepository.findByName("Test Relay Challenge").get();
        ChallengeRelayComponent component = challengeRelayComponentRepository.findByName("Component 1").get();
        Player player = playerRepository.findByDiscordName("Test Discord Name").get();
                
        // No existing submissions
        ChallengeSubmission submission = new ChallengeSubmission();
        submission.setChallenge(challenge);
        submission.setRelayComponent(component);
        submission.setPlayer(player);
        submission.setSubmissionState(Submission.State.OPEN);
        submission.setSeconds(5);
        submission.setType(Submission.Type.CHALLENGE);
        List<String> urls = new ArrayList<String>();
        urls.add("Test URL");
        submission.setScreenshotUrls(urls);
        submission = submissionRepository.save(submission);
        int id = submission.getId();
        
        submissionApprovalService.approveSubmission(id, "Test Reviewer");
        
        submission = (ChallengeSubmission) submissionRepository.findById(id).get();
        player = playerRepository.findById(player.getId()).get();
        Team team = teamRepository.findByName("Test Team").get();
        ChallengeCompletion challengeCompletion = null;
        for(ChallengeCompletion completion : team.getChallengeCompletions()) {
            if(completion.getChallenge().equals(challenge)) {
                challengeCompletion = completion;
            }
        }
        PlayerChallengeCompletion playerCompletion = player.getPlayerChallengeCompletions().toArray(new PlayerChallengeCompletion[0])[0];
        
        assertEquals(Submission.State.APPROVED, submission.getSubmissionState());
        assertEquals("Test Reviewer", submission.getReviewer());
        assertTrue(Math.abs(submission.getReviewedAt().getTime() - new Date().getTime()) < 5000);
        
        assertEquals(1, player.getPlayerChallengeCompletions().size());
        assertNotNull(playerCompletion);
        assertEquals("Test URL", playerCompletion.getScreenshotUrl());
        assertEquals(challenge, playerCompletion.getChallenge());
        assertEquals(challengeCompletion, playerCompletion.getChallengeCompletion());
        assertEquals(component, playerCompletion.getChallengeRelayComponent());
        assertEquals(player, playerCompletion.getPlayer());
        assertEquals(5, playerCompletion.getSeconds());
        
        // Submit something better than the existing one - Should change completion
        player = playerRepository.findByDiscordName("Test Discord Name").get();
        ChallengeSubmission submission2 = new ChallengeSubmission();
        submission2.setChallenge(challenge);
        submission2.setRelayComponent(component);
        submission2.setPlayer(player);
        submission2.setSubmissionState(Submission.State.OPEN);
        submission2.setSeconds(4);
        submission2.setType(Submission.Type.CHALLENGE);
        List<String> urls2 = new ArrayList<String>();
        urls2.add("Test URL 2");
        submission2.setScreenshotUrls(urls2);
        submission2 = submissionRepository.save(submission2);
        int id2 = submission2.getId();
        
        submissionApprovalService.approveSubmission(id2, "Test Reviewer");
        
        submission2 = (ChallengeSubmission) submissionRepository.findById(id2).get();
        player = playerRepository.findById(player.getId()).get();
        team = teamRepository.findByName("Test Team").get();
        challengeCompletion = null;
        for(ChallengeCompletion completion : team.getChallengeCompletions()) {
            if(completion.getChallenge().equals(challenge)) {
                challengeCompletion = completion;
            }
        }
        PlayerChallengeCompletion playerCompletion2 = player.getPlayerChallengeCompletions().toArray(new PlayerChallengeCompletion[0])[0];
        
        assertEquals(Submission.State.APPROVED, submission2.getSubmissionState());
        assertEquals("Test Reviewer", submission2.getReviewer());
        assertTrue(Math.abs(submission2.getReviewedAt().getTime() - new Date().getTime()) < 5000);
        
        assertEquals(1, player.getPlayerChallengeCompletions().size());
        assertNotNull(playerCompletion2);
        assertEquals("Test URL 2", playerCompletion2.getScreenshotUrl());
        assertEquals(challenge, playerCompletion2.getChallenge());
        assertEquals(challengeCompletion, playerCompletion2.getChallengeCompletion());
        assertEquals(component, playerCompletion2.getChallengeRelayComponent());
        assertEquals(player, playerCompletion2.getPlayer());
        assertEquals(4, playerCompletion2.getSeconds());
        
        // Submit something worse than the existing one - Shouldn't change completion
        player = playerRepository.findByDiscordName("Test Discord Name").get();
        ChallengeSubmission submission3 = new ChallengeSubmission();
        submission3.setChallenge(challenge);
        submission3.setRelayComponent(component);
        submission3.setPlayer(player);
        submission3.setSubmissionState(Submission.State.OPEN);
        submission3.setSeconds(6);
        submission3.setType(Submission.Type.CHALLENGE);
        List<String> urls3 = new ArrayList<String>();
        urls3.add("Test URL 3");
        submission3.setScreenshotUrls(urls3);
        submission3 = submissionRepository.save(submission3);
        int id3 = submission3.getId();
        
        submissionApprovalService.approveSubmission(id3, "Test Reviewer");
        
        submission3 = (ChallengeSubmission) submissionRepository.findById(id3).get();
        player = playerRepository.findById(player.getId()).get();
        team = teamRepository.findByName("Test Team").get();
        challengeCompletion = null;
        for(ChallengeCompletion completion : team.getChallengeCompletions()) {
            if(completion.getChallenge().equals(challenge)) {
                challengeCompletion = completion;
            }
        }
        PlayerChallengeCompletion playerCompletion3 = player.getPlayerChallengeCompletions().toArray(new PlayerChallengeCompletion[0])[0];
        
        assertEquals(Submission.State.APPROVED, submission3.getSubmissionState());
        assertEquals("Test Reviewer", submission3.getReviewer());
        assertTrue(Math.abs(submission3.getReviewedAt().getTime() - new Date().getTime()) < 5000);
        
        assertEquals(1, player.getPlayerChallengeCompletions().size());
        assertNotNull(playerCompletion3);
        assertEquals("Test URL 2", playerCompletion3.getScreenshotUrl());
        assertEquals(challenge, playerCompletion3.getChallenge());
        assertEquals(challengeCompletion, playerCompletion3.getChallengeCompletion());
        assertEquals(component, playerCompletion3.getChallengeRelayComponent());
        assertEquals(player, playerCompletion3.getPlayer());
        assertEquals(4, playerCompletion3.getSeconds());
    }
    
    @Test
    @Transactional
    public void approveSpeedrunChallengeSubmission() throws Exception {
        Challenge challenge = challengeRepository.findByName("Test Speedrun Challenge").get();
        Player player = playerRepository.findByDiscordName("Test Discord Name").get();
                
        // No existing submissions
        ChallengeSubmission submission = new ChallengeSubmission();
        submission.setChallenge(challenge);
        submission.setPlayer(player);
        submission.setSubmissionState(Submission.State.OPEN);
        submission.setSeconds(5);
        submission.setType(Submission.Type.CHALLENGE);
        List<String> urls = new ArrayList<String>();
        urls.add("Test URL");
        submission.setScreenshotUrls(urls);
        submission = submissionRepository.save(submission);
        int id = submission.getId();
        
        submissionApprovalService.approveSubmission(id, "Test Reviewer");
        
        submission = (ChallengeSubmission) submissionRepository.findById(id).get();
        player = playerRepository.findById(player.getId()).get();
        Team team = teamRepository.findByName("Test Team").get();
        ChallengeCompletion challengeCompletion = null;
        for(ChallengeCompletion completion : team.getChallengeCompletions()) {
            if(completion.getChallenge().equals(challenge)) {
                challengeCompletion = completion;
            }
        }
        PlayerChallengeCompletion playerCompletion = player.getPlayerChallengeCompletions().toArray(new PlayerChallengeCompletion[0])[0];
        
        assertEquals(Submission.State.APPROVED, submission.getSubmissionState());
        assertEquals("Test Reviewer", submission.getReviewer());
        assertTrue(Math.abs(submission.getReviewedAt().getTime() - new Date().getTime()) < 5000);
        
        assertEquals(1, player.getPlayerChallengeCompletions().size());
        assertNotNull(playerCompletion);
        assertEquals("Test URL", playerCompletion.getScreenshotUrl());
        assertEquals(challenge, playerCompletion.getChallenge());
        assertEquals(challengeCompletion, playerCompletion.getChallengeCompletion());
        assertEquals(player, playerCompletion.getPlayer());
        assertEquals(5, playerCompletion.getSeconds());
        
        // Submit something better than the existing one - Should change completion
        player = playerRepository.findByDiscordName("Test Discord Name").get();
        ChallengeSubmission submission2 = new ChallengeSubmission();
        submission2.setChallenge(challenge);
        submission2.setPlayer(player);
        submission2.setSubmissionState(Submission.State.OPEN);
        submission2.setSeconds(4);
        submission2.setType(Submission.Type.CHALLENGE);
        List<String> urls2 = new ArrayList<String>();
        urls2.add("Test URL 2");
        submission2.setScreenshotUrls(urls2);
        submission2 = submissionRepository.save(submission2);
        int id2 = submission2.getId();
        
        submissionApprovalService.approveSubmission(id2, "Test Reviewer");
        
        submission2 = (ChallengeSubmission) submissionRepository.findById(id2).get();
        player = playerRepository.findById(player.getId()).get();
        team = teamRepository.findByName("Test Team").get();
        challengeCompletion = null;
        for(ChallengeCompletion completion : team.getChallengeCompletions()) {
            if(completion.getChallenge().equals(challenge)) {
                challengeCompletion = completion;
            }
        }
        PlayerChallengeCompletion playerCompletion2 = player.getPlayerChallengeCompletions().toArray(new PlayerChallengeCompletion[0])[0];
        
        assertEquals(Submission.State.APPROVED, submission2.getSubmissionState());
        assertEquals("Test Reviewer", submission2.getReviewer());
        assertTrue(Math.abs(submission2.getReviewedAt().getTime() - new Date().getTime()) < 5000);
        
        assertEquals(1, player.getPlayerChallengeCompletions().size());
        assertNotNull(playerCompletion2);
        assertEquals("Test URL 2", playerCompletion2.getScreenshotUrl());
        assertEquals(challenge, playerCompletion2.getChallenge());
        assertEquals(challengeCompletion, playerCompletion2.getChallengeCompletion());
        assertEquals(player, playerCompletion2.getPlayer());
        assertEquals(4, playerCompletion2.getSeconds());
        
        // Submit something worse than the existing one - Shouldn't change completion
        player = playerRepository.findByDiscordName("Test Discord Name").get();
        ChallengeSubmission submission3 = new ChallengeSubmission();
        submission3.setChallenge(challenge);
        submission3.setPlayer(player);
        submission3.setSubmissionState(Submission.State.OPEN);
        submission3.setSeconds(6);
        submission3.setType(Submission.Type.CHALLENGE);
        List<String> urls3 = new ArrayList<String>();
        urls3.add("Test URL 3");
        submission3.setScreenshotUrls(urls3);
        submission3 = submissionRepository.save(submission3);
        int id3 = submission3.getId();
        
        submissionApprovalService.approveSubmission(id3, "Test Reviewer");
        
        submission3 = (ChallengeSubmission) submissionRepository.findById(id3).get();
        player = playerRepository.findById(player.getId()).get();
        team = teamRepository.findByName("Test Team").get();
        challengeCompletion = null;
        for(ChallengeCompletion completion : team.getChallengeCompletions()) {
            if(completion.getChallenge().equals(challenge)) {
                challengeCompletion = completion;
            }
        }
        PlayerChallengeCompletion playerCompletion3 = player.getPlayerChallengeCompletions().toArray(new PlayerChallengeCompletion[0])[0];
        
        assertEquals(Submission.State.APPROVED, submission3.getSubmissionState());
        assertEquals("Test Reviewer", submission3.getReviewer());
        assertTrue(Math.abs(submission3.getReviewedAt().getTime() - new Date().getTime()) < 5000);
        
        assertEquals(1, player.getPlayerChallengeCompletions().size());
        assertNotNull(playerCompletion3);
        assertEquals("Test URL 2", playerCompletion3.getScreenshotUrl());
        assertEquals(challenge, playerCompletion3.getChallenge());
        assertEquals(challengeCompletion, playerCompletion3.getChallengeCompletion());
        assertEquals(player, playerCompletion3.getPlayer());
        assertEquals(4, playerCompletion3.getSeconds());
    }

    @Test
    @Transactional
    public void approveCollectionLogSubmission() throws Exception {
        Player player = playerRepository.findByDiscordName("Test Discord Name").get();
        CollectionLogItem item = collectionLogItemRepository.findByName("Test Item").get();
        
        // No existing submissions
        CollectionLogSubmission submission = new CollectionLogSubmission();
        submission.setItem(item);
        submission.setPlayer(player);
        List<String> urls = new ArrayList<String>();
        urls.add("Test URL");
        submission.setScreenshotUrls(urls);
        submission.setSubmissionState(Submission.State.OPEN);
        submission.setType(Submission.Type.COLLECTION_LOG);
        submission = submissionRepository.save(submission);
        int id = submission.getId();
        
        submissionApprovalService.approveSubmission(id, "Test Reviewer");
        
        submission = (CollectionLogSubmission) submissionRepository.findById(id).get();
        player = playerRepository.findById(player.getId()).get();
        
        CollectionLogCompletion completion = player.getCollectionLogCompletions().toArray(new CollectionLogCompletion[0])[0];
        
        assertEquals(Submission.State.APPROVED, submission.getSubmissionState());
        assertEquals("Test Reviewer", submission.getReviewer());
        assertTrue(Math.abs(submission.getReviewedAt().getTime() - new Date().getTime()) < 5000);
        
        assertEquals(1, player.getCollectionLogCompletions().size());
        assertNotNull(completion);
        assertEquals("Test URL", completion.getScreenshotUrl());
        assertEquals(item, completion.getItem());
        assertEquals(player, completion.getPlayer());
        
        // Submit again - Shouldn't change completion
        player = playerRepository.findById(player.getId()).get();
        CollectionLogSubmission submission2 = new CollectionLogSubmission();
        submission2.setItem(item);
        submission2.setPlayer(player);
        List<String> urls2 = new ArrayList<String>();
        urls2.add("Test URL 2");
        submission2.setScreenshotUrls(urls2);
        submission2.setSubmissionState(Submission.State.OPEN);
        submission2.setType(Submission.Type.COLLECTION_LOG);
        submission2 = submissionRepository.save(submission2);
        int id2 = submission2.getId();
        
        submissionApprovalService.approveSubmission(id2, "Test Reviewer");
        
        submission2 = (CollectionLogSubmission) submissionRepository.findById(id2).get();
        player = playerRepository.findById(player.getId()).get();
        
        CollectionLogCompletion completion2 = player.getCollectionLogCompletions().toArray(new CollectionLogCompletion[0])[0];
        
        assertEquals(Submission.State.APPROVED, submission2.getSubmissionState());
        assertEquals("Test Reviewer", submission2.getReviewer());
        assertTrue(Math.abs(submission2.getReviewedAt().getTime() - new Date().getTime()) < 5000);
        
        assertEquals(1, player.getCollectionLogCompletions().size());
        assertNotNull(completion2);
        assertEquals("Test URL", completion2.getScreenshotUrl());
        assertEquals(item, completion2.getItem());
        assertEquals(player, completion2.getPlayer());
    }
    
    @Test
    @Transactional
    public void approveContributionSubmission() throws Exception {
        Player player = playerRepository.findByDiscordName("Test Discord Name").get();
        ContributionMethod method = contributionMethodRepository.findByName("Test Method").get();
        
        // No existing submissions
        ContributionSubmission submission = new ContributionSubmission();
        submission.setContributionMethod(method);
        submission.setValue(1);
        submission.setPlayer(player);
        List<String> urls = new ArrayList<String>();
        urls.add("Test URL");
        submission.setScreenshotUrls(urls);
        submission.setSubmissionState(Submission.State.OPEN);
        submission.setType(Submission.Type.CONTRIBUTION);
        submission = submissionRepository.save(submission);
        int id = submission.getId();
        
        submissionApprovalService.approveSubmission(id, "Test Reviewer");
        
        submission = (ContributionSubmission) submissionRepository.findById(id).get();
        player = playerRepository.findById(player.getId()).get();
        
        Contribution contribution = player.getContributions().toArray(new Contribution[0])[0];
        
        assertEquals(Submission.State.APPROVED, submission.getSubmissionState());
        assertEquals("Test Reviewer", submission.getReviewer());
        assertTrue(Math.abs(submission.getReviewedAt().getTime() - new Date().getTime()) < 5000);
        
        assertEquals(1, player.getContributions().size());
        assertNotNull(contribution);
        assertFalse(contribution.isEmpty());
        assertEquals(method, contribution.getContributionMethod());
        assertEquals(1, contribution.getInitialValue());
        assertEquals("Test URL", contribution.getInitialValueScreenshotUrl());
        assertEquals(1, contribution.getFinalValue());
        assertEquals("Test URL", contribution.getFinalValueScreenshotUrl());
        assertEquals(-1, contribution.getUnrankedStartingValue());
        assertEquals(0, contribution.getStaffAdjustment());
        
        // Submit higher value - Should update contribution
        ContributionSubmission submission2 = new ContributionSubmission();
        submission2.setContributionMethod(method);
        submission2.setValue(2);
        submission2.setPlayer(player);
        List<String> urls2 = new ArrayList<String>();
        urls2.add("Test URL 2");
        submission2.setScreenshotUrls(urls2);
        submission2.setSubmissionState(Submission.State.OPEN);
        submission2.setType(Submission.Type.CONTRIBUTION);
        submission2 = submissionRepository.save(submission2);
        int id2 = submission2.getId();
        
        submissionApprovalService.approveSubmission(id2, "Test Reviewer");
        
        submission2 = (ContributionSubmission) submissionRepository.findById(id2).get();
        player = playerRepository.findById(player.getId()).get();
        
        Contribution contribution2 = player.getContributions().toArray(new Contribution[0])[0];
        
        assertEquals(Submission.State.APPROVED, submission2.getSubmissionState());
        assertEquals("Test Reviewer", submission2.getReviewer());
        assertTrue(Math.abs(submission2.getReviewedAt().getTime() - new Date().getTime()) < 5000);
        
        assertEquals(1, player.getContributions().size());
        assertNotNull(contribution2);
        assertFalse(contribution2.isEmpty());
        assertEquals(method, contribution2.getContributionMethod());
        assertEquals(1, contribution2.getInitialValue());
        assertEquals("Test URL", contribution2.getInitialValueScreenshotUrl());
        assertEquals(2, contribution2.getFinalValue());
        assertEquals("Test URL 2", contribution2.getFinalValueScreenshotUrl());
        assertEquals(-1, contribution2.getUnrankedStartingValue());
        assertEquals(0, contribution2.getStaffAdjustment());
        
        // Submit lower value - Should not update contribution
        ContributionSubmission submission3 = new ContributionSubmission();
        submission3.setContributionMethod(method);
        submission3.setValue(0);
        submission3.setPlayer(player);
        List<String> urls3 = new ArrayList<String>();
        urls3.add("Test URL 3");
        submission3.setScreenshotUrls(urls3);
        submission3.setSubmissionState(Submission.State.OPEN);
        submission3.setType(Submission.Type.CONTRIBUTION);
        submission3 = submissionRepository.save(submission3);
        int id3 = submission3.getId();
        
        submissionApprovalService.approveSubmission(id3, "Test Reviewer");
        
        submission3 = (ContributionSubmission) submissionRepository.findById(id3).get();
        player = playerRepository.findById(player.getId()).get();
        
        Contribution contribution3 = player.getContributions().toArray(new Contribution[0])[0];
        
        assertEquals(Submission.State.APPROVED, submission3.getSubmissionState());
        assertEquals("Test Reviewer", submission3.getReviewer());
        assertTrue(Math.abs(submission3.getReviewedAt().getTime() - new Date().getTime()) < 5000);
        
        assertEquals(1, player.getContributions().size());
        assertNotNull(contribution3);
        assertFalse(contribution3.isEmpty());
        assertEquals(method, contribution3.getContributionMethod());
        assertEquals(1, contribution3.getInitialValue());
        assertEquals("Test URL", contribution3.getInitialValueScreenshotUrl());
        assertEquals(2, contribution3.getFinalValue());
        assertEquals("Test URL 2", contribution3.getFinalValueScreenshotUrl());
        assertEquals(-1, contribution3.getUnrankedStartingValue());
        assertEquals(0, contribution3.getStaffAdjustment());
    }
    
    @Test
    @Transactional
    public void approveRecordSubmissionWithoutHandicap() throws Exception {
        Player player = playerRepository.findByDiscordName("Test Discord Name").get();
        Record record = recordRepository.findBySkill(Player.Skill.AGILITY).get();
        
        // No existing submissions
        RecordSubmission submission = new RecordSubmission();
        submission.setRecord(record);
        submission.setRawValue(1000);
        submission.setPlayer(player);
        submission.setVideoURL("Test Video URL");
        submission.setSubmissionState(Submission.State.OPEN);
        submission.setType(Submission.Type.RECORD);
        submission = submissionRepository.save(submission);
        int id = submission.getId();
        
        submissionApprovalService.approveSubmission(id, "Test Reviewer");
        
        submission = (RecordSubmission) submissionRepository.findById(id).get();
        player = playerRepository.findById(player.getId()).get();
        
        RecordCompletion completion = player.getRecordCompletions().toArray(new RecordCompletion[0])[0];
        
        assertEquals(Submission.State.APPROVED, submission.getSubmissionState());
        assertEquals("Test Reviewer", submission.getReviewer());
        assertTrue(Math.abs(submission.getReviewedAt().getTime() - new Date().getTime()) < 5000);
        
        assertEquals(1, player.getRecordCompletions().size());
        assertNotNull(completion);
        assertEquals(player, completion.getPlayer());
        assertEquals(1000, completion.getRawValue());
        assertEquals(record, completion.getRecord());
        assertEquals("Test Video URL", completion.getVideoUrl());
        
        // Submit a better record - Update the existing completion
        RecordSubmission submission2 = new RecordSubmission();
        submission2.setRecord(record);
        submission2.setRawValue(2000);
        submission2.setPlayer(player);
        submission2.setVideoURL("Test Video URL 2");
        submission2.setSubmissionState(Submission.State.OPEN);
        submission2.setType(Submission.Type.RECORD);
        submission2 = submissionRepository.save(submission2);
        int id2 = submission2.getId();
        
        submissionApprovalService.approveSubmission(id2, "Test Reviewer");
        
        submission2 = (RecordSubmission) submissionRepository.findById(id2).get();
        player = playerRepository.findById(player.getId()).get();
        
        RecordCompletion completion2 = player.getRecordCompletions().toArray(new RecordCompletion[0])[0];
        
        assertEquals(Submission.State.APPROVED, submission2.getSubmissionState());
        assertEquals("Test Reviewer", submission2.getReviewer());
        assertTrue(Math.abs(submission2.getReviewedAt().getTime() - new Date().getTime()) < 5000);
        
        assertEquals(1, player.getRecordCompletions().size());
        assertNotNull(completion2);
        assertEquals(player, completion2.getPlayer());
        assertEquals(2000, completion2.getRawValue());
        assertEquals(record, completion2.getRecord());
        assertEquals("Test Video URL 2", completion2.getVideoUrl());
        
        // Submit a worse record - Update the existing completion
        RecordSubmission submission3 = new RecordSubmission();
        submission3.setRecord(record);
        submission3.setRawValue(500);
        submission3.setPlayer(player);
        submission3.setVideoURL("Test Video URL 3");
        submission3.setSubmissionState(Submission.State.OPEN);
        submission3.setType(Submission.Type.RECORD);
        submission3 = submissionRepository.save(submission3);
        int id3 = submission3.getId();
        
        submissionApprovalService.approveSubmission(id3, "Test Reviewer");
        
        submission3 = (RecordSubmission) submissionRepository.findById(id3).get();
        player = playerRepository.findById(player.getId()).get();
        
        RecordCompletion completion3 = player.getRecordCompletions().toArray(new RecordCompletion[0])[0];
        
        assertEquals(Submission.State.APPROVED, submission3.getSubmissionState());
        assertEquals("Test Reviewer", submission3.getReviewer());
        assertTrue(Math.abs(submission3.getReviewedAt().getTime() - new Date().getTime()) < 5000);
        
        assertEquals(1, player.getRecordCompletions().size());
        assertNotNull(completion3);
        assertEquals(player, completion3.getPlayer());
        assertEquals(2000, completion3.getRawValue());
        assertEquals(record, completion3.getRecord());
        assertEquals("Test Video URL 2", completion3.getVideoUrl());
    }
    
    @Test
    @Transactional
    public void approveRecordSubmissionWithHandicap() throws Exception {
        Player player = playerRepository.findByDiscordName("Test Discord Name").get();
        Record record = recordRepository.findBySkill(Player.Skill.WOODCUTTING).get();
        RecordHandicap handicap = recordHandicapRepository.findByName("Dragon Axe").get();
        
        // No existing submissions
        RecordSubmission submission = new RecordSubmission();
        submission.setRecord(record);
        submission.setRawValue(1000);
        submission.setPlayer(player);
        submission.setVideoURL("Test Video URL");
        submission.setHandicap(handicap);
        submission.setSubmissionState(Submission.State.OPEN);
        submission.setType(Submission.Type.RECORD);
        submission = submissionRepository.save(submission);
        int id = submission.getId();
        
        submissionApprovalService.approveSubmission(id, "Test Reviewer");
        
        submission = (RecordSubmission) submissionRepository.findById(id).get();
        player = playerRepository.findById(player.getId()).get();
        
        RecordCompletion completion = player.getRecordCompletions().toArray(new RecordCompletion[0])[0];
        
        assertEquals(Submission.State.APPROVED, submission.getSubmissionState());
        assertEquals("Test Reviewer", submission.getReviewer());
        assertTrue(Math.abs(submission.getReviewedAt().getTime() - new Date().getTime()) < 5000);
        
        assertEquals(1, player.getRecordCompletions().size());
        assertNotNull(completion);
        assertEquals(player, completion.getPlayer());
        assertEquals(1000, completion.getRawValue());
        assertEquals(record, completion.getRecord());
        assertEquals("Test Video URL", completion.getVideoUrl());
        assertEquals(handicap, completion.getHandicap());
        
        // Submit a better record - Update the existing completion
        RecordSubmission submission2 = new RecordSubmission();
        submission2.setRecord(record);
        submission2.setRawValue(2000);
        submission2.setPlayer(player);
        submission2.setVideoURL("Test Video URL 2");
        submission2.setHandicap(handicap);
        submission2.setSubmissionState(Submission.State.OPEN);
        submission2.setType(Submission.Type.RECORD);
        submission2 = submissionRepository.save(submission2);
        int id2 = submission2.getId();
        
        submissionApprovalService.approveSubmission(id2, "Test Reviewer");
        
        submission2 = (RecordSubmission) submissionRepository.findById(id2).get();
        player = playerRepository.findById(player.getId()).get();
        
        RecordCompletion completion2 = player.getRecordCompletions().toArray(new RecordCompletion[0])[0];
        
        assertEquals(Submission.State.APPROVED, submission2.getSubmissionState());
        assertEquals("Test Reviewer", submission2.getReviewer());
        assertTrue(Math.abs(submission2.getReviewedAt().getTime() - new Date().getTime()) < 5000);
        
        assertEquals(1, player.getRecordCompletions().size());
        assertNotNull(completion2);
        assertEquals(player, completion2.getPlayer());
        assertEquals(2000, completion2.getRawValue());
        assertEquals(record, completion2.getRecord());
        assertEquals("Test Video URL 2", completion2.getVideoUrl());
        assertEquals(handicap, completion2.getHandicap());
        
        // Submit a worse record - Update the existing completion
        RecordSubmission submission3 = new RecordSubmission();
        submission3.setRecord(record);
        submission3.setRawValue(500);
        submission3.setPlayer(player);
        submission3.setVideoURL("Test Video URL 3");
        submission3.setHandicap(handicap);
        submission3.setSubmissionState(Submission.State.OPEN);
        submission3.setType(Submission.Type.RECORD);
        submission3 = submissionRepository.save(submission3);
        int id3 = submission3.getId();
        
        submissionApprovalService.approveSubmission(id3, "Test Reviewer");
        
        submission3 = (RecordSubmission) submissionRepository.findById(id3).get();
        player = playerRepository.findById(player.getId()).get();
        
        RecordCompletion completion3 = player.getRecordCompletions().toArray(new RecordCompletion[0])[0];
        
        assertEquals(Submission.State.APPROVED, submission3.getSubmissionState());
        assertEquals("Test Reviewer", submission3.getReviewer());
        assertTrue(Math.abs(submission3.getReviewedAt().getTime() - new Date().getTime()) < 5000);
        
        assertEquals(1, player.getRecordCompletions().size());
        assertNotNull(completion3);
        assertEquals(player, completion3.getPlayer());
        assertEquals(2000, completion3.getRawValue());
        assertEquals(record, completion3.getRecord());
        assertEquals("Test Video URL 2", completion3.getVideoUrl());
        assertEquals(handicap, completion3.getHandicap());
    }
    
    @Test
    @Transactional
    public void approveSubmissionNoSubmission() throws Exception {
        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> submissionApprovalService.approveSubmission(-1, "Test Reviewer"));
        
        assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        assertEquals("Submission not found: -1", e.getReason());
    }
    
    @Test
    @Transactional
    public void approveSubmissionAlreadyDenied() throws Exception {
        ContributionMethod method = contributionMethodRepository.findByName("Test Method").get();
        Player player = playerRepository.findByDiscordName("Test Discord Name").get();
        
        ContributionSubmission submission = new ContributionSubmission();
        submission.setContributionMethod(method);
        submission.setPlayer(player);
        submission.setSubmissionState(Submission.State.DENIED);
        submission.setType(Submission.Type.CONTRIBUTION);
        submission.setValue(5);
        submission = submissionRepository.save(submission);
        int id = submission.getId();
        
        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> submissionApprovalService.approveSubmission(id, "Test Reviewer"));
        
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        assertEquals("Submission is already denied", e.getReason());
    }
    
    @Test
    @Transactional
    public void approveSubmissionAlreadyApproved() throws Exception {
        ContributionMethod method = contributionMethodRepository.findByName("Test Method").get();
        Player player = playerRepository.findByDiscordName("Test Discord Name").get();
        
        ContributionSubmission submission = new ContributionSubmission();
        submission.setContributionMethod(method);
        submission.setPlayer(player);
        submission.setSubmissionState(Submission.State.APPROVED);
        submission.setType(Submission.Type.CONTRIBUTION);
        submission.setValue(5);
        submission = submissionRepository.save(submission);
        int id = submission.getId();
        
        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> submissionApprovalService.approveSubmission(id, "Test Reviewer"));
        
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        assertEquals("Submission is already approved", e.getReason());
    }

}
