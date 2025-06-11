package com.innercircle.survey.common.utils

inline fun <reified T : Enum<T>> String.toEnumOrNull(): T? =
    try {
        enumValueOf<T>(this.uppercase())
    } catch (e: IllegalArgumentException) {
        null
    }
