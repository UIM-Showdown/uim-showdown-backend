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
import org.uimshowdown.bingo.constants.TestTag;
import org.uimshowdown.bingo.models.Challenge;
import org.uimshowdown.bingo.models.ChallengeRelayComponent;

@SpringBootTest
@Tag(TestTag.INTEGRATION_TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChallengeRelayComponentRepositoryTests {
    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private ChallengeRelayComponentRepository challengeRelayComponentRepository;

    private Challenge testChallenge;
    private ChallengeRelayComponent testChallengeRelayComponent;

    @BeforeAll
    public void setUp() {
    	challengeRepository.deleteAll();
    	challengeRelayComponentRepository.deleteAll();
        testChallenge = challengeRepository.save(SharedTestVariables.makeTestChallenge());
        testChallengeRelayComponent = challengeRelayComponentRepository.save(SharedTestVariables.makeTestChallengeRelayComponent(testChallenge));
    }

    @AfterAll
    public void tearDown() {
        challengeRelayComponentRepository.delete(testChallengeRelayComponent);
        challengeRepository.delete(testChallenge);
    }

    @Test
    @Transactional
    public void Should_FindTestChallengeRelayComponent_When_GivenTestChallengeId() {
        Iterable<ChallengeRelayComponent> challengeRelayComponents = challengeRelayComponentRepository.findAllByChallengeId(testChallenge.getId());

        assertThat(challengeRelayComponents)
            .isNotNull()
            .isNotEmpty()
            .contains(testChallengeRelayComponent);
    }

    @Test
    @Transactional
    public void Should_NotFindTestChallengeRelayComponent_When_GivenWrongChallengeId() {
        Iterable<ChallengeRelayComponent> challengeRelayComponents = challengeRelayComponentRepository.findAllByChallengeId(0);

        assertThat(challengeRelayComponents)
            .isNotNull()
            .doesNotContain(testChallengeRelayComponent);
    }
}
