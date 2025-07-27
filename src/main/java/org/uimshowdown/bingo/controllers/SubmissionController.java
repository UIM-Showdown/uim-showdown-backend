package org.uimshowdown.bingo.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.uimshowdown.bingo.models.Challenge;
import org.uimshowdown.bingo.models.ChallengeRelayComponent;
import org.uimshowdown.bingo.models.ChallengeSubmission;
import org.uimshowdown.bingo.models.CollectionLogItem;
import org.uimshowdown.bingo.models.CollectionLogSubmission;
import org.uimshowdown.bingo.models.ContributionMethod;
import org.uimshowdown.bingo.models.ContributionSubmission;
import org.uimshowdown.bingo.models.ItemOption;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.Record;
import org.uimshowdown.bingo.models.RecordHandicap;
import org.uimshowdown.bingo.models.RecordSubmission;
import org.uimshowdown.bingo.models.Submission;
import org.uimshowdown.bingo.models.UnrankedStartingValueSubmission;
import org.uimshowdown.bingo.repositories.ChallengeRelayComponentRepository;
import org.uimshowdown.bingo.repositories.ChallengeRepository;
import org.uimshowdown.bingo.repositories.CollectionLogItemRepository;
import org.uimshowdown.bingo.repositories.ContributionMethodRepository;
import org.uimshowdown.bingo.repositories.PlayerRepository;
import org.uimshowdown.bingo.repositories.RecordHandicapRepository;
import org.uimshowdown.bingo.repositories.RecordRepository;
import org.uimshowdown.bingo.repositories.SubmissionRepository;
import org.uimshowdown.bingo.services.SubmissionApprovalService;

@RestController
public class SubmissionController {
    
    @Autowired
    private SubmissionRepository submissionRepository;
    
    @Autowired
    private PlayerRepository playerRepository;
    
    @Autowired
    private ContributionMethodRepository contributionMethodRepository;
    
    @Autowired
    private ChallengeRepository challengeRepository;
    
    @Autowired
    private ChallengeRelayComponentRepository challengeRelayComponentRepository;
    
    @Autowired
    private RecordRepository recordRepository;
    
    @Autowired
    private RecordHandicapRepository recordHandicapRepository;
    
    @Autowired
    private CollectionLogItemRepository collectionLogItemRepository;
    
    @Autowired
    private SubmissionApprovalService submissionApprovalService;
    
    @SuppressWarnings("unchecked")
    @PostMapping("/submissions/contribution")
    public Map<String, Object> createContributionSubmission(@RequestBody Map<String, Object> requestBody) {
        Player player = playerRepository.findByRsn((String) requestBody.get("rsn")).orElse(null);
        ContributionMethod contributionMethod = contributionMethodRepository.findByName((String) requestBody.get("methodName")).orElse(null);
        if(player == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found: " + (String) requestBody.get("rsn"));
        }
        if(contributionMethod == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contribution not found: " + (String) requestBody.get("methodName"));
        }
        ContributionMethod.Type type = contributionMethod.getContributionMethodType();
        if(type != ContributionMethod.Type.SUBMISSION_KC && type != ContributionMethod.Type.SUBMISSION_ITEM_DROP && type != ContributionMethod.Type.SUBMISSION_OTHER) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contribution method is not a submission-based method: " + (String) requestBody.get("methodName"));
        }
        
        ContributionSubmission submission = new ContributionSubmission();
        submission.setContributionMethod(contributionMethod);
        submission.setPlayer(player);
        submission.setSubmissionState(Submission.State.OPEN);
        submission.setValue((int) requestBody.get("value"));
        submission.setType(Submission.Type.CONTRIBUTION);
        submission.setDescription((String) requestBody.get("description"));
        submission.setScreenshotUrls((List<String>) requestBody.get("screenshotURLs"));
        
        Submission returnedSubmission = submissionRepository.save(submission);
        
