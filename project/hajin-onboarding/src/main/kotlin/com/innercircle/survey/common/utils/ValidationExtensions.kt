package com.innercircle.survey.common.utils

fun String?.isValidLength(maxLength: Int): Boolean {
    return this != null && this.isNotBlank() && this.length <= maxLength
}

fun <T> Collection<T>?.isValidSize(
    min: Int = 0,
    max: Int = Int.MAX_VALUE,
): Boolean {
    return this != null && this.size in min..max
}

fun String?.matchesPattern(pattern: String): Boolean {
    return this != null && this.matches(Regex(pattern))
}

fun <T> Collection<T>.hasDuplicates(): Boolean {
    return this.size != this.toSet().size
}

fun <T> Collection<T>.findDuplicates(): Set<T> {
    return this.groupingBy { it }
        .eachCount()
        .filter { it.value > 1 }
        .keys
        .toSet()
}
