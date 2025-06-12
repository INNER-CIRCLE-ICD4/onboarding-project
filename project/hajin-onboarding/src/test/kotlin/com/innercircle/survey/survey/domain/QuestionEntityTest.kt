package com.innercircle.survey.survey.domain

import com.innercircle.survey.survey.domain.exception.MissingChoicesException
import com.innercircle.survey.survey.domain.exception.SurveyChoiceLimitExceededException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class QuestionEntityTest : DescribeSpec({

    describe("Question 엔티티") {
        context("생성 시") {
            it("텍스트 타입 질문을 생성할 수 있다") {
                val question =
                    Question.create(
                        title = "이름을 입력해주세요",
                        description = "본인의 성명을 입력해주세요",
                        type = QuestionType.SHORT_TEXT,
                        required = true,
                    )

                question.title shouldBe "이름을 입력해주세요"
                question.description shouldBe "본인의 성명을 입력해주세요"
                question.type shouldBe QuestionType.SHORT_TEXT
                question.required shouldBe true
                question.choices shouldHaveSize 0
            }

            it("선택형 질문을 생성할 수 있다") {
                val question =
                    Question.create(
                        title = "좋아하는 색깔",
                        description = "좋아하는 색깔을 선택해주세요",
                        type = QuestionType.SINGLE_CHOICE,
                        required = false,
                        choices = listOf("빨강", "파랑", "노랑"),
                    )

                question.type shouldBe QuestionType.SINGLE_CHOICE
                question.choices shouldHaveSize 3
                question.choices.map { it.text } shouldBe listOf("빨강", "파랑", "노랑")
            }

            it("선택형 질문은 선택지가 필수다") {
                shouldThrow<MissingChoicesException> {
                    Question.create(
                        title = "선택 질문",
                        description = "설명",
                        type = QuestionType.SINGLE_CHOICE,
                        choices = emptyList(),
                    )
                }.message shouldBe "선택형 항목에는 선택지가 필요합니다. (항목: 선택 질문)"
            }

            it("빈 제목으로는 생성할 수 없다") {
                shouldThrow<IllegalArgumentException> {
                    Question.create(
                        title = "",
                        description = "설명",
                        type = QuestionType.SHORT_TEXT,
                    )
                }.message shouldBe "항목 제목은 필수입니다."
            }
        }

        context("선택지 관리") {
            it("선택형 질문에 선택지를 추가할 수 있다") {
                val question =
                    Question.create(
                        title = "질문",
                        description = "설명",
                        type = QuestionType.MULTIPLE_CHOICE,
                        choices = listOf("옵션1"),
                    )

                question.addChoice(Choice.create("옵션2"))
                question.choices shouldHaveSize 2
            }

            it("텍스트 타입 질문에는 선택지를 추가할 수 없다") {
                val question =
                    Question.create(
                        title = "질문",
                        description = "설명",
                        type = QuestionType.SHORT_TEXT,
                    )

                shouldThrow<IllegalArgumentException> {
                    question.addChoice(Choice.create("옵션"))
                }.message shouldBe "단답형 타입은 선택지를 가질 수 없습니다."
            }

            it("최대 20개까지 선택지를 추가할 수 있다") {
                val question =
                    Question.create(
                        title = "질문",
                        description = "설명",
                        type = QuestionType.MULTIPLE_CHOICE,
                        choices = (1..20).map { "옵션$it" },
                    )

                question.choices shouldHaveSize 20

                shouldThrow<SurveyChoiceLimitExceededException> {
                    question.addChoice(Choice.create("옵션21"))
                }.message shouldBe "선택지 수가 제한을 초과했습니다. (현재: 21, 최대: 20)"
            }
        }

        context("업데이트 시") {
            it("질문 정보를 업데이트할 수 있다") {
                val question =
                    Question.create(
                        title = "원래 제목",
                        description = "원래 설명",
                        type = QuestionType.SHORT_TEXT,
                    )

                question.update(
                    title = "새 제목",
                    description = "새 설명",
                    type = QuestionType.SHORT_TEXT,
                    required = true,
                )

                question.title shouldBe "새 제목"
                question.description shouldBe "새 설명"
                question.required shouldBe true
            }

            it("타입을 텍스트에서 선택형으로 변경할 수 있다") {
                val question =
                    Question.create(
                        title = "질문",
                        description = "설명",
                        type = QuestionType.SHORT_TEXT,
                    )

                question.update(
                    title = "질문",
                    description = "설명",
                    type = QuestionType.SINGLE_CHOICE,
                    required = false,
                    newChoices = listOf("예", "아니오"),
                )

                question.type shouldBe QuestionType.SINGLE_CHOICE
                question.choices shouldHaveSize 2
            }

            it("타입을 선택형에서 텍스트로 변경하면 선택지가 제거된다") {
                val question =
                    Question.create(
                        title = "질문",
                        description = "설명",
                        type = QuestionType.SINGLE_CHOICE,
                        choices = listOf("옵션1", "옵션2"),
                    )

                question.choices shouldHaveSize 2

                question.update(
                    title = "질문",
                    description = "설명",
                    type = QuestionType.SHORT_TEXT,
                    required = false,
                )

                question.type shouldBe QuestionType.SHORT_TEXT
                question.choices shouldHaveSize 0
            }
        }
    }
})
