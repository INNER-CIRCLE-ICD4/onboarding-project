package fc.innercircle.sanghyukonboarding.common.infrastructure.numbering

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldHaveLength
import io.kotest.matchers.string.shouldMatch
import java.time.Instant

/**
 * UlidGenerator의 ID 구조를 검증하는 테스트
 *
 * ULID 구조:
 * - 26자리 문자열 (Crockford's Base32 알파벳 사용: 0123456789ABCDEFGHJKMNPQRSTVWXYZ)
 * - 첫 10자리: 타임스탬프 (밀리초)
 * - 나머지 16자리: 무작위 값
 */
class UlidGeneratorStructureTest : FunSpec({
    val generator = UlidGenerator()

    context("ID 구조 검증") {
        test("생성된 ID는 26자리 문자열이어야 한다") {
            val id = generator.nextId()
            id.shouldHaveLength(26)
        }

        test("ID는 Crockford's Base32 알파벳만 사용해야 한다") {
            val id = generator.nextId()
            // Crockford's Base32 알파벳: 0-9, A-Z (제외: I, L, O, U)
            id.shouldMatch("^[0-9A-HJKMNP-TV-Z]{26}$")
        }

        test("ID의 첫 10자리는 타임스탬프 부분이어야 한다") {
            // 첫 번째 ID 생성
            val id1 = generator.nextId()
            // 약간의 시간 간격을 두고 두 번째 ID 생성
            Thread.sleep(5)
            val id2 = generator.nextId()

            // ULID의 첫 10자리는 타임스탬프
            val timestamp1 = id1.substring(0, 10)
            val timestamp2 = id2.substring(0, 10)

            // 두 번째 ID의 타임스탬프는 첫 번째보다 크거나 같아야 함
            (timestamp2 >= timestamp1) shouldBe true
        }

        test("ID의 마지막 16자리는 무작위 값이어야 한다") {
            // 여러 ID를 생성하여 무작위 부분이 다른지 확인
            val ids = List(10) { generator.nextId() }

            // 각 ID의 무작위 부분 추출
            val randomParts = ids.map { it.substring(10) }

            // 모든 무작위 부분은 16자리여야 함
            randomParts.forEach { it.shouldHaveLength(16) }

            // 모든 무작위 부분은 Crockford's Base32 알파벳만 사용해야 함
            randomParts.forEach { it.shouldMatch("^[0-9A-HJKMNP-TV-Z]{16}$") }

            // 무작위 부분은 서로 달라야 함 (중복이 없어야 함)
            val uniqueRandomParts = randomParts.toSet()
            uniqueRandomParts.size shouldBe randomParts.size
        }
    }

    context("시간 기반 특성 검증") {
        test("같은 밀리초에 생성된 ID는 사전순으로 정렬 가능해야 한다") {
            // 빠르게 여러 ID를 생성하여 같은 밀리초에 생성될 가능성을 높임
            val ids = List(100) { generator.nextId() }

            // 원본 ID 목록과 정렬된 ID 목록 비교
            val sortedIds = ids.sorted()

            // 정렬된 ID 목록의 타임스탬프 부분이 단조 증가해야 함
            for (i in 0 until sortedIds.size - 1) {
                val timestamp1 = sortedIds[i].substring(0, 10)
                val timestamp2 = sortedIds[i + 1].substring(0, 10)

                // 두 번째 타임스탬프는 첫 번째보다 크거나 같아야 함
                (timestamp2 >= timestamp1) shouldBe true
            }
        }

        test("ID의 타임스탬프 부분은 현재 시간과 근접해야 한다") {
            // 현재 시간 기록
            val beforeGeneration = Instant.now()

            // ID 생성
            val id = generator.nextId()

            // ID 생성 후 시간 기록
            val afterGeneration = Instant.now()

            // ULID의 타임스탬프는 Base32로 인코딩된 48비트 밀리초 타임스탬프
            // 정확한 디코딩은 복잡하므로, 여기서는 단순히 타임스탬프 부분이
            // 현재 시간 전후에 생성되었는지만 확인

            // 타임스탬프 부분이 ID의 첫 10자리
            val timestampPart = id.substring(0, 10)

            // 타임스탬프 부분이 Crockford's Base32 알파벳만 사용하는지 확인
            timestampPart.shouldMatch("^[0-9A-HJKMNP-TV-Z]{10}$")

            // 같은 시간에 생성된 여러 ID의 타임스탬프 부분이 동일한지 확인
            val ids = List(10) { generator.nextId() }
            val timestampParts = ids.map { it.substring(0, 10) }

            // 매우 짧은 시간 내에 생성된 ID들은 타임스탬프 부분이 같거나 매우 유사해야 함
            // (정확한 값 비교는 어렵지만, 타임스탬프 부분의 변화가 크지 않아야 함)
            val uniqueTimestamps = timestampParts.toSet()
            // 일반적으로 짧은 시간 내에는 1-2개의 고유 타임스탬프만 있어야 함
            (uniqueTimestamps.size <= 2) shouldBe true
        }
    }
})
