package com.innercircle.survey.survey.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class SurveyTest : DescribeSpec({
    describe("Survey") {
        describe("create") {
            context("유효한 데이터로 생성") {
                it("설문조사가 성공적으로 생성되어야 한다") {
                    val survey =
                        Survey.create(
                            title = "고객 만족도 조사",
                            description = "서비스 개선을 위한 설문조사입니다.",
                        )

                    survey.title shouldBe "고객 만족도 조사"
                    survey.description shouldBe "서비스 개선을 위한 설문조사입니다."
                    survey.version shouldBe 1
                    survey.questions shouldBe emptyList()
                }

                it("질문과 함께 설문조사가 생성되어야 한다") {
                    val questions =
                        listOf(
                            Question.create(
                                title = "만족도",
                                description = "서비스 만족도를 평가해주세요.",
                                type = QuestionType.SINGLE_CHOICE,
                                required = true,
                                choices = listOf("만족", "불만족"),
                            ),
                        )

                    val survey =
                        Survey.create(
                            title = "설문조사",
                            description = "설명",
                            questions = questions,
                        )

                    survey.questions.size shouldBe 1
                    survey.questions.first().survey shouldBe survey
                }
            }

            context("유효하지 않은 데이터로 생성") {
                it("빈 제목으로 생성 시 예외가 발생해야 한다") {
                    shouldThrow<IllegalArgumentException> {
                        Survey.create(
                            title = "",
                            description = "설명",
                        )
                    }.message shouldBe "설문조사 제목은 필수입니다."
                }

                it("빈 설명으로 생성 시 예외가 발생해야 한다") {
                    shouldThrow<IllegalArgumentException> {
                        Survey.create(
                            title = "제목",
                            description = "",
                        )
                    }.message shouldBe "설문조사 설명은 필수입니다."
                }

                it("10개를 초과하는 질문으로 생성 시 예외가 발생해야 한다") {
                    val questions =
                        (1..11).map {
                            Question.create(
                                title = "질문 $it",
                                description = "설명 $it",
                                type = QuestionType.SHORT_TEXT,
                            )
                        }

                    shouldThrow<IllegalArgumentException> {
                        Survey.create(
                            title = "설문조사",
                            description = "설명",
                            questions = questions,
                        )
                    }.message shouldBe "설문조사는 최대 10개의 항목만 가질 수 있습니다."
                }
            }
        }

        describe("addQuestion") {
            val survey =
                Survey.create(
                    title = "설문조사",
                    description = "설명",
                )

            context("질문 추가") {
                it("질문이 성공적으로 추가되어야 한다") {
                    val question =
                        Question.create(
                            title = "질문",
                            description = "설명",
                            type = QuestionType.SHORT_TEXT,
                        )

                    survey.addQuestion(question)

                    survey.questions.size shouldBe 1
                    survey.questions.first() shouldBe question
                    question.survey shouldBe survey
                }

                it("10개 초과 추가 시 예외가 발생해야 한다") {
                    repeat(9) {
                        survey.addQuestion(
                            Question.create(
                                title = "질문 $it",
                                description = "설명 $it",
                                type = QuestionType.SHORT_TEXT,
                            ),
                        )
                    }

                    shouldThrow<IllegalArgumentException> {
                        survey.addQuestion(
                            Question.create(
                                title = "11번째 질문",
                                description = "설명",
                                type = QuestionType.SHORT_TEXT,
                            ),
                        )
                    }.message shouldBe "설문조사는 최대 10개의 항목만 가질 수 있습니다."
                }
            }
        }

        describe("update") {
            val survey =
                Survey.create(
                    title = "원래 제목",
                    description = "원래 설명",
                )

            it("제목과 설명이 업데이트되고 버전이 증가해야 한다") {
                val originalVersion = survey.version

                survey.update(
                    title = "새 제목",
                    description = "새 설명",
                )

                survey.title shouldBe "새 제목"
                survey.description shouldBe "새 설명"
                survey.version shouldBe originalVersion + 1
            }
        }

        describe("removeQuestion") {
            val survey =
                Survey.create(
                    title = "설문조사",
                    description = "설명",
                )
            val question =
                Question.create(
                    title = "질문",
                    description = "설명",
                    type = QuestionType.SHORT_TEXT,
                )

            it("질문이 제거되어야 한다") {
                survey.addQuestion(question)
                survey.questions.size shouldBe 1

                survey.removeQuestion(question)

                survey.questions.size shouldBe 0
                question.survey shouldBe null
            }
        }
    }
})
