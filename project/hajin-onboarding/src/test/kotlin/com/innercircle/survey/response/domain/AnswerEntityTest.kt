package com.innercircle.survey.response.domain

import com.innercircle.survey.survey.domain.Question
import com.innercircle.survey.survey.domain.QuestionType
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class AnswerEntityTest : DescribeSpec({

    describe("Answer 엔티티") {
        context("텍스트 답변 생성") {
            it("단답형 질문에 텍스트 답변을 생성할 수 있다") {
                val question =
                    Question.create(
                        title = "이름",
                        description = "이름을 입력하세요",
                        type = QuestionType.SHORT_TEXT,
                    )

                val answer = Answer.createTextAnswer(question, "홍길동")

                answer.question shouldBe question
                answer.textValue shouldBe "홍길동"
                answer.selectedChoices shouldHaveSize 0
                answer.isValid() shouldBe true
            }

            it("장문형 질문에 텍스트 답변을 생성할 수 있다") {
                val question =
                    Question.create(
                        title = "의견",
                        description = "자유롭게 의견을 작성하세요",
                        type = QuestionType.LONG_TEXT,
                    )

                val answer = Answer.createTextAnswer(question, "이것은 긴 답변입니다...")

                answer.textValue shouldBe "이것은 긴 답변입니다..."
                answer.isValid() shouldBe true
            }

            it("선택형 질문에는 텍스트 답변을 생성할 수 없다") {
                val question =
                    Question.create(
                        title = "선택",
                        description = "하나를 선택하세요",
                        type = QuestionType.SINGLE_CHOICE,
                        choices = listOf("옵션1", "옵션2"),
                    )

                shouldThrow<IllegalArgumentException> {
                    Answer.createTextAnswer(question, "텍스트")
                }.message shouldBe "텍스트 답변은 텍스트 타입 질문에만 가능합니다."
            }

            it("빈 텍스트로는 답변을 생성할 수 없다") {
                val question =
                    Question.create(
                        title = "질문",
                        description = "설명",
                        type = QuestionType.SHORT_TEXT,
                    )

                shouldThrow<IllegalArgumentException> {
                    Answer.createTextAnswer(question, "")
                }.message shouldBe "텍스트 답변은 비어있을 수 없습니다."
            }
        }

        context("선택지 답변 생성") {
            it("단일 선택 질문에 하나의 선택지를 선택할 수 있다") {
                val question =
                    Question.create(
                        title = "성별",
                        description = "성별을 선택하세요",
                        type = QuestionType.SINGLE_CHOICE,
                        choices = listOf("남성", "여성", "기타"),
                    )

                val choice = question.choices.first()
                val answer = Answer.createChoiceAnswer(question, setOf(choice))

                answer.selectedChoices shouldHaveSize 1
                answer.selectedChoices.first() shouldBe choice
                answer.isValid() shouldBe true
            }

            it("다중 선택 질문에 여러 선택지를 선택할 수 있다") {
                val question =
                    Question.create(
                        title = "취미",
                        description = "취미를 모두 선택하세요",
                        type = QuestionType.MULTIPLE_CHOICE,
                        choices = listOf("독서", "운동", "영화", "게임"),
                    )

                val selectedChoices = question.choices.take(2).toSet()
                val answer = Answer.createChoiceAnswer(question, selectedChoices)

                answer.selectedChoices shouldHaveSize 2
                answer.isValid() shouldBe true
            }

            it("텍스트 타입 질문에는 선택지 답변을 생성할 수 없다") {
                val question =
                    Question.create(
                        title = "질문",
                        description = "설명",
                        type = QuestionType.SHORT_TEXT,
                    )

                shouldThrow<IllegalArgumentException> {
                    Answer.createChoiceAnswer(question, setOf())
                }.message shouldBe "선택지 답변은 선택형 질문에만 가능합니다."
            }

            it("빈 선택지로는 답변을 생성할 수 없다") {
                val question =
                    Question.create(
                        title = "선택",
                        description = "선택하세요",
                        type = QuestionType.SINGLE_CHOICE,
                        choices = listOf("옵션1", "옵션2"),
                    )

                shouldThrow<IllegalArgumentException> {
                    Answer.createChoiceAnswer(question, emptySet())
                }.message shouldBe "최소 하나 이상의 선택지를 선택해야 합니다."
            }
        }

        context("선택지 검증") {
            it("해당 질문의 선택지만 선택할 수 있다") {
                val question1 =
                    Question.create(
                        title = "질문1",
                        description = "설명",
                        type = QuestionType.SINGLE_CHOICE,
                        choices = listOf("옵션1", "옵션2"),
                    )

                val question2 =
                    Question.create(
                        title = "질문2",
                        description = "설명",
                        type = QuestionType.SINGLE_CHOICE,
                        choices = listOf("옵션A", "옵션B"),
                    )

                val answer = Answer.createChoiceAnswer(question1, setOf(question1.choices.first()))

                shouldThrow<IllegalArgumentException> {
                    answer.addSelectedChoice(question2.choices.first())
                }.message shouldBe "해당 선택지는 이 질문의 선택지가 아닙니다."
            }

            it("단일 선택 질문에는 하나의 선택지만 선택할 수 있다") {
                val question =
                    Question.create(
                        title = "질문",
                        description = "설명",
                        type = QuestionType.SINGLE_CHOICE,
                        choices = listOf("옵션1", "옵션2", "옵션3"),
                    )

                val answer = Answer.createChoiceAnswer(question, setOf(question.choices[0]))

                shouldThrow<IllegalArgumentException> {
                    answer.addSelectedChoice(question.choices[1])
                }.message shouldBe "단일 선택 질문에는 하나의 선택지만 선택할 수 있습니다."
            }
        }
    }
})
