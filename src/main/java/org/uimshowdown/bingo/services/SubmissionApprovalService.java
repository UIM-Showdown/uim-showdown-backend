package org.uimshowdown.bingo.services;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.uimshowdown.bingo.models.ChallengeSubmission;
import org.uimshowdown.bingo.models.CollectionLogCompletion;
import org.uimshowdown.bingo.models.CollectionLogSubmission;
import org.uimshowdown.bingo.models.Contribution;
import org.uimshowdown.bingo.models.ContributionSubmission;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.PlayerChallengeCompletion;
import org.uimshowdown.bingo.models.RecordCompletion;
import org.uimshowdown.bingo.models.RecordSubmission;
import org.uimshowdown.bingo.models.Submission;
import org.uimshowdown.bingo.models.UnrankedStartingValueSubmission;
import org.uimshowdown.bingo.repositories.PlayerChallengeCompletionRepository;
import org.uimshowdown.bingo.repositories.PlayerRepository;
import org.uimshowdown.bingo.repositories.RecordCompletionRepository;
import org.uimshowdown.bingo.repositories.SubmissionRepository;
import org.uimshowdown.bingo.repositories.TeamRepository;

@Component
public class SubmissionApprovalService {
    
    @Autowired SubmissionRepository submissionRepository;
    @Autowired PlayerRepository playerRepository;
    @Autowired TeamRepository teamRepository;
    @Autowired PlayerChallengeCompletionRepository playerChallengeCompletionRepository;
    @Autowired RecordCompletionRepository recordCompletionRepository;
    
    /**
     * Marks a submission as denied
     * @param id
     * @throws Exception
     */
    public Submission denySubmission(int id, String reviewer) throws Exception {
        Submission submission = submissionRepository.findById(id).orElse(null);
        if(submission == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Submission not found: " + id);
        }
        if(submission.getSubmissionState() == Submission.State.DENIED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Submission is already denied");
        }
        if(submission.getSubmissionState() == Submission.State.APPROVED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Submission is already approved");
        }
        submission.setSubmissionState(Submission.State.DENIED);
        submission.setReviewedAt(new Timestamp(new Date().getTime()));
        submission.setReviewer(reviewer);
        return submissionRepository.save(submission);
    }
    
    /**
     * Marks a submission as approved and processes its approval
     * @param id
     * @throws Exception
     */
    public Submission approveSubmission(int id, String reviewer) throws Exception {
        Submission submission = submissionRepository.findById(id).orElse(null);
        if(submission == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Submission not found: " + id);
        }
        if(submission.getSubmissionState() == Submission.State.DENIED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Submission is already denied");
        }
        if(submission.getSubmissionState() == Submission.State.APPROVED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Submission is already approved");
        }
        if(submission instanceof ChallengeSubmission) {
            processChallengeSubmission((ChallengeSubmission) submission);
        }
        if(submission instanceof CollectionLogSubmission) {
            processCollectionLogSubmission((CollectionLogSubmission) submission);
        }
        if(submission instanceof ContributionSubmission) {
            processContributionSubmission((ContributionSubmission) submission);
        }
        if(submission instanceof RecordSubmission) {
            processRecordSubmission((RecordSubmission) submission);
        }
        if(submission instanceof UnrankedStartingValueSubmission) {
            processUnrankedStartingValueSubmission((UnrankedStartingValueSubmission) submission);
        }
        submission.setSubmissionState(Submission.State.APPROVED);
        submission.setReviewedAt(new Timestamp(new Date().getTime()));
        submission.setReviewer(reviewer);
        return submissionRepository.save(submission);
    }
    
    /**
     * Adds a challenge completion to the player or replaces one, and does the same for the team's challenge completion
     * @param submission
     * @throws Exception
     */
    private void processChallengeSubmission(ChallengeSubmission submission) throws Exception {
        Player player = submission.getPlayer();
        
        // Remove the old player challenge completion if it exists
        PlayerChallengeCompletion existingPlayerChallengeCompletion = null;
        for(PlayerChallengeCompletion completion : player.getPlayerChallengeCompletions()) {
            boolean sameChallenge = completion.getChallenge().equals(submission.getChallenge());
            boolean sameComponent = completion.getChallengeRelayComponent() == null && submission.getRelayComponent() == null
                    || (completion.getChallengeRelayComponent() != null && completion.getChallengeRelayComponent().equals(submission.getRelayComponent()));
            if(sameChallenge && sameComponent) {
                existingPlayerChallengeCompletion = completion;
                if(existingPlayerChallengeCompletion.getSeconds() < submission.getSeconds()) { // Submissions were approved out of order and this is an out-of-date one
                    return;
                }
                break;
            }
        }
        if(existingPlayerChallengeCompletion != null) { // Update the existing one
            if(submission.getScreenshotUrls().size() > 0) {
                existingPlayerChallengeCompletion.setScreenshotUrl(submission.getScreenshotUrls().get(0));
            }
            existingPlayerChallengeCompletion.setSeconds(submission.getSeconds());
            playerRepository.save(player);
            return;
        }
        
        // Old one didn't exist, so make a new one
        PlayerChallengeCompletion playerCompletion = new PlayerChallengeCompletion();
        playerCompletion.setChallenge(submission.getChallenge());
        playerCompletion.setPlayer(player);
        playerCompletion.setRelayComponent(submission.getRelayComponent());
        if(submission.getScreenshotUrls().size() > 0) {
            playerCompletion.setScreenshotUrl(submission.getScreenshotUrls().get(0));
        }
        playerCompletion.setSeconds(submission.getSeconds());
        player.getPlayerChallengeCompletions().add(playerCompletion);
        playerRepository.save(player);
    }
    
