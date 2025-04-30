package org.uimshowdown.bingo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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
import org.uimshowdown.bingo.repositories.SubmissionScreenshotUrlRepository;
import org.uimshowdown.bingo.repositories.TeamRepository;
import org.uimshowdown.bingo.repositories.TeamScoreboardRepository;
import org.uimshowdown.bingo.repositories.TileProgressRepository;
import org.uimshowdown.bingo.repositories.TileRepository;

@Component
public class TestUtils {
    
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
    @Autowired SubmissionScreenshotUrlRepository submissionScreenshotUrlRepository;
    @Autowired TeamRepository teamRepository;
    @Autowired TeamScoreboardRepository teamScoreboardRepository;
    @Autowired TileProgressRepository tileProgressRepository;
    @Autowired TileRepository tileRepository;
    @Autowired ItemOptionRepository itemOptionRepository;
    
    @Transactional
    public void resetDB() {
        challengeCompletionRepository.deleteAll();
        challengeRelayComponentRepository.deleteAll();
        collectionLogCompletionRepository.deleteAll();
        contributionRepository.deleteAll();
        playerChallengeCompletionRepository.deleteAll();
        playerRepository.deleteAll();
        playerScoreboardRepository.deleteAll();
        recordCompletionRepository.deleteAll();
        recordHandicapRepository.deleteAll();
        submissionRepository.deleteAll();
        submissionScreenshotUrlRepository.deleteAll();
        teamRepository.deleteAll();
        teamScoreboardRepository.deleteAll();
        tileProgressRepository.deleteAll();
        tileRepository.deleteAll();
        itemOptionRepository.deleteAll();
        challengeRepository.deleteAll();
        contributionMethodRepository.deleteAll();
        recordRepository.deleteAll();
        collectionLogItemRepository.deleteAll();
    }

}
