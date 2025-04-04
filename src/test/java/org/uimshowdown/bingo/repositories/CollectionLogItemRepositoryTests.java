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

@SpringBootTest
@Tag(TestTag.INTEGRATION_TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CollectionLogItemRepositoryTests {
    @Autowired
    private CollectionLogGroupRepository collectionLogGroupRepository;

    @Autowired
    private CollectionLogItemRepository collectionLogItemRepository;

    private CollectionLogGroup testCollectionLogGroup;
    private CollectionLogItem testCollectionLogItem;

    @BeforeAll
    public void setUp() {
    	collectionLogItemRepository.deleteAll();
    	collectionLogGroupRepository.deleteAll();
        testCollectionLogGroup = collectionLogGroupRepository.save(SharedTestVariables.makeTestCollectionLogChecklistGroup());
        testCollectionLogItem = collectionLogItemRepository.save(SharedTestVariables.makeTestCollectionLogItem(testCollectionLogGroup));
    }

    @AfterAll
    public void tearDown() {
        collectionLogItemRepository.delete(testCollectionLogItem);
        collectionLogGroupRepository.delete(testCollectionLogGroup);
    }

    @Test
    @Transactional
    public void Should_FindTestCollectionLogItem_When_GivenTestCollectionLogGroupId() {
        Iterable<CollectionLogItem> collectionLogItems = collectionLogItemRepository.findAllByGroupId(testCollectionLogGroup.getId());

        assertThat(collectionLogItems)
            .isNotNull()
            .isNotEmpty()
            .contains(testCollectionLogItem);
    }

    @Test
    @Transactional
    public void Should_NotFindTestCollectionLogItem_When_GivenWrongCollectionLogGroupId() {
        Iterable<CollectionLogItem> collectionLogItems = collectionLogItemRepository.findAllByGroupId(0);

        assertThat(collectionLogItems)
            .isNotNull()
            .doesNotContain(testCollectionLogItem);
    }

    @Test
    @Transactional
    public void Should_FindTestCollectionLogItem_When_GivenTestCollectionLogItemName() {
        CollectionLogItem collectionLogItem = collectionLogItemRepository.findByName(testCollectionLogItem.getName()).orElse(null);

        assertThat(collectionLogItem)
            .isNotNull()
            .isEqualTo(testCollectionLogItem);
    }

    @Test
    @Transactional
    public void Should_NotFindTestCollectionLogItem_When_GivenWrongCollectionLogItemName() {
        CollectionLogItem collectionLogItem = collectionLogItemRepository.findByName("Not Likely to be Found").orElse(null);

        assertThat(collectionLogItem)
            .isNull();
    }
}
