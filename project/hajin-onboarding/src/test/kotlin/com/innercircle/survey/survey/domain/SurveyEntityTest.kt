package com.innercircle.survey.survey.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class SurveyEntityTest : DescribeSpec({

    describe("Survey 엔티티") {
        context("생성 시") {
            it("유효한 데이터로 설문조사를 생성할 수 있다") {
                val survey =
                    Survey.create(
                        title = "고객 만족도 조사",
                        description = "서비스 품질 개선을 위한 설문조사입니다.",
                    )

                survey.title shouldBe "고객 만족도 조사"
                survey.description shouldBe "서비스 품질 개선을 위한 설문조사입니다."
                survey.questions shouldHaveSize 0
            }

            it("빈 제목으로는 생성할 수 없다") {
                shouldThrow<IllegalArgumentException> {
                    Survey.create(
                        title = "",
                        description = "설명",
                    )
                }.message shouldBe "설문조사 제목은 필수입니다."
            }

            it("빈 설명으로는 생성할 수 없다") {
                shouldThrow<IllegalArgumentException> {
                    Survey.create(
                        title = "제목",
                        description = "",
                    )
                }.message shouldBe "설문조사 설명은 필수입니다."
            }

            it("질문과 함께 생성할 수 있다") {
                val question =
                    Question.create(
                        title = "서비스 만족도",
                        description = "전반적인 서비스 만족도를 평가해주세요",
                        type = QuestionType.SINGLE_CHOICE,
                        required = true,
                        choices = listOf("매우 만족", "만족", "보통", "불만족", "매우 불만족"),
                    )

                val survey =
                    Survey.create(
                        title = "고객 만족도 조사",
                        description = "서비스 품질 개선을 위한 설문조사입니다.",
                        questions = listOf(question),
                    )

                survey.questions shouldHaveSize 1
                survey.questions.first() shouldBe question
                question.survey shouldBe survey
            }
        }

        context("질문 추가 시") {
            it("최대 10개까지 질문을 추가할 수 있다") {
                val survey = Survey.create("설문조사", "설명")

                repeat(10) { index ->
                    val question =
                        Question.create(
                            title = "질문 ${index + 1}",
                            description = "설명",
                            type = QuestionType.SHORT_TEXT,
                        )
                    survey.addQuestion(question)
                }

                survey.questions shouldHaveSize 10
            }

            it("10개를 초과하여 질문을 추가할 수 없다") {
                val survey = Survey.create("설문조사", "설명")

                repeat(10) { index ->
                    val question =
                        Question.create(
                            title = "질문 ${index + 1}",
                            description = "설명",
                            type = QuestionType.SHORT_TEXT,
                        )
                    survey.addQuestion(question)
                }

                shouldThrow<IllegalArgumentException> {
                    survey.addQuestion(
                        Question.create(
                            title = "질문 11",
                            description = "설명",
                            type = QuestionType.SHORT_TEXT,
                        ),
                    )
                }.message shouldBe "설문조사는 최대 10개의 항목만 가질 수 있습니다."
            }
        }

        context("질문 제거 시") {
            it("질문을 제거할 수 있다") {
                val question =
                    Question.create(
                        title = "질문",
                        description = "설명",
                        type = QuestionType.SHORT_TEXT,
                    )
                val survey =
                    Survey.create(
                        title = "설문조사",
                        description = "설명",
                        questions = listOf(question),
                    )

                survey.questions shouldHaveSize 1
                survey.removeQuestion(question)
                survey.questions shouldHaveSize 0
                question.survey shouldBe null
            }
        }

        context("정보 업데이트 시") {
            it("제목과 설명을 업데이트할 수 있다") {
                val survey = Survey.create("원래 제목", "원래 설명")

                survey.update("새로운 제목", "새로운 설명")

                survey.title shouldBe "새로운 제목"
                survey.description shouldBe "새로운 설명"
            }
        }
    }
})
