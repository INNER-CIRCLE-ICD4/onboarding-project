package fc.innercircle.sanghyukonboarding.common.infrastructure.numbering

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.longs.shouldBeInRange
import io.kotest.matchers.shouldBe
import java.time.Instant

/**
 * SnowflakeIdGenerator의 ID 구조를 검증하는 테스트
 *
 * Snowflake ID 구조:
 * - 64비트 정수
 * - 첫 1비트: 부호 비트 (항상 0)
 * - 다음 41비트: 타임스탬프 (밀리초)
 * - 다음 5비트: 데이터센터 ID
 * - 다음 5비트: 워커 ID
 * - 마지막 12비트: 시퀀스 번호
 */
class SnowflakeIdGeneratorStructureTest : FunSpec({
    val generator = SnowflakeIdGenerator()
    val epoch = 1609459200000L // 2021-01-01 UTC 기준

    context("ID 구조 검증") {
        test("생성된 ID는 64비트 양수여야 한다") {
            val id = generator.nextId()
            id shouldBeGreaterThan 0L
            id shouldBeLessThan Long.MAX_VALUE
        }

        test("ID에서 타임스탬프 부분을 추출할 수 있어야 한다") {
            val id = generator.nextId()
            val timestamp = (id shr 22) + epoch

            // 타임스탬프는 현재 시간과 근접해야 함 (1초 이내)
            val now = Instant.now().toEpochMilli()
            timestamp shouldBeInRange (now - 1000L..now + 1000L)
        }

        test("ID에서 데이터센터 ID를 추출할 수 있어야 한다") {
            val id = generator.nextId()
            val datacenterId = (id shr 17) and 0x1F // 5비트 마스크

            // 현재 구현에서는 데이터센터 ID가 1로 고정되어 있음
            datacenterId shouldBe 1L
        }

        test("ID에서 워커 ID를 추출할 수 있어야 한다") {
            val id = generator.nextId()
            val workerId = (id shr 12) and 0x1F // 5비트 마스크

            // 현재 구현에서는 워커 ID가 1로 고정되어 있음
            workerId shouldBe 1L
        }

        test("ID에서 시퀀스 번호를 추출할 수 있어야 한다") {
            val id = generator.nextId()
            val sequence = id and 0xFFF // 12비트 마스크

            // 시퀀스 번호는 0부터 4095 사이의 값이어야 함
            sequence shouldBeInRange (0L..4095L)
        }
    }

    context("연속 생성된 ID의 구조 검증") {
        test("연속 생성된 ID의 타임스탬프는 같거나 증가해야 한다") {
            val id1 = generator.nextId()
            val id2 = generator.nextId()

            val timestamp1 = (id1 shr 22) + epoch
            val timestamp2 = (id2 shr 22) + epoch

            // 두 번째 타임스탬프는 첫 번째와 같거나 더 커야 함
            (timestamp2 >= timestamp1) shouldBe true
        }

        test("같은 밀리초에 생성된 ID는 시퀀스 번호가 증가해야 한다") {
            // 빠르게 여러 ID를 생성하여 같은 밀리초에 생성될 가능성을 높임
            val ids = List(10) { generator.nextId() }

            // 타임스탬프가 같은 연속된 ID 쌍 찾기
            for (i in 0 until ids.size - 1) {
                val timestamp1 = (ids[i] shr 22) + epoch
                val timestamp2 = (ids[i + 1] shr 22) + epoch

                if (timestamp1 == timestamp2) {
                    // 같은 밀리초에 생성된 ID는 시퀀스 번호가 증가해야 함
                    val sequence1 = ids[i] and 0xFFF
                    val sequence2 = ids[i + 1] and 0xFFF

                    (sequence2 > sequence1) shouldBe true
                    return@test // 테스트 성공
                }
            }

            // 같은 밀리초에 생성된 ID를 찾지 못했다면 테스트를 건너뜀
            // 실제로는 대부분의 경우 같은 밀리초에 생성된 ID를 찾을 수 있음
        }
    }
})
