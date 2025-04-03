package org.uimshowdown.bingo;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.uimshowdown.bingo.constants.TestTag;

@SpringBootTest
class BingoApplicationTests {

	@Test
	@Tag(TestTag.SMOKE_TEST)
	void contextLoads() {
	}

}
