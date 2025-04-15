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
import org.uimshowdown.bingo.models.CollectionLogGroup;

@SpringBootTest
@Tag(TestTag.INTEGRATION_TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CollectionLogGroupRepositoryTests {
    
    @Autowired
    private TestUtils testUtils;
    
    @Autowired
    private CollectionLogGroupRepository collectionLogGroupRepository;

    private CollectionLogGroup testCollectionLogGroup;

    @BeforeAll
    public void setUp() {
        testUtils.resetDB();
        testCollectionLogGroup = collectionLogGroupRepository.save(SharedTestVariables.makeTestCollectionLogChecklistGroup());
    }

    @AfterAll
    public void tearDown() {
        testUtils.resetDB();
    }

    @Test
    @Transactional
    public void Should_FindTestCollectionLogGroup_When_GivenTestCollectionLogGroupName() {
        CollectionLogGroup collectionLogGroup = collectionLogGroupRepository.findByName(testCollectionLogGroup.getName()).orElse(null);

        assertThat(collectionLogGroup)
            .isNotNull()
            .isEqualTo(testCollectionLogGroup);
    }

    @Test
    @Transactional
    public void Should_NotFindTestCollectionLogGroup_When_GivenWrongCollectionLogGroupName() {
        CollectionLogGroup collectionLogGroup = collectionLogGroupRepository.findByName("Not Likely to be Found").orElse(null);

        assertThat(collectionLogGroup)
            .isNull();
    }
    
}
