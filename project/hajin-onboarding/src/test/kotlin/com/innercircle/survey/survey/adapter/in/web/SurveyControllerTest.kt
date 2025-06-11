package com.innercircle.survey.survey.adapter.`in`.web

import com.innercircle.survey.survey.application.port.`in`.SurveyUseCase
import com.innercircle.survey.survey.domain.Question
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
class SurveyControllerTest(
    @LocalServerPort private val port: Int,
    @MockkBean private val surveyUseCase: SurveyUseCase,
) : DescribeSpec({

        beforeSpec {
            RestAssured.port = port
        }

        describe("POST /api/v1/surveys") {
            context("유효한 설문조사 생성 요청") {
                it("201 Created와 함께 생성된 설문조사를 반환해야 한다") {
                    // given
                    val mockSurvey =
                        Survey.create(
                            title = "고객 만족도 조사",
                            description = "서비스 개선을 위한 설문조사",
                            questions =
                                listOf(
                                    Question.create(
                                        title = "만족도",
                                        description = "서비스 만족도를 평가해주세요",
                                        type = QuestionType.SINGLE_CHOICE,
                                        required = true,
                                        choices = listOf("매우 만족", "만족", "보통", "불만족"),
                                    ),
                                ),
                        ).apply {
                            // Mock을 위한 리플렉션 설정
                            val baseClass = this::class.java.superclass

                            // ID 설정
                            val idField = baseClass.getDeclaredField("id")
                            idField.isAccessible = true
                            idField.set(this, UUID.randomUUID())

                            // createdAt 설정
                            val createdAtField = baseClass.getDeclaredField("createdAt")
                            createdAtField.isAccessible = true
                            createdAtField.set(this, LocalDateTime.now())

                            // updatedAt 설정
                            val updatedAtField = baseClass.getDeclaredField("updatedAt")
                            updatedAtField.isAccessible = true
                            updatedAtField.set(this, LocalDateTime.now())
                        }

                    every { surveyUseCase.createSurvey(any()) } returns mockSurvey

                    val requestBody =
                        """
                        {
                            "title": "고객 만족도 조사",
                            "description": "서비스 개선을 위한 설문조사",
                            "questions": [
                                {
                                    "title": "만족도",
                                    "description": "서비스 만족도를 평가해주세요",
                                    "type": "SINGLE_CHOICE",
                                    "required": true,
                                    "choices": ["매우 만족", "만족", "보통", "불만족"]
                                }
                            ]
                        }
                        """.trimIndent()

                    // when & then
                    RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(requestBody)
                        .`when`()
                        .post("/api/v1/surveys")
                        .then()
                        .statusCode(201)
                        .body("success", equalTo(true))
                        .body("message", equalTo("설문조사가 성공적으로 생성되었습니다."))
                        .body("data.title", equalTo("고객 만족도 조사"))
                        .body("data.questions", hasSize<Any>(1))
                        .body("data.questions[0].choices", hasSize<Any>(4))

                    verify { surveyUseCase.createSurvey(any()) }
                }
            }

            context("잘못된 요청 데이터") {
                it("400 Bad Request를 반환해야 한다") {
                    val invalidRequestBody =
                        """
                        {
                            "title": "",
                            "description": "설명",
                            "questions": []
                        }
                        """.trimIndent()

                    RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(invalidRequestBody)
                        .`when`()
                        .post("/api/v1/surveys")
                        .then()
                        .statusCode(400)
                        .body("errorCode", notNullValue())
                }
            }
        }

        describe("GET /api/v1/surveys/{surveyId}") {
            context("존재하는 설문조사 ID로 조회") {
                it("200 OK와 함께 설문조사를 반환해야 한다") {
                    // given
                    val surveyId = UUID.randomUUID()
                    val mockSurvey =
                        Survey.create(
                            title = "조회 테스트",
                            description = "설문조사 조회 테스트",
                        ).apply {
                            val baseClass = this::class.java.superclass
                            val idField = baseClass.getDeclaredField("id")
                            idField.isAccessible = true
                            idField.set(this, surveyId)

                            val createdAtField = baseClass.getDeclaredField("createdAt")
                            createdAtField.isAccessible = true
                            createdAtField.set(this, LocalDateTime.now())

                            val updatedAtField = baseClass.getDeclaredField("updatedAt")
                            updatedAtField.isAccessible = true
                            updatedAtField.set(this, LocalDateTime.now())
                        }

                    every { surveyUseCase.getSurveyById(surveyId) } returns mockSurvey

                    // when & then
                    RestAssured.given()
                        .`when`()
                        .get("/api/v1/surveys/$surveyId")
                        .then()
                        .statusCode(200)
                        .body("success", equalTo(true))
                        .body("data.title", equalTo("조회 테스트"))
                }
            }

            context("존재하지 않는 설문조사 ID로 조회") {
                it("404 Not Found를 반환해야 한다") {
                    // given
                    val surveyId = UUID.randomUUID()
                    every { surveyUseCase.getSurveyById(surveyId) } throws SurveyNotFoundException(surveyId)

                    // when & then
                    RestAssured.given()
                        .`when`()
                        .get("/api/v1/surveys/$surveyId")
                        .then()
                        .statusCode(404)
                }
            }
        }

        describe("GET /api/v1/surveys") {
            context("페이징 파라미터와 함께 목록 조회") {
                it("200 OK와 함께 페이징된 설문조사 목록을 반환해야 한다") {
                    // given
                    val pageable = PageRequest.of(0, 10)
                    val mockSurveys =
                        listOf(
                            Survey.create(title = "설문조사 1", description = "설명 1"),
                            Survey.create(title = "설문조사 2", description = "설명 2"),
                        )
                    mockSurveys.forEach { survey ->
                        val baseClass = survey::class.java.superclass
                        val idField = baseClass.getDeclaredField("id")
                        idField.isAccessible = true
                        idField.set(survey, UUID.randomUUID())

                        val createdAtField = baseClass.getDeclaredField("createdAt")
                        createdAtField.isAccessible = true
                        createdAtField.set(survey, LocalDateTime.now())

                        val updatedAtField = baseClass.getDeclaredField("updatedAt")
                        updatedAtField.isAccessible = true
                        updatedAtField.set(survey, LocalDateTime.now())
                    }

                    val page = PageImpl(mockSurveys, pageable, 2)
                    every { surveyUseCase.getSurveys(any()) } returns page

                    // when & then
                    RestAssured.given()
                        .queryParam("page", 0)
                        .queryParam("size", 10)
                        .`when`()
                        .get("/api/v1/surveys")
                        .then()
                        .statusCode(200)
                        .body("content", hasSize<Any>(2))
                        .body("pageNumber", equalTo(0))
                        .body("pageSize", equalTo(10))
                        .body("totalElements", equalTo(2))
                }
            }
        }

        describe("PUT /api/v1/surveys/{surveyId}") {
            context("유효한 설문조사 수정 요청") {
                it("200 OK와 함께 수정된 설문조사를 반환해야 한다") {
                    // given
                    val surveyId = UUID.randomUUID()
                    val mockSurvey =
                        Survey.create(
                            title = "수정된 설문조사",
                            description = "수정된 설명",
                            questions =
                                listOf(
                                    Question.create(
                                        title = "새로운 질문",
                                        description = "새로운 질문 설명",
                                        type = QuestionType.MULTIPLE_CHOICE,
                                        required = true,
                                        choices = listOf("옵션1", "옵션2", "옵션3"),
                                    ),
                                ),
                        ).apply {
                            val baseClass = this::class.java.superclass

                            val idField = baseClass.getDeclaredField("id")
                            idField.isAccessible = true
                            idField.set(this, surveyId)

                            val createdAtField = baseClass.getDeclaredField("createdAt")
                            createdAtField.isAccessible = true
                            createdAtField.set(this, LocalDateTime.now())

                            val updatedAtField = baseClass.getDeclaredField("updatedAt")
                            updatedAtField.isAccessible = true
                            updatedAtField.set(this, LocalDateTime.now())

                            // 버전을 2로 설정 (수정되었음을 나타냄)
                            val versionField = this::class.java.getDeclaredField("version")
                            versionField.isAccessible = true
                            versionField.set(this, 2)
                        }

                    every { surveyUseCase.updateSurvey(any()) } returns mockSurvey

                    val requestBody =
                        """
                        {
                            "title": "수정된 설문조사",
                            "description": "수정된 설명",
                            "questions": [
                                {
                                    "title": "새로운 질문",
                                    "description": "새로운 질문 설명",
                                    "type": "MULTIPLE_CHOICE",
                                    "required": true,
                                    "choices": ["옵션1", "옵션2", "옵션3"]
                                }
                            ]
                        }
                        """.trimIndent()

                    // when & then
                    RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(requestBody)
                        .`when`()
                        .put("/api/v1/surveys/$surveyId")
                        .then()
                        .statusCode(200)
                        .body("success", equalTo(true))
                        .body("message", equalTo("설문조사가 성공적으로 수정되었습니다."))
                        .body("data.title", equalTo("수정된 설문조사"))
                        .body("data.version", equalTo(2))
                        .body("data.questions", hasSize<Any>(1))

                    verify { surveyUseCase.updateSurvey(any()) }
                }
            }

            context("존재하지 않는 설문조사 ID로 수정 요청") {
                it("404 Not Found를 반환해야 한다") {
                    // given
                    val surveyId = UUID.randomUUID()
                    every { surveyUseCase.updateSurvey(any()) } throws SurveyNotFoundException(surveyId)

                    val requestBody =
                        """
                        {
                            "title": "수정할 설문조사",
                            "description": "수정할 설명",
                            "questions": [
                                {
                                    "title": "질문",
                                    "description": "설명",
                                    "type": "SHORT_TEXT",
                                    "required": false
                                }
                            ]
                        }
                        """.trimIndent()

                    // when & then
                    RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(requestBody)
                        .`when`()
                        .put("/api/v1/surveys/$surveyId")
                        .then()
                        .statusCode(404)
                }
            }

            context("잘못된 요청 데이터로 수정 요청") {
                it("400 Bad Request를 반환해야 한다") {
                    val surveyId = UUID.randomUUID()
                    val invalidRequestBody =
                        """
                        {
                            "title": "",
                            "description": "설명",
                            "questions": []
                        }
                        """.trimIndent()

                    RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(invalidRequestBody)
                        .`when`()
                        .put("/api/v1/surveys/$surveyId")
                        .then()
                        .statusCode(400)
                        .body("errorCode", notNullValue())
                }
            }
        }
    })
