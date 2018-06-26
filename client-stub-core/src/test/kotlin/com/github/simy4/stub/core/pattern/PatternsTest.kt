package com.github.simy4.stub.core.pattern

import kotlin.test.Test
import kotlin.test.assertEquals

class PatternsTest {
    @Test
    fun equivPattern_shouldMatchEquivalentObjects() {
        assertEquals(ExactMatch, EquivPattern(1)(1))
    }

    @Test
    fun equivPattern_shouldNotMatchNotEquivalentObjects() {
        assertEquals(NoMatch, EquivPattern(1)(-1))
    }

    @Test
    fun matchesSafelyPattern_shouldMatchEquivalentObjects() {
        assertEquals(ExactMatch, MatchingSafelyPattern(EquivPattern("test"))("test"))
    }

    @Test
    fun matchesSafelyPattern_shouldNotMatchNotEquivalentObjects() {
        assertEquals(NoMatch, MatchingSafelyPattern(EquivPattern("test"))(1))
    }

    @Test
    fun mapEntryPattern_shouldMatchIfEntryExistsThatSatisfyValuePattern() {
        assertEquals(ExactMatch, MapEntryPattern("two", EquivPattern(2))(mapOf(
                "one" to 1,
                "two" to 2,
                "three" to 3)))
    }

    @Test
    fun mapEntryPattern_shouldNotMatchIfKeyNotFound() {
        assertEquals(NoMatch, MapEntryPattern("four", EquivPattern(4))(mapOf(
                "one" to 1,
                "two" to 2,
                "three" to 3)))
    }

    @Test
    fun mapEntryPattern_shouldNoMatchIfNoEntrySatisfyValuePattern() {
        assertEquals(NoMatch, MapEntryPattern("two", EquivPattern(4))(mapOf(
                "one" to 1,
                "two" to 2,
                "three" to 3)))
    }

    @Test
    fun containsPattern_shouldMatchIfElementExistsThatSatisfyPattern() {
        assertEquals(ExactMatch, ContainsPattern(EquivPattern(2))(listOf(1, 2, 3)))
    }

    @Test
    fun containsPattern_shouldNoMatchIfNoElementSatisfyPattern() {
        assertEquals(NoMatch, ContainsPattern(EquivPattern(4))(listOf(1, 2, 3)))
    }
}