        Map<String, Object> responseBody = new HashMap<String, Object>();
        responseBody.put("id", returnedSubmission.getId());
        return responseBody;
    }
    
    @SuppressWarnings("unchecked")
    @PostMapping("/submissions/challenge")
    public Map<String, Object> createChallengeSubmission(@RequestBody Map<String, Object> requestBody) {
        Player player = playerRepository.findByRsn((String) requestBody.get("rsn")).get();
        Challenge challenge = challengeRepository.findByName((String) requestBody.get("challengeName")).get();
        ChallengeRelayComponent relayComponent = null;
        if((String) requestBody.get("relayComponentName") != null) {
            relayComponent = challengeRelayComponentRepository.findByName((String) requestBody.get("relayComponentName")).get();
        }
        
        ChallengeSubmission submission = new ChallengeSubmission();
        submission.setChallenge(challenge);
        submission.setPlayer(player);
        submission.setSubmissionState(Submission.State.OPEN);
        submission.setSeconds((double) requestBody.get("seconds"));
        submission.setType(Submission.Type.CHALLENGE);
        submission.setDescription((String) requestBody.get("description"));
        if(relayComponent != null) {
            submission.setRelayComponent(relayComponent);
        }
        submission.setScreenshotUrls((List<String>) requestBody.get("screenshotURLs"));
        
        Submission returnedSubmission = submissionRepository.save(submission);
        
        Map<String, Object> responseBody = new HashMap<String, Object>();
        responseBody.put("id", returnedSubmission.getId());
        return responseBody;
    }
    
    @PostMapping("/submissions/record")
    public Map<String, Object> createRecordSubmission(@RequestBody Map<String, Object> requestBody) {
        Player player = playerRepository.findByRsn((String) requestBody.get("rsn")).get();
        Record record = recordRepository.findBySkill(Player.Skill.valueOf((String) requestBody.get("skill"))).get();
        RecordHandicap handicap = null;
        if((String) requestBody.get("handicapName") != null) {
            handicap = recordHandicapRepository.findByName((String) requestBody.get("handicapName")).get();
        }
        
        RecordSubmission submission = new RecordSubmission();
        submission.setRecord(record);
        submission.setPlayer(player);
        submission.setSubmissionState(Submission.State.OPEN);
        submission.setRawValue((int) requestBody.get("rawValue"));
        submission.setType(Submission.Type.RECORD);
        submission.setVideoURL((String) requestBody.get("videoUrl"));
        submission.setDescription((String) requestBody.get("description"));
        if(handicap != null) {
            submission.setHandicap(handicap);
        }
        submission.setScreenshotUrls(new ArrayList<String>());
        
        Submission returnedSubmission = submissionRepository.save(submission);
        
        Map<String, Object> responseBody = new HashMap<String, Object>();
        responseBody.put("id", returnedSubmission.getId());
        return responseBody;
    }
    
    @SuppressWarnings("unchecked")
    @PostMapping("/submissions/collectionlog")
    public Map<String, Object> createCollectionLogSubmission(@RequestBody Map<String, Object> requestBody) {
        Player player = playerRepository.findByRsn((String) requestBody.get("rsn")).get();
        String itemName = (String) requestBody.get("itemName");
        CollectionLogItem item = collectionLogItemRepository.findByNameOrOption(itemName).get();
        ItemOption itemOption = null;
        if(item.getItemOptionNames() != null && item.getItemOptionNames().contains(itemName)) {
            List<ItemOption> itemOptions = item.getItemOptions();
            for(ItemOption option : itemOptions) {
                if(option.getName().equals(itemName)) {
                    itemOption = option;
                }
            }
        }
        
        CollectionLogSubmission submission = new CollectionLogSubmission();
        submission.setItem(item);
        submission.setPlayer(player);
        submission.setSubmissionState(Submission.State.OPEN);
        submission.setType(Submission.Type.COLLECTION_LOG);
        submission.setDescription((String) requestBody.get("description"));
        if(itemOption != null) {
            submission.setItemOption(itemOption);
        }
        submission.setScreenshotUrls((List<String>) requestBody.get("screenshotURLs"));
        
        Submission returnedSubmission = submissionRepository.save(submission);
        
        Map<String, Object> responseBody = new HashMap<String, Object>();
        responseBody.put("id", returnedSubmission.getId());
        return responseBody;
    }
    
    @SuppressWarnings("unchecked")
    @PostMapping("/submissions/unrankedstartingvalue")
    public Map<String, Object> createUnrankedStartingValueSubmission(@RequestBody Map<String, Object> requestBody) {
        Player player = playerRepository.findByRsn((String) requestBody.get("rsn")).orElse(null);
        ContributionMethod contributionMethod = contributionMethodRepository.findByName((String) requestBody.get("methodName")).orElse(null);
        if(player == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found: " + (String) requestBody.get("rsn"));
        }
        if(contributionMethod == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contribution not found: " + (String) requestBody.get("methodName"));
        }
        if(contributionMethod.getContributionMethodType() != ContributionMethod.Type.TEMPLE_KC || contributionMethod.getTempleId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contribution method is not a Temple-tracked KC method: " + (String) requestBody.get("methodName"));
        }
        
        UnrankedStartingValueSubmission submission = new UnrankedStartingValueSubmission();
        submission.setContributionMethod(contributionMethod);
        submission.setPlayer(player);
        submission.setSubmissionState(Submission.State.OPEN);
        submission.setValue((int) requestBody.get("value"));
        submission.setType(Submission.Type.UNRANKED_STARTING_VALUE);
        submission.setDescription((String) requestBody.get("description"));
        submission.setScreenshotUrls((List<String>) requestBody.get("screenshotURLs"));
        
        Submission returnedSubmission = submissionRepository.save(submission);
        
        Map<String, Object> responseBody = new HashMap<String, Object>();
        responseBody.put("id", returnedSubmission.getId());
        return responseBody;
    }
    
    @PatchMapping("/submissions/{id}")
    public Map<String, Object> approveOrDenySubmission(@PathVariable int id, @RequestBody Map<String, String> requestBody) throws Exception {
        Submission.State state = Submission.State.valueOf(requestBody.get("state"));
        if(state == Submission.State.OPEN) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot re-open submission");
        }
        if(state == Submission.State.DENIED) {
            Submission submission = submissionApprovalService.denySubmission(id, requestBody.get("reviewer"));
            Map<String, Object> response = new HashMap<String, Object>();
            response.put("id", submission.getId());
            response.put("description", submission.getDescription());
            response.put("state", submission.getState());
            return response;
        }
        if(state == Submission.State.APPROVED) {
            Submission submission = submissionApprovalService.approveSubmission(id, requestBody.get("reviewer"));
            Map<String, Object> response = new HashMap<String, Object>();
            response.put("id", submission.getId());
            response.put("description", submission.getDescription());
            response.put("state", submission.getState());
            return response;
        }
        return null;
    }
    
    @PatchMapping("/submissions/{id}/undo")
    public Map<String, Object> undoDecision(@PathVariable int id) throws Exception {
        Submission submission = submissionApprovalService.undoDecision(id);
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("id", submission.getId());
        response.put("description", submission.getDescription());
        response.put("state", submission.getState());
        return response;
    }

}
