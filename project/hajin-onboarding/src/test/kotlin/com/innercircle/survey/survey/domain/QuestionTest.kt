package com.innercircle.survey.survey.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class QuestionTest : DescribeSpec({
    describe("Question") {
        describe("create") {
            context("텍스트 타입 질문 생성") {
                it("SHORT_TEXT 타입 질문이 생성되어야 한다") {
                    val question =
                        Question.create(
                            title = "이름",
                            description = "이름을 입력해주세요.",
                            type = QuestionType.SHORT_TEXT,
                            required = true,
                        )

                    question.title shouldBe "이름"
                    question.description shouldBe "이름을 입력해주세요."
                    question.type shouldBe QuestionType.SHORT_TEXT
                    question.required shouldBe true
                    question.choices shouldBe emptyList()
                }

                it("LONG_TEXT 타입 질문이 생성되어야 한다") {
                    val question =
                        Question.create(
                            title = "의견",
                            description = "자유롭게 의견을 작성해주세요.",
                            type = QuestionType.LONG_TEXT,
                            required = false,
                        )

                    question.type shouldBe QuestionType.LONG_TEXT
                    question.required shouldBe false
                    question.choices shouldBe emptyList()
                }
            }

            context("선택형 타입 질문 생성") {
                it("SINGLE_CHOICE 타입 질문이 선택지와 함께 생성되어야 한다") {
                    val choices = listOf("예", "아니오")
                    val question =
                        Question.create(
                            title = "동의 여부",
                            description = "약관에 동의하시나요?",
                            type = QuestionType.SINGLE_CHOICE,
                            required = true,
                            choices = choices,
                        )

                    question.type shouldBe QuestionType.SINGLE_CHOICE
                    question.choices.size shouldBe 2
                    question.choices.map { it.text } shouldBe choices
                }

                it("MULTIPLE_CHOICE 타입 질문이 선택지와 함께 생성되어야 한다") {
                    val choices = listOf("옵션1", "옵션2", "옵션3")
                    val question =
                        Question.create(
                            title = "복수 선택",
                            description = "해당하는 항목을 모두 선택하세요.",
                            type = QuestionType.MULTIPLE_CHOICE,
                            required = false,
                            choices = choices,
                        )

                    question.type shouldBe QuestionType.MULTIPLE_CHOICE
                    question.choices.size shouldBe 3
                }
            }

            context("유효하지 않은 데이터로 생성") {
                it("빈 제목으로 생성 시 예외가 발생해야 한다") {
                    shouldThrow<IllegalArgumentException> {
                        Question.create(
                            title = "",
                            description = "설명",
                            type = QuestionType.SHORT_TEXT,
                        )
                    }.message shouldBe "항목 제목은 필수입니다."
                }

                it("빈 설명으로 생성 시 예외가 발생해야 한다") {
                    shouldThrow<IllegalArgumentException> {
                        Question.create(
                            title = "제목",
                            description = "",
                            type = QuestionType.SHORT_TEXT,
                        )
                    }.message shouldBe "항목 설명은 필수입니다."
                }

                it("선택형 질문에 선택지가 없으면 예외가 발생해야 한다") {
                    shouldThrow<IllegalArgumentException> {
                        Question.create(
                            title = "선택",
                            description = "선택해주세요.",
                            type = QuestionType.SINGLE_CHOICE,
                            choices = emptyList(),
                        )
                    }.message shouldBe "단일 선택 타입은 최소 1개 이상의 선택지가 필요합니다."
                }

                it("선택지가 20개를 초과하면 예외가 발생해야 한다") {
                    val choices = (1..21).map { "선택지 $it" }

                    shouldThrow<IllegalArgumentException> {
                        Question.create(
                            title = "너무 많은 선택지",
                            description = "선택해주세요.",
                            type = QuestionType.MULTIPLE_CHOICE,
                            choices = choices,
                        )
                    }.message shouldBe "선택지는 최대 20개까지만 추가할 수 있습니다."
                }
            }
        }

        describe("update") {
            context("질문 타입이 변경되는 경우") {
                it("선택형에서 텍스트형으로 변경 시 선택지가 제거되어야 한다") {
                    val question =
                        Question.create(
                            title = "원래 선택형",
                            description = "선택해주세요.",
                            type = QuestionType.SINGLE_CHOICE,
                            choices = listOf("A", "B", "C"),
                        )

                    question.choices.size shouldBe 3

                    question.update(
                        title = "텍스트형으로 변경",
                        description = "입력해주세요.",
                        type = QuestionType.SHORT_TEXT,
                        required = true,
                    )

                    question.type shouldBe QuestionType.SHORT_TEXT
                    question.choices shouldBe emptyList()
                }

                it("텍스트형에서 선택형으로 변경 시 선택지가 추가되어야 한다") {
                    val question =
                        Question.create(
                            title = "원래 텍스트형",
                            description = "입력해주세요.",
                            type = QuestionType.SHORT_TEXT,
                        )

                    question.update(
                        title = "선택형으로 변경",
                        description = "선택해주세요.",
                        type = QuestionType.MULTIPLE_CHOICE,
                        required = false,
                        newChoices = listOf("새 옵션1", "새 옵션2"),
                    )

                    question.type shouldBe QuestionType.MULTIPLE_CHOICE
                    question.choices.size shouldBe 2
                    question.choices.map { it.text } shouldBe listOf("새 옵션1", "새 옵션2")
                }
            }

            context("선택형 질문의 선택지 업데이트") {
                it("기존 선택지가 새 선택지로 교체되어야 한다") {
                    val question =
                        Question.create(
                            title = "선택형",
                            description = "선택해주세요.",
                            type = QuestionType.SINGLE_CHOICE,
                            choices = listOf("기존1", "기존2"),
                        )

                    question.update(
                        title = "선택형",
                        description = "선택해주세요.",
                        type = QuestionType.SINGLE_CHOICE,
                        required = true,
                        newChoices = listOf("새로운1", "새로운2", "새로운3"),
                    )

                    question.choices.size shouldBe 3
                    question.choices.map { it.text } shouldBe listOf("새로운1", "새로운2", "새로운3")
                }
            }
        }

        describe("addChoice") {
            context("선택형 질문에 선택지 추가") {
                val question =
                    Question.create(
                        title = "선택형",
                        description = "선택해주세요.",
                        type = QuestionType.SINGLE_CHOICE,
                        choices = listOf("초기 선택지"),
                    )

                it("선택지가 추가되어야 한다") {
                    val newChoice = Choice.create("새 선택지")
                    question.addChoice(newChoice)

                    question.choices.size shouldBe 2
                    question.choices.last().text shouldBe "새 선택지"
                    newChoice.question shouldBe question
                }
            }

            context("텍스트형 질문에 선택지 추가") {
                val question =
                    Question.create(
                        title = "텍스트형",
                        description = "입력해주세요.",
                        type = QuestionType.SHORT_TEXT,
                    )

                it("예외가 발생해야 한다") {
                    shouldThrow<IllegalArgumentException> {
                        question.addChoice(Choice.create("선택지"))
                    }.message shouldBe "단답형 타입은 선택지를 가질 수 없습니다."
                }
            }

            context("최대 개수 초과") {
                val choices = (1..20).map { "선택지 $it" }
                val question =
                    Question.create(
                        title = "많은 선택지",
                        description = "선택해주세요.",
                        type = QuestionType.MULTIPLE_CHOICE,
                        choices = choices,
                    )

                it("21번째 선택지 추가 시 예외가 발생해야 한다") {
                    shouldThrow<IllegalArgumentException> {
                        question.addChoice(Choice.create("21번째"))
                    }.message shouldBe "선택지는 최대 20개까지만 추가할 수 있습니다."
                }
            }
        }
    }
})
