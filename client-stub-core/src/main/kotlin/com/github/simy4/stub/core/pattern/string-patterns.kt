package com.github.simy4.stub.core.pattern

class RegexPattern(regexString: String, ignoreCase: Boolean = false): Pattern<String> {
    private val regex = if (ignoreCase) Regex(regexString, RegexOption.IGNORE_CASE) else Regex(regexString)

    override fun invoke(value: String): MatchResult = MatchResult.match(regex.matches(value))
}

class StartsWithPattern(private val prefix: String, private val ignoreCase: Boolean = false): Pattern<String> {
    override fun invoke(value: String): MatchResult = MatchResult.match(value.startsWith(prefix, ignoreCase))
}

class EndsWithPattern(private val postfix: String, private val ignoreCase: Boolean = false): Pattern<String> {
    override fun invoke(value: String): MatchResult = MatchResult.match(value.endsWith(postfix, ignoreCase))
}
