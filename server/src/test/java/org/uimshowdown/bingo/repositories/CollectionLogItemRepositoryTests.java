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
import org.uimshowdown.bingo.models.CollectionLogItem;

@SpringBootTest
@Tag(TestTag.INTEGRATION_TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class CollectionLogItemRepositoryTests {
    
    @Autowired
    private TestUtils testUtils;

    @Autowired
    private CollectionLogItemRepository collectionLogItemRepository;

    private CollectionLogItem testCollectionLogItem;

    @BeforeAll
    public void setUp() {
        testUtils.resetDB();
        testCollectionLogItem = collectionLogItemRepository.save(SharedTestVariables.makeTestCollectionLogItem());
    }

    @AfterAll
    public void tearDown() {
        testUtils.resetDB();
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
