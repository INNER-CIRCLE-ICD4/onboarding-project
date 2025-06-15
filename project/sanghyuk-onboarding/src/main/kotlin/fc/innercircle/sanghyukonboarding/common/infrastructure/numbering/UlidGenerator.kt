package fc.innercircle.sanghyukonboarding.common.infrastructure.numbering

import java.security.SecureRandom

/**
 * ULID (Universally Unique Lexicographically Sortable Identifier) Generator
 *  - 48-bit timestamp (milliseconds since unix epoch)
 *  - 80-bit randomness
 *  - Crockford's Base32 encoding → 26-character string
 */
object UlidGenerator {
    private val ENCODING = charArrayOf(
        '0','1','2','3','4','5','6','7','8','9',
        'A','B','C','D','E','F','G','H','J','K',
        'M','N','P','Q','R','S','T','V','W','X','Y','Z'
    )

    private val random = SecureRandom()

    /**
     * Generates a new ULID string (26 characters)
     */
    fun next(): String {
        // 1) Timestamp (48 bits)
        val time = System.currentTimeMillis()
        val timeChars = CharArray(10)
        var value = time
        for (i in 9 downTo 0) {
            timeChars[i] = ENCODING[(value and 0x1FL).toInt()]
            value = value ushr 5
        }

        // 2) Randomness (80 bits)
        val randomBytes = ByteArray(10)
        random.nextBytes(randomBytes)
        val randChars = CharArray(16)
        var bitBuffer = 0L
        var bitBufferLen = 0
        var byteIndex = 0
        var charPos = 0
        while (charPos < 16) {
            if (bitBufferLen < 5) {
                bitBuffer = (bitBuffer shl 8) or (randomBytes[byteIndex].toLong() and 0xFFL)
                bitBufferLen += 8
                byteIndex++
            }
            bitBufferLen -= 5
            val idx = ((bitBuffer ushr bitBufferLen) and 0x1F).toInt()
            randChars[charPos++] = ENCODING[idx]
        }

        return String(timeChars) + String(randChars)
    }
}
