package org.uimshowdown.bingo;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.uimshowdown.bingo.constants.TestTag;

@SpringBootTest
@ActiveProfiles("test")
class BingoApplicationTests {

    @Test
    @Tag(TestTag.SMOKE_TEST)
    void contextLoads() {
    }

}
