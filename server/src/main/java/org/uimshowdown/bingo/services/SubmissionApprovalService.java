package org.uimshowdown.bingo.services;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.uimshowdown.bingo.models.Challenge;
import org.uimshowdown.bingo.models.ChallengeRelayComponent;
import org.uimshowdown.bingo.models.SpeedChallengeSubmission;
import org.uimshowdown.bingo.models.CollectionLogCompletion;
import org.uimshowdown.bingo.models.CollectionLogSubmission;
import org.uimshowdown.bingo.models.Contribution;
import org.uimshowdown.bingo.models.ContributionIncrementSubmission;
import org.uimshowdown.bingo.models.ContributionMethod;
import org.uimshowdown.bingo.models.ContributionPurchaseSubmission;
import org.uimshowdown.bingo.models.ContributionSubmission;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.PlayerPointsChallengeCompletion;
import org.uimshowdown.bingo.models.PlayerSpeedChallengeCompletion;
import org.uimshowdown.bingo.models.PointsChallengeSubmission;
import org.uimshowdown.bingo.models.Record;
import org.uimshowdown.bingo.models.RecordCompletion;
import org.uimshowdown.bingo.models.RecordSubmission;
import org.uimshowdown.bingo.models.Submission;
import org.uimshowdown.bingo.repositories.CollectionLogCompletionRepository;
import org.uimshowdown.bingo.repositories.PlayerPointsChallengeCompletionRepository;
import org.uimshowdown.bingo.repositories.PlayerSpeedChallengeCompletionRepository;
import org.uimshowdown.bingo.repositories.PlayerRepository;
import org.uimshowdown.bingo.repositories.RecordCompletionRepository;
import org.uimshowdown.bingo.repositories.SubmissionRepository;
import org.uimshowdown.bingo.repositories.TeamRepository;

@Component
public class SubmissionApprovalService {
    
    @Autowired SubmissionRepository submissionRepository;
    @Autowired PlayerRepository playerRepository;
    @Autowired TeamRepository teamRepository;
    @Autowired PlayerSpeedChallengeCompletionRepository playerSpeedChallengeCompletionRepository;
    @Autowired PlayerPointsChallengeCompletionRepository playerPointsChallengeCompletionRepository;
    @Autowired RecordCompletionRepository recordCompletionRepository;
    @Autowired CollectionLogCompletionRepository collectionLogCompletionRepository;
    
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
        if(submission instanceof ContributionSubmission) {
            processContributionSubmission((ContributionSubmission) submission, false);
        }
        if(submission instanceof ContributionIncrementSubmission) {
            processContributionIncrementSubmission((ContributionIncrementSubmission) submission);
        }
        if(submission instanceof ContributionPurchaseSubmission) {
            processContributionPurchaseSubmission((ContributionPurchaseSubmission) submission);
        }
        if(submission instanceof SpeedChallengeSubmission) {
            processSpeedChallengeSubmission((SpeedChallengeSubmission) submission, false);
        }
        if(submission instanceof PointsChallengeSubmission) {
            processPointsChallengeSubmission((PointsChallengeSubmission) submission, false);
        }
        if(submission instanceof RecordSubmission) {
            processRecordSubmission((RecordSubmission) submission, false);
        }
        if(submission instanceof CollectionLogSubmission) {
            processCollectionLogSubmission((CollectionLogSubmission) submission);
        }
        
