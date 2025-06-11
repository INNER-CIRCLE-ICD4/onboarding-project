package com.innercircle.survey.response.domain

import com.innercircle.survey.survey.domain.Question
import com.innercircle.survey.survey.domain.QuestionType
import com.innercircle.survey.survey.domain.Survey
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class ResponseSnapshotTest : DescribeSpec({

    describe("응답 스냅샷 기능") {
        context("응답 생성 시") {
            it("설문조사의 현재 상태를 스냅샷으로 저장한다") {
                // given
                val survey =
                    Survey.create(
                        title = "원래 제목",
                        description = "원래 설명",
                    )
                val question =
                    Question.create(
                        title = "질문 1",
                        description = "설명",
                        type = QuestionType.SHORT_TEXT,
                        required = true,
                    )
                survey.addQuestion(question)

                // when
                val answer = Answer.createTextAnswer(question, "답변")
                val response = Response.create(survey, listOf(answer))

                // then
                response.surveySnapshot shouldContain "원래 제목"
                response.surveySnapshot shouldContain "원래 설명"
                response.surveySnapshot shouldContain "질문 1"
            }
        }

        context("설문 수정 후") {
            it("응답은 원래 설문 정보를 유지한다") {
                // given
                val survey = Survey.create("원래 제목", "원래 설명")
                val question =
                    Question.create(
                        title = "원래 질문",
                        description = "설명",
                        type = QuestionType.SHORT_TEXT,
                        required = true,
                    )
                survey.addQuestion(question)

                val answer = Answer.createTextAnswer(question, "답변")
                val response = Response.create(survey, listOf(answer))

                // when: 설문 수정
                survey.update("새로운 제목", "새로운 설명")
                question.update(
                    title = "새로운 질문",
                    description = "새로운 설명",
                    type = QuestionType.SHORT_TEXT,
                    required = true,
                )

                // then: 응답은 원래 정보 유지
                response.getOriginalSurveyTitle() shouldBe "원래 제목"
                response.getOriginalSurveyVersion() shouldBe 1
                response.surveySnapshot shouldContain "원래 질문"

                // 현재 설문은 변경됨
                survey.title shouldBe "새로운 제목"
                survey.version shouldBe 2
            }
        }
    }
})
