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
import org.uimshowdown.bingo.models.CollectionLogGroup;
import org.uimshowdown.bingo.models.CollectionLogItem;
import org.uimshowdown.bingo.models.CollectionLogSubmission;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.Team;

@SpringBootTest
@Tag(TestTag.INTEGRATION_TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CollectionLogSubmissionRepositoryTests {
    @Autowired
    private CollectionLogGroupRepository collectionLogGroupRepository;

    @Autowired
    private CollectionLogItemRepository collectionLogItemRepository;

    @Autowired
    private CollectionLogSubmissionRepository collectionLogSubmissionRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    private CollectionLogGroup testCollectionLogGroup;
    private CollectionLogItem testCollectionLogItem;
    private CollectionLogSubmission testCollectionLogSubmission;
    private Player testPlayer;
    private Team testTeam;

    @BeforeAll
    public void setUp() {
    	teamRepository.deleteAll();
    	playerRepository.deleteAll();
    	collectionLogGroupRepository.deleteAll();
    	collectionLogItemRepository.deleteAll();
    	collectionLogSubmissionRepository.deleteAll();
        testTeam = teamRepository.save(SharedTestVariables.makeTestTeam());
        testPlayer = playerRepository.save(SharedTestVariables.makeTestPlayer(testTeam));
        testCollectionLogGroup = collectionLogGroupRepository.save(SharedTestVariables.makeTestCollectionLogChecklistGroup());
        testCollectionLogItem = collectionLogItemRepository.save(SharedTestVariables.makeTestCollectionLogItem(testCollectionLogGroup));
        testCollectionLogSubmission = collectionLogSubmissionRepository.save(SharedTestVariables.makeTestCollectionLogSubmission(testCollectionLogItem, testPlayer));
    }

    @AfterAll
    public void tearDown() {
        collectionLogSubmissionRepository.delete(testCollectionLogSubmission);
        collectionLogItemRepository.delete(testCollectionLogItem);
        collectionLogGroupRepository.delete(testCollectionLogGroup);
        playerRepository.delete(testPlayer);
        teamRepository.delete(testTeam);
    }

    @Test
    @Transactional
    public void Should_FindTestCollectionLogSubmission_When_GivenTestCollectionLogItemId() {
        Iterable<CollectionLogSubmission> collectionLogSubmissions = collectionLogSubmissionRepository.findAllByItemId(testCollectionLogItem.getId());

        assertThat(collectionLogSubmissions)
            .isNotNull()
            .isNotEmpty()
            .contains(testCollectionLogSubmission);
    }

    @Test
    @Transactional
    public void Should_NotFindTestCollectionLogSubmission_When_GivenWrongCollectionLogItemId() {
        Iterable<CollectionLogSubmission> collectionLogSubmissions = collectionLogSubmissionRepository.findAllByItemId(0);

        assertThat(collectionLogSubmissions)
            .isNotNull()
            .doesNotContain(testCollectionLogSubmission);
    }
}
