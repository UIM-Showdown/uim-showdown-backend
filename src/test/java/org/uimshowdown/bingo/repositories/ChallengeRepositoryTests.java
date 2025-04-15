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

@SpringBootTest
@Tag(TestTag.INTEGRATION_TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChallengeRepositoryTests {
    
    @Autowired
    private TestUtils testUtils;
    
    @Autowired
    private ChallengeRepository challengeRepository;

    private Challenge testChallenge;

    @BeforeAll
    public void setUp() {
        testUtils.resetDB();
        testChallenge = challengeRepository.save(SharedTestVariables.makeTestChallenge());
    }

    @AfterAll
    public void tearDown() {
        testUtils.resetDB();
    }

    @Test
    @Transactional
    public void Should_FindTestChallenge_When_GivenTestChallengeName() {
        Challenge challenge = challengeRepository.findByName(testChallenge.getName()).orElse(null);

        assertThat(challenge)
            .isNotNull()
            .isEqualTo(testChallenge);
    }

    @Test
    @Transactional
    public void Should_NotFindTestChallenge_When_GivenWrongChallengeName() {
        Challenge challenge = challengeRepository.findByName("Not Likely To Be Found").orElse(null);

        assertThat(challenge).isNull();
    }
    
}
