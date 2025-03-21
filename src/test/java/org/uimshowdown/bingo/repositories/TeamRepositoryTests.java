package org.uimshowdown.bingo.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.uimshowdown.bingo.constants.TestTag;
import org.uimshowdown.bingo.models.Team;

@SpringBootTest
@Tag(TestTag.INTEGRATION_TEST)
public class TeamRepositoryTests {
    @Autowired
    private TeamRepository teamRepository;

    private Team testTeam;

    @BeforeEach
    public void setUp() {
        testTeam = teamRepository.save(SharedTestVariables.makeTestTeam());
    }

    @AfterEach
    public void tearDown() {
        teamRepository.delete(testTeam);
    }

    @Test
    @Transactional
    public void Should_FindTestTeam_When_GivenTestTeamName() {
        Team team = teamRepository.findByName(testTeam.getName()).orElse(new Team());

        assertThat(team)
            .isNotNull()
            .isEqualTo(testTeam);
    }

    @Test
    @Transactional
    public void Should_NotFindTestTeam_When_GivenWrongTestName() {
        Team team = teamRepository.findByName("Unlikely To Be Found").orElse(new Team());

        assertThat(team)
            .isNotNull()
            .isNotEqualTo(testTeam);
    }
}
