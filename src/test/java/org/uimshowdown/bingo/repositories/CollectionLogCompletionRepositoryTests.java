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
import org.uimshowdown.bingo.models.CollectionLogCompletion;
import org.uimshowdown.bingo.models.CollectionLogGroup;
import org.uimshowdown.bingo.models.CollectionLogItem;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.Team;

@SpringBootTest
@Tag(TestTag.INTEGRATION_TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CollectionLogCompletionRepositoryTests {
    @Autowired
    private CollectionLogCompletionRepository collectionLogCompletionRepository;

    @Autowired
    private CollectionLogGroupRepository collectionLogGroupRepository;

    @Autowired
    private CollectionLogItemRepository collectionLogItemRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    private CollectionLogCompletion testCollectionLogCompletion;
    private CollectionLogGroup testCollectionLogGroup;
    private CollectionLogItem testCollectionLogItem;
    private Player testPlayer;
    private Team testTeam;

    @BeforeAll
    public void setUp() {
        testTeam = teamRepository.save(SharedTestVariables.makeTestTeam());
        testPlayer = playerRepository.save(SharedTestVariables.makeTestPlayer(testTeam));
        testCollectionLogGroup = collectionLogGroupRepository.save(SharedTestVariables.makeTestCollectionLogChecklistGroup());
        testCollectionLogItem = collectionLogItemRepository.save(SharedTestVariables.makeTestCollectionLogItem(testCollectionLogGroup));
        testCollectionLogCompletion = collectionLogCompletionRepository.save(SharedTestVariables.makeTestCollectionLogCompletion(testCollectionLogItem, testPlayer));
    }

    @AfterAll
    public void tearDown() {
        collectionLogCompletionRepository.delete(testCollectionLogCompletion);
        collectionLogItemRepository.delete(testCollectionLogItem);
        collectionLogGroupRepository.delete(testCollectionLogGroup);
        playerRepository.delete(testPlayer);
        teamRepository.delete(testTeam);
    }

    @Test
    @Transactional
    public void Should_FindTestCollectionLogCompletion_When_GivenTestCollectionLogItemId() {
        Iterable<CollectionLogCompletion> collectionLogCompletions = collectionLogCompletionRepository.findAllByItemId(testCollectionLogItem.getId());

        assertThat(collectionLogCompletions)
            .isNotNull()
            .isNotEmpty()
            .contains(testCollectionLogCompletion);
    }

    @Test
    @Transactional
    public void Should_NotFindTestCollectionLogCompletion_When_GivenWrongCollectionLogItemId() {
        Iterable<CollectionLogCompletion> collectionLogCompletions = collectionLogCompletionRepository.findAllByItemId(0);

        assertThat(collectionLogCompletions)
            .isNotNull()
            .doesNotContain(testCollectionLogCompletion);
    }

    @Test
    @Transactional
    public void Should_FindTestCollectionLogCompletion_When_GivenTestPlayerId() {
        Iterable<CollectionLogCompletion> collectionLogCompletions = collectionLogCompletionRepository.findAllByPlayerId(testPlayer.getId());

        assertThat(collectionLogCompletions)
            .isNotNull()
            .isNotEmpty()
            .contains(testCollectionLogCompletion);
    }

    @Test
    @Transactional
    public void Should_NotFindTestCollectionLogCompletion_When_GivenWrongPlayerId() {
        Iterable<CollectionLogCompletion> collectionLogCompletions = collectionLogCompletionRepository.findAllByPlayerId(0);

        assertThat(collectionLogCompletions)
            .isNotNull()
            .doesNotContain(testCollectionLogCompletion);
    }
}
