// src/main/kotlin/com/example/survey/id/SnowflakeIdGenerator.kt
package fc.innercircle.sanghyukonboarding.common.infrastructure.numbering

import fc.innercircle.sanghyukonboarding.common.domain.service.IdGenerator
import org.springframework.stereotype.Component
import java.time.Instant

/**
 * Twitter Snowflake 알고리즘 기반의 64-bit ID 생성기
 */
@Component
class SnowflakeIdGenerator : IdGenerator {
    override fun nextId(): Long = SnowflakeIdGenerator.nextId()

    /**
     * ID 생성 로직:
     * 1. 현재 시간의 밀리초 단위 타임스탬프를 가져옵니다.
     * 2. 워커 ID와 데이터센터 ID를 설정합니다.
     * 3. 시퀀스를 관리하여 중복되지 않는 ID를 생성합니다.
     * 4. 타임스탬프, 워커 ID, 데이터센터 ID, 시퀀스를 조합하여 최종 ID를 반환합니다.
     */
    companion object {
        private const val EPOCH = 1609459200000L // 2021-01-01 UTC 기준
        private const val WORKER_ID_BITS = 5
        private const val DATACENTER_ID_BITS = 5
        private const val SEQUENCE_BITS = 12

        private const val MAX_WORKER_ID = -1L xor (-1L shl WORKER_ID_BITS)
        private const val MAX_DATACENTER_ID = -1L xor (-1L shl DATACENTER_ID_BITS)

        private const val WORKER_ID_SHIFT = SEQUENCE_BITS
        private const val DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS
        private const val TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS
        private const val SEQUENCE_MASK = -1L xor (-1L shl SEQUENCE_BITS)

        // TODO: 실제 운영 환경에 맞게 설정하거나, 환경변수/설정파일에서 로드하세요.
        private val workerId = 1L.coerceIn(0L, MAX_WORKER_ID)
        private val datacenterId = 1L.coerceIn(0L, MAX_DATACENTER_ID)

        @Volatile private var lastTimestamp = -1L

        @Volatile private var sequence = 0L

        @Synchronized
        private fun nextId(): Long {
            var timestamp = Instant.now().toEpochMilli()
            if (timestamp < lastTimestamp) {
                throw RuntimeException("Clock moved backwards. Refusing to generate id for $lastTimestamp ms")
            }
            if (timestamp == lastTimestamp) {
                sequence = (sequence + 1) and SEQUENCE_MASK
                if (sequence == 0L) {
                    // 시퀀스 소진 시, 다음 밀리초로 대기
                    while (timestamp <= lastTimestamp) {
                        timestamp = Instant.now().toEpochMilli()
                    }
                }
            } else {
                sequence = 0L
            }
            lastTimestamp = timestamp

            return ((timestamp - EPOCH) shl TIMESTAMP_LEFT_SHIFT) or
                (datacenterId shl DATACENTER_ID_SHIFT) or
                (workerId shl WORKER_ID_SHIFT) or
                sequence
        }
    }
}
