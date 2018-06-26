package com.github.simy4.stub.core.pattern

import kotlin.test.Test
import kotlin.test.assertEquals

class StringPatternsTest {
    @Test fun startsWithPattern_shouldMatch() {
        assertEquals(ExactMatch, StartsWithPattern("test")("test-string"))
    }

    @Test fun startsWithPattern_shouldMatchSelf() {
        assertEquals(ExactMatch, StartsWithPattern("test")("test"))
    }

    @Test fun startsWithPattern_shouldNotMatchSubpattern() {
        assertEquals(NoMatch, StartsWithPattern("test")("tes"))
    }

    @Test fun endsWithPattern_shouldMatch() {
        assertEquals(ExactMatch, EndsWithPattern("test")("string-test"))
    }

    @Test fun endsWithPattern_shouldMatchSelf() {
        assertEquals(ExactMatch, EndsWithPattern("test")("test"))
    }

    @Test fun endsWithPattern_shouldNotMatchSubpattern() {
        assertEquals(NoMatch, EndsWithPattern("test")("est"))
    }
}