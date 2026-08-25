package org.uimshowdown.bingo.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.uimshowdown.bingo.TestUtils;
import org.uimshowdown.bingo.constants.TestTag;
import org.uimshowdown.bingo.models.Team;

@SpringBootTest
@Tag(TestTag.INTEGRATION_TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class TeamRepositoryTests {
    
    @Autowired
    private TestUtils testUtils;
    
    @Autowired
    private TeamRepository teamRepository;

    private Team testTeam;

    @BeforeAll
    public void setUp() {
        testUtils.resetDB();
        testTeam = teamRepository.save(SharedTestVariables.makeTestTeam());
    }

    @AfterAll
    public void tearDown() {
        testUtils.resetDB();
    }

    @Test
    @Transactional
    public void Should_FindTestTeam_When_GivenTestTeamName() {
        Team team = teamRepository.findByName(testTeam.getName()).orElse(null);

        assertThat(team)
            .isNotNull()
            .isEqualTo(testTeam);
    }

    @Test
    @Transactional
    public void Should_NotFindTestTeam_When_GivenWrongTestName() {
        Team team = teamRepository.findByName("Unlikely To Be Found").orElse(null);

        assertThat(team).isNull();
    }
    
}
