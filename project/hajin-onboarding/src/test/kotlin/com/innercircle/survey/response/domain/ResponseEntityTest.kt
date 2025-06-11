package com.innercircle.survey.response.domain

import com.innercircle.survey.survey.domain.Question
import com.innercircle.survey.survey.domain.QuestionType
import com.innercircle.survey.survey.domain.Survey
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class ResponseEntityTest : DescribeSpec({

    describe("Response 엔티티") {
        val requiredQuestion =
            Question.create(
                title = "필수 질문",
                description = "반드시 답변해야 합니다",
                type = QuestionType.SHORT_TEXT,
                required = true,
            )

        val optionalQuestion =
            Question.create(
                title = "선택 질문",
                description = "답변하지 않아도 됩니다",
                type = QuestionType.SHORT_TEXT,
                required = false,
            )

        val survey =
            Survey.create(
                title = "테스트 설문조사",
                description = "테스트용 설문조사입니다",
                questions = listOf(requiredQuestion, optionalQuestion),
            )

        context("생성 시") {
            it("모든 필수 항목에 답변하면 응답을 생성할 수 있다") {
                val answer = Answer.createTextAnswer(requiredQuestion, "답변 내용")
                val response = Response.create(survey, listOf(answer))

                response.survey shouldBe survey
                response.answers shouldHaveSize 1
                response.hasAnsweredAllRequiredQuestions() shouldBe true
            }

            it("필수 항목에 답변하지 않으면 생성할 수 없다") {
                shouldThrow<IllegalArgumentException> {
                    Response.create(survey, emptyList())
                }.message shouldBe "모든 필수 항목에 답변해야 합니다."
            }

            it("선택 항목은 답변하지 않아도 된다") {
                val answer = Answer.createTextAnswer(requiredQuestion, "답변 내용")
                val response = Response.create(survey, listOf(answer))

                response.answers shouldHaveSize 1
                response.findAnswerByQuestionId(optionalQuestion.id) shouldBe null
            }
        }

        context("답변 추가 시") {
            it("동일한 질문에 중복 답변할 수 없다") {
                val answer1 = Answer.createTextAnswer(requiredQuestion, "첫 번째 답변")
                val response = Response.create(survey, listOf(answer1))

                val answer2 = Answer.createTextAnswer(requiredQuestion, "두 번째 답변")

                shouldThrow<IllegalArgumentException> {
                    response.addAnswer(answer2)
                }.message shouldBe "이미 해당 질문에 대한 답변이 존재합니다."
            }

            it("설문조사에 없는 질문에는 답변할 수 없다") {
                val otherQuestion =
                    Question.create(
                        title = "다른 질문",
                        description = "설명",
                        type = QuestionType.SHORT_TEXT,
                    )
                val answer = Answer.createTextAnswer(requiredQuestion, "답변")
                val response = Response.create(survey, listOf(answer))

                val invalidAnswer = Answer.createTextAnswer(otherQuestion, "답변")

                shouldThrow<IllegalArgumentException> {
                    response.addAnswer(invalidAnswer)
                }.message shouldBe "답변하려는 질문이 설문조사에 존재하지 않습니다."
            }
        }

        context("답변 조회 시") {
            it("질문 ID로 답변을 찾을 수 있다") {
                val answer = Answer.createTextAnswer(requiredQuestion, "답변 내용")
                val response = Response.create(survey, listOf(answer))

                val foundAnswer = response.findAnswerByQuestionId(requiredQuestion.id)
                foundAnswer shouldNotBe null
                foundAnswer?.textValue shouldBe "답변 내용"
            }

            it("답변하지 않은 질문은 null을 반환한다") {
                val answer = Answer.createTextAnswer(requiredQuestion, "답변 내용")
                val response = Response.create(survey, listOf(answer))

                val foundAnswer = response.findAnswerByQuestionId(optionalQuestion.id)
                foundAnswer shouldBe null
            }
        }
    }
})
