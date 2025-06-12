package fc.innercircle.jinhoonboarding.common.util

inline fun <reified T : Enum<T>> String.toEnumOrNull(): T? {
    return try {
        enumValueOf<T>(this.uppercase())
    } catch (e: IllegalArgumentException) {
        null
    }
}