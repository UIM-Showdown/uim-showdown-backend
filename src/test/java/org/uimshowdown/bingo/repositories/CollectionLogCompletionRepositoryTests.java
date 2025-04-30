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
import org.uimshowdown.bingo.models.CollectionLogCompletion;
import org.uimshowdown.bingo.models.CollectionLogItem;
import org.uimshowdown.bingo.models.Player;
import org.uimshowdown.bingo.models.Team;

@SpringBootTest
@Tag(TestTag.INTEGRATION_TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class CollectionLogCompletionRepositoryTests {
    
    @Autowired
    private TestUtils testUtils;
    
    @Autowired
    private CollectionLogCompletionRepository collectionLogCompletionRepository;

    @Autowired
    private CollectionLogItemRepository collectionLogItemRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    private CollectionLogCompletion testCollectionLogCompletion;
    private CollectionLogItem testCollectionLogItem;
    private Player testPlayer;
    private Team testTeam;

    @BeforeAll
    public void setUp() {
        testUtils.resetDB();
        testTeam = teamRepository.save(SharedTestVariables.makeTestTeam());
        testPlayer = playerRepository.save(SharedTestVariables.makeTestPlayer(testTeam));
        testCollectionLogItem = collectionLogItemRepository.save(SharedTestVariables.makeTestCollectionLogItem());
        testCollectionLogCompletion = collectionLogCompletionRepository.save(SharedTestVariables.makeTestCollectionLogCompletion(testCollectionLogItem, testPlayer));
    }

    @AfterAll
    public void tearDown() {
        testUtils.resetDB();
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
