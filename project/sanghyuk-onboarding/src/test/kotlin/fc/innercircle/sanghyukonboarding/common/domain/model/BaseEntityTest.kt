package fc.innercircle.sanghyukonboarding.common.domain.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

// BaseEntity는 abstract 클래스이므로 테스트를 위한 구현체를 만듭니다.
class TestEntity(
    createdBy: String,
) : BaseEntity(createdBy)

class BaseEntityTest :
    DescribeSpec({
        describe("BaseEntity 클래스 테스트") {

            context("BaseEntity 생성") {
                it("생성자를 통해 BaseEntity를 생성할 수 있어야 한다") {
                    // given
                    val createdBy = "testUser"
                    val now = LocalDateTime.now()

                    // when
                    val entity = TestEntity(createdBy)

                    // then
                    entity.createdBy shouldBe createdBy
                    entity.createdAt shouldNotBe null
                    // 생성 시간이 현재 시간과 1초 이내인지 확인
                    ChronoUnit.SECONDS.between(entity.createdAt, now) shouldBe 0
                    entity.updatedBy shouldBe null
                    entity.updatedAt shouldBe null
                }
            }
        }
    })