        submission.setSubmissionState(Submission.State.APPROVED);
        submission.setReviewedAt(new Timestamp(new Date().getTime()));
        submission.setReviewer(reviewer);
        return submissionRepository.save(submission);
    }
    
    /**
     * Changes the state of the submission with the given ID to open, and if it was previously approved, undoes 
     * the effect of the approval
     * @param id
     * @throws Exception 
     */
    public Submission undoDecision(int id) throws Exception {
        Submission submission = submissionRepository.findById(id).orElse(null);
        if(submission == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Submission not found: " + id);
        }
        Submission.State oldState = submission.getState();
        if(oldState == Submission.State.OPEN) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Submission is already open");
        }
        if(oldState == Submission.State.APPROVED) {            
            if(submission instanceof ContributionSubmission) {
                undoContributionApproval((ContributionSubmission) submission);
            }
            if(submission instanceof ContributionIncrementSubmission) {
                undoContributionIncrementApproval((ContributionIncrementSubmission) submission);
            }
            if(submission instanceof ContributionPurchaseSubmission) {
                undoContributionPurchaseApproval((ContributionPurchaseSubmission) submission);
            }
            if(submission instanceof SpeedChallengeSubmission) {
                undoSpeedChallengeApproval((SpeedChallengeSubmission) submission);
            }
            if(submission instanceof PointsChallengeSubmission) {
                undoPointsChallengeApproval((PointsChallengeSubmission) submission);
            }
            if(submission instanceof RecordSubmission) {
                undoRecordApproval((RecordSubmission) submission);
            }
            if(submission instanceof CollectionLogSubmission) {
                undoCollectionLogApproval((CollectionLogSubmission) submission);
            }
        }
        
        submission = submissionRepository.findById(id).orElse(null); // In case it was modified by a preremove method
        submission.setState(Submission.State.OPEN);
        submission.setReviewer(null);
        submission.setReviewedAt(null);
        return submissionRepository.save(submission);
    }
    
    /**
     * Updates the player's contribution to have the new value
     * @param submission
     * @param overwriteBetterValue If false, will silently do nothing if the existing final value of the contribution is better than the value in the submission
     * @throws Exception
     */
    private void processContributionSubmission(ContributionSubmission submission, boolean overwriteBetterValue) throws Exception {
        Player player = submission.getPlayer();
        Contribution contribution = player.getContribution(submission.getContributionMethod());
        if(contribution.getContributionMethod().equals(submission.getContributionMethod())) {
            if(!overwriteBetterValue && contribution.getFinalValue() > submission.getValue()) { // Submissions were approved out of order and this is an out-of-date one
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
        }
        playerRepository.save(player);
    }
    
    /**
     * Updates the player's contribution to increment the final value by the amount in the submission
     * @param submission
     * @throws Exception
     */
    private void processContributionIncrementSubmission(ContributionIncrementSubmission submission) throws Exception {
        Player player = submission.getPlayer();
        Contribution contribution = player.getContribution(submission.getContributionMethod());
        if(contribution.getContributionMethod().equals(submission.getContributionMethod())) {
            if(contribution.isEmpty()) {
                contribution.setInitialValue(0);
                contribution.setFinalValue(0);
                contribution.setIsEmpty(false);
                if(submission.getScreenshotUrls().size() > 0) {
                    contribution.setInitialValueScreenshotUrl(submission.getScreenshotUrls().get(0));
                }
            }
            contribution.setFinalValue(contribution.getFinalValue() + submission.getAmount());
            if(submission.getScreenshotUrls().size() > 0) {
                contribution.setFinalValueScreenshotUrl(submission.getScreenshotUrls().get(0));
            }
        }
        playerRepository.save(player);
    }
    
    /**
     * Updates the player's contribution to decrease the final value by the amount in the submission, and increase the purchase amount by the same amount
     * @param submission
     * @throws Exception
     */
    private void processContributionPurchaseSubmission(ContributionPurchaseSubmission submission) throws Exception {
        Player player = submission.getPlayer();
        Contribution contribution = player.getContribution(submission.getContributionMethod());
        if(contribution.getContributionMethod().equals(submission.getContributionMethod())) {
            contribution.setPurchaseAmount(contribution.getPurchaseAmount() + submission.getAmount());
            contribution.setFinalValue(contribution.getFinalValue() - submission.getAmount());
        }
        playerRepository.save(player);
    }
    
    /**
     * Adds a challenge completion to the player or replaces one, and does the same for the team's challenge completion
     * @param submission
     * @param overwriteBetterValue If false, will silently do nothing if the existing time of the completion is better than the time in the submission
     * @throws Exception
     */
    private void processSpeedChallengeSubmission(SpeedChallengeSubmission submission, boolean overwriteBetterValue) throws Exception {
        Player player = submission.getPlayer();
        
        // Remove the old player challenge completion if it exists
        PlayerSpeedChallengeCompletion existingPlayerSpeedChallengeCompletion = null;
        for(PlayerSpeedChallengeCompletion completion : player.getPlayerSpeedChallengeCompletions()) {
            boolean sameChallenge = completion.getChallenge().equals(submission.getChallenge());
            boolean sameComponent = completion.getChallengeRelayComponent() == null && submission.getRelayComponent() == null
                    || (completion.getChallengeRelayComponent() != null && completion.getChallengeRelayComponent().equals(submission.getRelayComponent()));
            if(sameChallenge && sameComponent) {
                existingPlayerSpeedChallengeCompletion = completion;
                if(!overwriteBetterValue && existingPlayerSpeedChallengeCompletion.getSeconds() < submission.getSeconds()) { // Submissions were approved out of order and this is an out-of-date one
                    return;
                }
                break;
            }
        }
        if(existingPlayerSpeedChallengeCompletion != null) { // Update the existing one
            if(submission.getScreenshotUrls().size() > 0) {
                existingPlayerSpeedChallengeCompletion.setScreenshotUrl(submission.getScreenshotUrls().get(0));
            }
            existingPlayerSpeedChallengeCompletion.setSeconds(submission.getSeconds());
            playerRepository.save(player);
            return;
        }
        
        // Old one didn't exist, so make a new one
        PlayerSpeedChallengeCompletion playerCompletion = new PlayerSpeedChallengeCompletion();
        playerCompletion.setChallenge(submission.getChallenge());
        playerCompletion.setPlayer(player);
        playerCompletion.setRelayComponent(submission.getRelayComponent());
        playerCompletion.setSubmission(submission);
        if(submission.getScreenshotUrls().size() > 0) {
            playerCompletion.setScreenshotUrl(submission.getScreenshotUrls().get(0));
        }
        playerCompletion.setSeconds(submission.getSeconds());
        player.getPlayerSpeedChallengeCompletions().add(playerCompletion);
        playerRepository.save(player);
    }
    
    /**
     * Adds a challenge completion to the player or replaces one, and does the same for the team's challenge completion
     * @param submission
     * @param overwriteBetterValue If false, will silently do nothing if the existing points of the completion is better than the points in the submission
     * @throws Exception
     */
    private void processPointsChallengeSubmission(PointsChallengeSubmission submission, boolean overwriteBetterValue) throws Exception {
        Player player = submission.getPlayer();
        
        // Remove the old player challenge completion if it exists
        PlayerPointsChallengeCompletion existingPlayerPointsChallengeCompletion = null;
        for(PlayerPointsChallengeCompletion completion : player.getPlayerPointsChallengeCompletions()) {
            if(completion.getChallenge().equals(submission.getChallenge())) {
                existingPlayerPointsChallengeCompletion = completion;
                if(!overwriteBetterValue && existingPlayerPointsChallengeCompletion.getPoints() > submission.getPoints()) { // Submissions were approved out of order and this is an out-of-date one
                    return;
                }
                break;
            }
        }
        if(existingPlayerPointsChallengeCompletion != null) { // Update the existing one
            if(submission.getScreenshotUrls().size() > 0) {
                existingPlayerPointsChallengeCompletion.setScreenshotUrl(submission.getScreenshotUrls().get(0));
            }
            existingPlayerPointsChallengeCompletion.setPoints(submission.getPoints());
            playerRepository.save(player);
            return;
        }
        
        // Old one didn't exist, so make a new one
        PlayerPointsChallengeCompletion playerCompletion = new PlayerPointsChallengeCompletion();
        playerCompletion.setChallenge(submission.getChallenge());
        playerCompletion.setPlayer(player);
        playerCompletion.setSubmission(submission);
        if(submission.getScreenshotUrls().size() > 0) {
            playerCompletion.setScreenshotUrl(submission.getScreenshotUrls().get(0));
        }
        playerCompletion.setPoints(submission.getPoints());
        player.getPlayerPointsChallengeCompletions().add(playerCompletion);
        playerRepository.save(player);
    }
    
    /**
     * Adds a record completion to the player or replaces an existing one
     * @param submission
     * @param overwriteBetterValue If false, will silently do nothing if the existing value of the contribution is better than the value in the submission
     * @throws Exception
     */
    private void processRecordSubmission(RecordSubmission submission, boolean overwriteBetterValue) throws Exception {
        Player player = submission.getPlayer();
        RecordCompletion existingCompletion = null;
        for(RecordCompletion completion : player.getRecordCompletions()) {
            if(completion.getRecord().equals(submission.getRecord())) {
                existingCompletion = completion;
                if(!overwriteBetterValue && existingCompletion.getValue() > submission.getValue()) { // Submissions were approved out of order and this is an out-of-date one
                    return;
                }
                break;
            }
        }
        
        if(existingCompletion != null) { // Update the existing one
            existingCompletion.setHandicap(submission.getHandicap());
            existingCompletion.setRawValue(submission.getRawValue());
            existingCompletion.setVideoUrl(submission.getVideoUrl());
            playerRepository.save(player);
            return;
        }
        
        // Old one didn't exist, so make a new one
        RecordCompletion completion = new RecordCompletion();
        completion.setHandicap(submission.getHandicap());
        completion.setPlayer(submission.getPlayer());
        completion.setRawValue(submission.getRawValue());
        completion.setRecord(submission.getRecord());
        completion.setVideoUrl(submission.getVideoUrl());
        completion.setSubmission(submission);
        player.getRecordCompletions().add(completion);
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
        completion.setSubmission(submission);
        player.getCollectionLogCompletions().add(completion);
        playerRepository.save(player);
    }
    
    private void undoContributionApproval(ContributionSubmission submission) throws Exception {
        Player player = submission.getPlayer();
        ContributionMethod method = submission.getContributionMethod();
        Contribution contribution = player.getContribution(method);
        Submission previousSubmission = submissionRepository.getPreviousContributionSubmission(player.getId(), method.getId(), submission.getId()).orElse(null);
        if(previousSubmission == null) {
            contribution.setIsEmpty(true);
            contribution.setInitialValue(0);
            contribution.setFinalValue(0);
            contribution.setInitialValueScreenshotUrl(null);
            contribution.setFinalValueScreenshotUrl(null);
            playerRepository.save(player);
        } else {
            processContributionSubmission((ContributionSubmission) previousSubmission, true);
        }
    }
    
    private void undoContributionIncrementApproval(ContributionIncrementSubmission submission) throws Exception {
        Player player = submission.getPlayer();
        ContributionMethod method = submission.getContributionMethod();
        Contribution contribution = player.getContribution(method);
        Submission previousSubmission = submissionRepository.getPreviousContributionIncrementSubmission(player.getId(), method.getId(), submission.getId()).orElse(null);
        if(previousSubmission == null) {
            contribution.setIsEmpty(true);
            contribution.setInitialValue(0);
            contribution.setFinalValue(0);
            contribution.setInitialValueScreenshotUrl(null);
            contribution.setFinalValueScreenshotUrl(null);
            playerRepository.save(player);
        } else {
            contribution.setFinalValue(contribution.getFinalValue() - submission.getAmount());
            contribution.setFinalValueScreenshotUrl(previousSubmission.getScreenshotUrls().get(0));
            playerRepository.save(player);
        }
    }
    
    private void undoContributionPurchaseApproval(ContributionPurchaseSubmission submission) throws Exception {
        Player player = submission.getPlayer();
        ContributionMethod method = submission.getContributionMethod();
        Contribution contribution = player.getContribution(method);
        contribution.setFinalValue(contribution.getFinalValue() + submission.getAmount());
        contribution.setPurchaseAmount(contribution.getPurchaseAmount() - submission.getAmount());
        playerRepository.save(player);
    }
    
    private void undoSpeedChallengeApproval(SpeedChallengeSubmission submission) throws Exception {
        Player player = submission.getPlayer();
        Challenge challenge = submission.getChallenge();
        Submission previousSubmission = null;
        if(challenge.getType() == Challenge.Type.SPEEDRUN) {
            previousSubmission = submissionRepository.getPreviousSpeedrunChallengeSubmission(player.getId(), challenge.getId(), submission.getId()).orElse(null);
        } else {            
            ChallengeRelayComponent relayComponent = submission.getRelayComponent();
            previousSubmission = submissionRepository.getPreviousRelayChallengeSubmission(player.getId(), challenge.getId(), relayComponent.getId(), submission.getId()).orElse(null);
        }
        if(previousSubmission == null) {
            playerSpeedChallengeCompletionRepository.deleteById(submission.getCompletion().getId());
        } else {
            processSpeedChallengeSubmission((SpeedChallengeSubmission) previousSubmission, true);
        }
    }
    
    private void undoPointsChallengeApproval(PointsChallengeSubmission submission) throws Exception {
        Player player = submission.getPlayer();
        Challenge challenge = submission.getChallenge();
        Submission previousSubmission = null;
        previousSubmission = submissionRepository.getPreviousPointsChallengeSubmission(player.getId(), challenge.getId(), submission.getId()).orElse(null);
        if(previousSubmission == null) {
            playerPointsChallengeCompletionRepository.deleteById(submission.getCompletion().getId());
        } else {
            processPointsChallengeSubmission((PointsChallengeSubmission) previousSubmission, true);
        }
    }
    
    private void undoRecordApproval(RecordSubmission submission) throws Exception {
        Player player = submission.getPlayer();
        Record record = submission.getRecord();
        Submission previousSubmission = null;
        previousSubmission = submissionRepository.getPreviousRecordSubmission(player.getId(), record.getId(), submission.getId()).orElse(null);
        if(previousSubmission == null) {
            recordCompletionRepository.deleteById(submission.getCompletion().getId());
        } else {
            processRecordSubmission((RecordSubmission) previousSubmission, true);
        }
    }
    
    private void undoCollectionLogApproval(CollectionLogSubmission submission) {
        collectionLogCompletionRepository.deleteById(submission.getCompletion().getId());
    }

}
