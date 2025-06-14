package com.innercircle.survey.response.adapter.`in`.web

import com.innercircle.survey.response.adapter.out.persistence.dto.ResponseSummaryProjection
import com.innercircle.survey.response.application.port.`in`.ResponseUseCase
import com.innercircle.survey.response.domain.Answer
import com.innercircle.survey.response.domain.Response
import com.innercircle.survey.response.domain.exception.MissingRequiredAnswerException
import com.innercircle.survey.response.domain.exception.ResponseNotFoundException
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
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
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

        describe("GET /api/v1/responses/{responseId}") {
            context("존재하는 응답 ID로 조회") {
                it("200 OK와 함께 응답을 반환해야 한다") {
                    // given
                    val responseId = UUID.randomUUID()
                    val surveyId = UUID.randomUUID()

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
                            idField.set(this, responseId)

                            val createdAtField = baseClass.getDeclaredField("createdAt")
                            createdAtField.isAccessible = true
                            createdAtField.set(this, LocalDateTime.now())
                        }

                    every { responseUseCase.getResponseById(responseId) } returns mockResponse

                    // when & then
                    RestAssured.given()
                        .`when`()
                        .get("/api/v1/responses/$responseId")
                        .then()
                        .statusCode(200)
                        .body("success", equalTo(true))
                        .body("data.id", equalTo(responseId.toString()))
                        .body("data.respondentId", equalTo("user123"))
                }
            }

            context("존재하지 않는 응답 ID로 조회") {
                it("404 Not Found를 반환해야 한다") {
                    // given
                    val responseId = UUID.randomUUID()
                    every { responseUseCase.getResponseById(responseId) } throws ResponseNotFoundException(responseId)

                    // when & then
                    RestAssured.given()
                        .`when`()
                        .get("/api/v1/responses/$responseId")
                        .then()
                        .statusCode(404)
                }
            }
        }

        describe("GET /api/v1/surveys/{surveyId}/responses") {
            val surveyId = UUID.randomUUID()

            context("페이징 파라미터와 함께 목록 조회") {
                it("200 OK와 함께 페이징된 응답 목록을 반환해야 한다") {
                    // given
                    val pageable = PageRequest.of(0, 10)
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

                    val mockResponses =
                        listOf(
                            Response.create(survey = mockSurvey, respondentId = "user1"),
                            Response.create(survey = mockSurvey, respondentId = "user2"),
                        )
                    mockResponses.forEach { response ->
                        val baseClass = response::class.java.superclass
                        val idField = baseClass.getDeclaredField("id")
                        idField.isAccessible = true
                        idField.set(response, UUID.randomUUID())

                        val createdAtField = baseClass.getDeclaredField("createdAt")
                        createdAtField.isAccessible = true
                        createdAtField.set(response, LocalDateTime.now())
                    }

                    val page = PageImpl(mockResponses, pageable, 2)
                    every { responseUseCase.getResponsesBySurveyId(surveyId, any()) } returns page

                    // when & then
                    RestAssured.given()
                        .queryParam("page", 0)
                        .queryParam("size", 10)
                        .`when`()
                        .get("/api/v1/surveys/$surveyId/responses")
                        .then()
                        .statusCode(200)
                        .body("content", hasSize<Any>(2))
                        .body("pageNumber", equalTo(0))
                        .body("pageSize", equalTo(10))
                        .body("totalElements", equalTo(2))
                }
            }

            context("summary=true 파라미터와 함께 요약 조회") {
                it("200 OK와 함께 응답 요약 목록을 반환해야 한다") {
                    // given
                    val pageable = PageRequest.of(0, 10)
                    val mockSummaries =
                        listOf(
                            ResponseSummaryProjection(
                                id = UUID.randomUUID(),
                                surveyId = surveyId,
                                surveyVersion = 1,
                                respondentId = "user1",
                                createdAt = LocalDateTime.now(),
                                answerCount = 5,
                            ),
                            ResponseSummaryProjection(
                                id = UUID.randomUUID(),
                                surveyId = surveyId,
                                surveyVersion = 1,
                                respondentId = "user2",
                                createdAt = LocalDateTime.now(),
                                answerCount = 3,
                            ),
                        )
                    val page = PageImpl(mockSummaries, pageable, 2)
                    every { responseUseCase.getResponseSummariesBySurveyId(surveyId, any()) } returns page

                    // when & then
                    RestAssured.given()
                        .queryParam("page", 0)
                        .queryParam("size", 10)
                        .queryParam("summary", true)
                        .`when`()
                        .get("/api/v1/surveys/$surveyId/responses")
                        .then()
                        .statusCode(200)
                        .body("content", hasSize<Any>(2))
                        .body("content[0].answerCount", equalTo(5))
                        .body("content[1].answerCount", equalTo(3))
                }
            }

            context("존재하지 않는 설문조사 ID로 조회") {
                it("404 Not Found를 반환해야 한다") {
                    // given
                    every {
                        responseUseCase.getResponsesBySurveyId(
                            surveyId,
                            any(),
                        )
                    } throws SurveyNotFoundException(surveyId)

                    // when & then
                    RestAssured.given()
                        .queryParam("page", 0)
                        .queryParam("size", 10)
                        .`when`()
                        .get("/api/v1/surveys/$surveyId/responses")
                        .then()
                        .statusCode(404)
                }
            }
        }
    })
