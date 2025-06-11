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

fun <T> Collection<T>.hasDuplicates(): Boolean {
    return this.size != this.toSet().size
}
