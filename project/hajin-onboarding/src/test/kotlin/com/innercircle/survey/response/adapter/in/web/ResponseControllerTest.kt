package com.innercircle.survey.response.adapter.`in`.web

import com.innercircle.survey.response.application.port.`in`.ResponseUseCase
import com.innercircle.survey.response.domain.Answer
import com.innercircle.survey.response.domain.Response
import com.innercircle.survey.response.domain.exception.MissingRequiredAnswerException
import com.innercircle.survey.survey.domain.QuestionType
import com.innercircle.survey.survey.domain.Survey
import com.innercircle.survey.survey.domain.exception.SurveyNotFoundException
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.verify
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.notNullValue
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.TestConstructor
import java.time.LocalDateTime
import java.util.UUID

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ResponseControllerTest(
    @LocalServerPort private val port: Int,
    @MockkBean private val responseUseCase: ResponseUseCase,
) : DescribeSpec({

        beforeSpec {
            RestAssured.port = port
        }

        describe("POST /api/v1/surveys/{surveyId}/responses") {
            context("유효한 응답 제출 요청") {
                it("201 Created와 함께 생성된 응답을 반환해야 한다") {
                    // given
                    val surveyId = UUID.randomUUID()
                    val questionId = UUID.randomUUID()
                    val choiceId = UUID.randomUUID()

                    val mockSurvey =
                        Survey.create(
                            title = "테스트 설문",
                            description = "테스트 설명",
                        ).apply {
                            val baseClass = this::class.java.superclass
                            val idField = baseClass.getDeclaredField("id")
                            idField.isAccessible = true
                            idField.set(this, surveyId)
                        }

                    val mockResponse =
                        Response.create(
                            survey = mockSurvey,
                            respondentId = "user123",
                        ).apply {
                            val baseClass = this::class.java.superclass

                            val idField = baseClass.getDeclaredField("id")
                            idField.isAccessible = true
                            idField.set(this, UUID.randomUUID())

                            val createdAtField = baseClass.getDeclaredField("createdAt")
                            createdAtField.isAccessible = true
                            createdAtField.set(this, LocalDateTime.now())

                            // Answer 추가
                            val answer =
                                Answer.createChoiceAnswer(
                                    questionId = questionId,
                                    questionTitle = "질문 제목",
                                    questionType = QuestionType.SINGLE_CHOICE,
                                    selectedChoiceIds = setOf(choiceId),
                                )
                            addAnswer(answer)
                        }

                    every { responseUseCase.submitResponse(any()) } returns mockResponse

                    val requestBody =
                        """
                        {
                            "respondentId": "user123",
                            "answers": [
                                {
                                    "questionId": "$questionId",
                                    "selectedChoiceIds": ["$choiceId"]
                                }
                            ]
                        }
                        """.trimIndent()

                    // when & then
                    RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(requestBody)
                        .`when`()
                        .post("/api/v1/surveys/$surveyId/responses")
                        .then()
                        .statusCode(201)
                        .body("success", equalTo(true))
                        .body("message", equalTo("응답이 성공적으로 제출되었습니다."))
                        .body("data.surveyId", equalTo(surveyId.toString()))
                        .body("data.respondentId", equalTo("user123"))
                        .body("data.answers", hasSize<Any>(1))

                    verify { responseUseCase.submitResponse(any()) }
                }
            }

            context("텍스트형 응답 제출") {
                it("201 Created를 반환해야 한다") {
                    // given
                    val surveyId = UUID.randomUUID()
                    val questionId = UUID.randomUUID()

                    val mockSurvey =
                        Survey.create(
                            title = "테스트 설문",
                            description = "테스트 설명",
                        ).apply {
                            val baseClass = this::class.java.superclass
                            val idField = baseClass.getDeclaredField("id")
                            idField.isAccessible = true
                            idField.set(this, surveyId)
                        }

                    val mockResponse =
                        Response.create(
                            survey = mockSurvey,
                        ).apply {
                            val baseClass = this::class.java.superclass

                            val idField = baseClass.getDeclaredField("id")
                            idField.isAccessible = true
                            idField.set(this, UUID.randomUUID())

                            val createdAtField = baseClass.getDeclaredField("createdAt")
                            createdAtField.isAccessible = true
                            createdAtField.set(this, LocalDateTime.now())

                            val answer =
                                Answer.createTextAnswer(
                                    questionId = questionId,
                                    questionTitle = "의견",
                                    questionType = QuestionType.LONG_TEXT,
                                    textValue = "좋은 서비스입니다.",
                                )
                            addAnswer(answer)
                        }

                    every { responseUseCase.submitResponse(any()) } returns mockResponse

                    val requestBody =
                        """
                        {
                            "answers": [
                                {
                                    "questionId": "$questionId",
                                    "textValue": "좋은 서비스입니다."
                                }
                            ]
                        }
                        """.trimIndent()

                    // when & then
                    RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(requestBody)
                        .`when`()
                        .post("/api/v1/surveys/$surveyId/responses")
                        .then()
                        .statusCode(201)
                        .body("data.answers[0].textValue", equalTo("좋은 서비스입니다."))
                }
            }

            context("존재하지 않는 설문조사에 응답 제출") {
                it("404 Not Found를 반환해야 한다") {
                    // given
                    val surveyId = UUID.randomUUID()
                    every { responseUseCase.submitResponse(any()) } throws SurveyNotFoundException(surveyId)

                    val requestBody =
                        """
                        {
                            "answers": [
                                {
                                    "questionId": "${UUID.randomUUID()}",
                                    "textValue": "응답"
                                }
                            ]
                        }
                        """.trimIndent()

                    // when & then
                    RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(requestBody)
                        .`when`()
                        .post("/api/v1/surveys/$surveyId/responses")
                        .then()
                        .statusCode(404)
                }
            }

            context("필수 항목 누락") {
                it("400 Bad Request를 반환해야 한다") {
                    // given
                    val surveyId = UUID.randomUUID()
                    val missingQuestions = setOf(UUID.randomUUID())
                    every { responseUseCase.submitResponse(any()) } throws
                        MissingRequiredAnswerException(missingQuestions)

                    val requestBody =
                        """
                        {
                            "answers": []
                        }
                        """.trimIndent()

                    // when & then
                    RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(requestBody)
                        .`when`()
                        .post("/api/v1/surveys/$surveyId/responses")
                        .then()
                        .statusCode(400)
                        .body("errorCode", notNullValue())
                }
            }
        }
    })
