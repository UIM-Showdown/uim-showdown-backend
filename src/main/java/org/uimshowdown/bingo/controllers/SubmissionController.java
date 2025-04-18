package org.uimshowdown.bingo.controllers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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
import org.uimshowdown.bingo.models.SubmissionScreenshotUrl;
import org.uimshowdown.bingo.models.UnrankedStartingValueSubmission;
import org.uimshowdown.bingo.repositories.ChallengeRelayComponentRepository;
import org.uimshowdown.bingo.repositories.ChallengeRepository;
import org.uimshowdown.bingo.repositories.CollectionLogItemRepository;
import org.uimshowdown.bingo.repositories.ContributionMethodRepository;
import org.uimshowdown.bingo.repositories.PlayerRepository;
import org.uimshowdown.bingo.repositories.RecordHandicapRepository;
import org.uimshowdown.bingo.repositories.RecordRepository;
import org.uimshowdown.bingo.repositories.SubmissionRepository;

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
    
    @SuppressWarnings("unchecked")
    @PostMapping("/submissions/contribution")
    public Map<String, Object> createContributionSubmission(@RequestBody Map<String, Object> requestBody) {
        Player player = playerRepository.findByRsn((String) requestBody.get("rsn")).get();
        ContributionMethod contributionMethod = contributionMethodRepository.findByName((String) requestBody.get("methodName")).get();
        
        ContributionSubmission submission = new ContributionSubmission();
        submission.setContributionMethod(contributionMethod);
        submission.setPlayer(player);
        submission.setSubmissionState(Submission.State.OPEN);
        submission.setValue((int) requestBody.get("value"));
        submission.setType(Submission.Type.CONTRIBUTION);
        
        Set<SubmissionScreenshotUrl> urls = new HashSet<SubmissionScreenshotUrl>();
        for(String url : (List<String>) requestBody.get("screenshotURLs")) {
            SubmissionScreenshotUrl submissionScreenshotUrl = new SubmissionScreenshotUrl();
            submissionScreenshotUrl.setScreenshotUrl(url);
            submissionScreenshotUrl.setSubmission(submission);
            urls.add(submissionScreenshotUrl);
        }
        submission.setScreenshotUrls(urls);
        
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
        if(relayComponent != null) {
            submission.setRelayComponent(relayComponent);
        }
        
        Set<SubmissionScreenshotUrl> urls = new HashSet<SubmissionScreenshotUrl>();
        for(String url : (List<String>) requestBody.get("screenshotURLs")) {
            SubmissionScreenshotUrl submissionScreenshotUrl = new SubmissionScreenshotUrl();
            submissionScreenshotUrl.setScreenshotUrl(url);
            submissionScreenshotUrl.setSubmission(submission);
            urls.add(submissionScreenshotUrl);
        }
        submission.setScreenshotUrls(urls);
        
        Submission returnedSubmission = submissionRepository.save(submission);
        
        Map<String, Object> responseBody = new HashMap<String, Object>();
        responseBody.put("id", returnedSubmission.getId());
        return responseBody;
    }
    
    @SuppressWarnings("unchecked")
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
        submission.setValue((int) requestBody.get("value"));
        submission.setType(Submission.Type.RECORD);
        if(handicap != null) {
            submission.setHandicap(handicap);
        }
        
        Set<SubmissionScreenshotUrl> urls = new HashSet<SubmissionScreenshotUrl>();
        for(String url : (List<String>) requestBody.get("screenshotURLs")) {
            SubmissionScreenshotUrl submissionScreenshotUrl = new SubmissionScreenshotUrl();
            submissionScreenshotUrl.setScreenshotUrl(url);
            submissionScreenshotUrl.setSubmission(submission);
            urls.add(submissionScreenshotUrl);
        }
        submission.setScreenshotUrls(urls);
        
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
            Set<ItemOption> itemOptions = item.getItemOptions();
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
        if(itemOption != null) {
            submission.setItemOption(itemOption);
        }
        
        Set<SubmissionScreenshotUrl> urls = new HashSet<SubmissionScreenshotUrl>();
        for(String url : (List<String>) requestBody.get("screenshotURLs")) {
            SubmissionScreenshotUrl submissionScreenshotUrl = new SubmissionScreenshotUrl();
            submissionScreenshotUrl.setScreenshotUrl(url);
            submissionScreenshotUrl.setSubmission(submission);
            urls.add(submissionScreenshotUrl);
        }
        submission.setScreenshotUrls(urls);
        
        Submission returnedSubmission = submissionRepository.save(submission);
        
        Map<String, Object> responseBody = new HashMap<String, Object>();
        responseBody.put("id", returnedSubmission.getId());
        return responseBody;
    }
    
    @SuppressWarnings("unchecked")
    @PostMapping("/submissions/unrankedstartingid")
    public Map<String, Object> createUnrankedStartingIDSubmission(@RequestBody Map<String, Object> requestBody) {
        Player player = playerRepository.findByRsn((String) requestBody.get("rsn")).get();
        ContributionMethod contributionMethod = contributionMethodRepository.findByName((String) requestBody.get("methodName")).get();
        
        UnrankedStartingValueSubmission submission = new UnrankedStartingValueSubmission();
        submission.setContributionMethod(contributionMethod);
        submission.setPlayer(player);
        submission.setSubmissionState(Submission.State.OPEN);
        submission.setValue((int) requestBody.get("value"));
        submission.setType(Submission.Type.UNRANKED_STARTING_VALUE);
        
        Set<SubmissionScreenshotUrl> urls = new HashSet<SubmissionScreenshotUrl>();
        for(String url : (List<String>) requestBody.get("screenshotURLs")) {
            SubmissionScreenshotUrl submissionScreenshotUrl = new SubmissionScreenshotUrl();
            submissionScreenshotUrl.setScreenshotUrl(url);
            submissionScreenshotUrl.setSubmission(submission);
            urls.add(submissionScreenshotUrl);
        }
        submission.setScreenshotUrls(urls);
        
        Submission returnedSubmission = submissionRepository.save(submission);
        
        Map<String, Object> responseBody = new HashMap<String, Object>();
        responseBody.put("id", returnedSubmission.getId());
        return responseBody;
    }

}
