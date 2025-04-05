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
import org.uimshowdown.bingo.models.Team;
import org.uimshowdown.bingo.models.TeamScoreboard;

@SpringBootTest
@Tag(TestTag.INTEGRATION_TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TeamScoreboardRepositoryTests {
	
	@Autowired
	private TestUtils testUtils;
	
    @Autowired
    private TeamScoreboardRepository teamScoreboardRepository;

    @Autowired
    private TeamRepository teamRepository;

    private Team testTeam;
    private TeamScoreboard testTeamScoreboard;
    
    @BeforeAll
    public void setUp() {
    	testUtils.resetDB();
        testTeam = teamRepository.save(SharedTestVariables.makeTestTeam());
        testTeamScoreboard = teamScoreboardRepository.save(SharedTestVariables.makeTestTeamScoreboard(testTeam));
    }

    @AfterAll
    public void tearDown() {
    	testUtils.resetDB();
    }

    @Test
    @Transactional
    public void Should_FindTestTeamScoreboard_When_GivenTestTeamId() {
        TeamScoreboard teamScoreboard = teamScoreboardRepository.findByTeamId(testTeam.getId()).orElse(null);

        assertThat(teamScoreboard)
            .isNotNull()
            .isEqualTo(testTeamScoreboard);
    }

    @Test
    @Transactional
    public void Should_NotFindTestTeamScoreboard_When_GivenWrongTeamId() {
        TeamScoreboard teamScoreboard = teamScoreboardRepository.findByTeamId(0).orElse(null);

        assertThat(teamScoreboard)
            .isNull();
    }
    
}
