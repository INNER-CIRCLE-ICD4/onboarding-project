package com.innercircle.survey.common

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
@Transactional
abstract class BaseIntegrationTest : DescribeSpec() {
    override fun extensions() = listOf(SpringExtension)

    init {
        // 테스트 실행 전 설정
        beforeSpec {
            // 필요한 경우 테스트 데이터 초기화
        }

        // 테스트 실행 후 정리
        afterSpec {
            // 필요한 경우 리소스 정리
        }
    }
}
