package formService.util

fun checkUUID(match: String): Boolean {
    val uuidReg = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}\$".toRegex()

    return uuidReg.matches(match)
}
