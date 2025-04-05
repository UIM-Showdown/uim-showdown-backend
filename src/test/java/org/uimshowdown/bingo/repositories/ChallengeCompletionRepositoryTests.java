package org.uimshowdown.bingo.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.uimshowdown.bingo.TestUtils;
import org.uimshowdown.bingo.constants.TestTag;
import org.uimshowdown.bingo.models.Challenge;
import org.uimshowdown.bingo.models.ChallengeCompletion;
import org.uimshowdown.bingo.models.Team;

@SpringBootTest
@Tag(TestTag.INTEGRATION_TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChallengeCompletionRepositoryTests {
	
	@Autowired
	private TestUtils testUtils;
	
    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private ChallengeCompletionRepository challengeCompletionRepository;

    @Autowired
    private TeamRepository teamRepository;

    private Challenge testChallenge;
    private ChallengeCompletion testChallengeCompletion;
    private Team testTeam;

    @BeforeAll
    public void setUp() {
        testUtils.resetDB();
        testTeam = teamRepository.save(SharedTestVariables.makeTestTeam());
        testChallenge = challengeRepository.save(SharedTestVariables.makeTestChallenge());
        testChallengeCompletion = challengeCompletionRepository.save(SharedTestVariables.makeTestChallengeCompletion(testChallenge, testTeam));
    }

    @AfterAll
    public void tearDown() {
    	testUtils.resetDB();
    }

    @Test
    @Transactional
    public void Should_FindTestChallengeCompletion_When_GivenTestChallengeId() {
        Iterable<ChallengeCompletion> challengeCompletions = challengeCompletionRepository.findAllByChallengeId(testChallenge.getId());

        assertThat(challengeCompletions)
            .isNotNull()
            .isNotEmpty()
            .contains(testChallengeCompletion);
    }

    @Test
    @Transactional
    public void Should_NotFindTestChallengeCompletion_When_GivenWrongChallengeId() {
        Iterable<ChallengeCompletion> challengeCompletions = challengeCompletionRepository.findAllByChallengeId(0);

        assertThat(challengeCompletions)
            .isNotNull()
            .doesNotContain(testChallengeCompletion);
    }

    @Test
    @Transactional
    public void Should_FindTestChallengeCompletion_When_GivenTestTeamId() {
        Iterable<ChallengeCompletion> challengeCompletions = challengeCompletionRepository.findAllByTeamId(testTeam.getId());

        assertThat(challengeCompletions)
            .isNotNull()
            .isNotEmpty()
            .contains(testChallengeCompletion);
    }

    @Test
    @Transactional
    public void Should_NotFindTestChallengeCompletion_When_GivenWrongTeamId() {
        Iterable<ChallengeCompletion> challengeCompletions = challengeCompletionRepository.findAllByTeamId(0);

        assertThat(challengeCompletions)
            .isNotNull()
            .doesNotContain(testChallengeCompletion);
    }
    
}