    /**
     * Adds a collection log completion to the player
     * @param submission
     * @throws Exception
     */
    private void processCollectionLogSubmission(CollectionLogSubmission submission) throws Exception {
        Player player = submission.getPlayer();
        for(CollectionLogCompletion completion : player.getCollectionLogCompletions()) {
            if(completion.getItem().equals(submission.getItem())) { // Player already has a completion for this item
                return;
            }
        }
        CollectionLogCompletion completion = new CollectionLogCompletion();
        completion.setPlayer(player);
        completion.setItem(submission.getItem());
        if(submission.getScreenshotUrls().size() > 0) {
            completion.setScreenshotUrl(submission.getScreenshotUrls().get(0));
        }
        player.getCollectionLogCompletions().add(completion);
        playerRepository.save(player);
    }
    
    /**
     * Updates the player's contribution to have the new value
     * @param submission
     * @throws Exception
     */
    private void processContributionSubmission(ContributionSubmission submission) throws Exception {
        Player player = submission.getPlayer();
        for(Contribution contribution : player.getContributions()) {
            if(contribution.getContributionMethod().equals(submission.getContributionMethod())) {
                if(contribution.getFinalValue() > submission.getValue()) { // Submissions were approved out of order and this is an out-of-date one
                    return;
                }
                if(contribution.isEmpty()) {
                    contribution.setInitialValue(submission.getValue());
                    contribution.setIsEmpty(false);
                    if(submission.getScreenshotUrls().size() > 0) {
                        contribution.setInitialValueScreenshotUrl(submission.getScreenshotUrls().get(0));
                    }
                }
                contribution.setFinalValue(submission.getValue());
                if(submission.getScreenshotUrls().size() > 0) {
                    contribution.setFinalValueScreenshotUrl(submission.getScreenshotUrls().get(0));
                }
                break;
            }
        }
        playerRepository.save(player);
    }
    
    /**
     * Adds a record completion to the player or replaces an existing one
     * @param submission
     * @throws Exception
     */
    private void processRecordSubmission(RecordSubmission submission) throws Exception {
        Player player = submission.getPlayer();
        RecordCompletion existingCompletion = null;
        for(RecordCompletion completion : player.getRecordCompletions()) {
            if(completion.getRecord().equals(submission.getRecord())) {
                existingCompletion = completion;
                if(existingCompletion.getValue() > submission.getValue()) { // Submissions were approved out of order and this is an out-of-date one
                    return;
                }
                break;
            }
        }
        
        if(existingCompletion != null) { // Update the existing one
            existingCompletion.setCompletedAt(submission.getCompletedAt());
            existingCompletion.setHandicap(submission.getHandicap());
            existingCompletion.setRawValue(submission.getRawValue());
            existingCompletion.setVideoUrl(submission.getVideoUrl());
            playerRepository.save(player);
            return;
        }
        
        // Old one didn't exist, so make a new one
        RecordCompletion completion = new RecordCompletion();
        completion.setCompletedAt(submission.getCompletedAt());
        completion.setHandicap(submission.getHandicap());
        completion.setPlayer(submission.getPlayer());
        completion.setRawValue(submission.getRawValue());
        completion.setRecord(submission.getRecord());
        completion.setVideoUrl(submission.getVideoUrl());
        player.getRecordCompletions().add(completion);
        playerRepository.save(player);
    }
    
    /**
     * Updates the player's contribution to have the new unranked starting value
     * @param submission
     * @throws Exception
     */
    private void processUnrankedStartingValueSubmission(UnrankedStartingValueSubmission submission) throws Exception {
        Player player = submission.getPlayer();
        for(Contribution contribution : player.getContributions()) {
            if(contribution.getContributionMethod().equals(submission.getContributionMethod())) {
                contribution.setUnrankedStartingValue(submission.getValue());
                break;
            }
        }
        playerRepository.save(player);
    }

}
