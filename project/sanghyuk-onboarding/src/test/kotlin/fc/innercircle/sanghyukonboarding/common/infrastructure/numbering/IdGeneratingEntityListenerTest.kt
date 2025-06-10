package fc.innercircle.sanghyukonboarding.common.infrastructure.numbering

import fc.innercircle.sanghyukonboarding.common.domain.model.Identifiable
import fc.innercircle.sanghyukonboarding.mock.TestIdGenerator
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.test.util.ReflectionTestUtils

/**
 * IdGeneratingEntityListener 테스트
 *
 * 이 테스트는 IdGeneratingEntityListener가 @PrePersist 시점에
 * Identifiable 엔티티의 ID를 올바르게 설정하는지 검증합니다.
 */
class IdGeneratingEntityListenerTest : BehaviorSpec({
    given("IdGeneratingEntityListener와 Identifiable 엔티티") {
        // 테스트용 IdGenerator 구현체
        val testIdGenerator = TestIdGenerator()
        val listener = IdGeneratingEntityListener()

        // 리플렉션을 사용하여 private 필드 설정
        ReflectionTestUtils.setField(listener, "idGenerator", testIdGenerator)

        `when`("ID가 0인 엔티티에 대해 onPrePersist가 호출되면") {
            // 테스트용 Identifiable 구현체
            val entity = object : Identifiable {
                override var id: Long = 0L
            }

            // ID 생성기가 반환할 ID 설정
            val generatedId = 12345L
            testIdGenerator.nextIdToReturn = generatedId
            testIdGenerator.callCount = 0

            // onPrePersist 호출
            listener.onPrePersist(entity)

            then("엔티티의 ID가 생성된 ID로 설정되어야 한다") {
                entity.id shouldBe generatedId
                testIdGenerator.callCount shouldBe 1
            }
        }

        `when`("ID가 이미 설정된 엔티티에 대해 onPrePersist가 호출되면") {
            // 테스트용 Identifiable 구현체 (ID가 이미 설정됨)
            val existingId = 9876L
            val entity = object : Identifiable {
                override var id: Long = existingId
            }

            // 호출 횟수 초기화
            testIdGenerator.callCount = 0

            // onPrePersist 호출
            listener.onPrePersist(entity)

            then("엔티티의 ID가 변경되지 않아야 한다") {
                entity.id shouldBe existingId
                testIdGenerator.callCount shouldBe 0
            }
        }

        `when`("Identifiable이 아닌 객체에 대해 onPrePersist가 호출되면") {
            // Identifiable이 아닌 객체
            val nonIdentifiableEntity = object {}

            // 호출 횟수 초기화
            testIdGenerator.callCount = 0

            // onPrePersist 호출
            listener.onPrePersist(nonIdentifiableEntity)

            then("아무 일도 일어나지 않아야 한다") {
                testIdGenerator.callCount shouldBe 0
            }
        }
    }
})
