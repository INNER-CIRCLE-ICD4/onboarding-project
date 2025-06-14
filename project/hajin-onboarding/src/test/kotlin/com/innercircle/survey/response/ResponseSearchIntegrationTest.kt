package com.innercircle.survey.response

import com.innercircle.survey.response.adapter.`in`.web.dto.SubmitResponseRequest
import com.innercircle.survey.survey.adapter.`in`.web.dto.CreateSurveyRequest
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.Matchers.`is`
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import java.util.UUID

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ResponseSearchIntegrationTest(
    @LocalServerPort private val port: Int,
) : DescribeSpec({
    beforeSpec {
        RestAssured.port = port
    }

    describe("설문조사 응답 검색 API") {
        lateinit var surveyId: String

        beforeTest {
            // 설문조사 생성
            val createSurveyRequest = CreateSurveyRequest(
                title = "검색 테스트 설문조사",
                description = "검색 기능 테스트를 위한 설문조사입니다.",
                questions = listOf(
                    CreateSurveyRequest.QuestionRequest(
                        title = "좋아하는 프로그래밍 언어는?",
                        description = "가장 좋아하는 프로그래밍 언어를 선택해주세요.",
                        type = "SINGLE_CHOICE",
                        required = true,
                        choices = listOf("Java", "Kotlin", "Python", "JavaScript"),
                    ),
                    CreateSurveyRequest.QuestionRequest(
                        title = "프로그래밍 경력",
                        description = "프로그래밍 경력을 입력해주세요.",
                        type = "SHORT_TEXT",
                        required = true,
                    ),
                    CreateSurveyRequest.QuestionRequest(
                        title = "사용하는 IDE",
                        description = "주로 사용하는 IDE를 모두 선택해주세요.",
                        type = "MULTIPLE_CHOICE",
                        required = false,
                        choices = listOf("IntelliJ IDEA", "VS Code", "Eclipse", "Vim"),
                    ),
                ),
            )

            val surveyResponse = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(createSurveyRequest)
                .`when`()
                .post("/api/v1/surveys")
                .then()
                .statusCode(201)
                .extract()
                .response()

            surveyId = surveyResponse.jsonPath().getString("data.id")

            // 여러 응답 제출
            val questions = surveyResponse.jsonPath().getList("data.questions", Map::class.java)
            
            // 응답 1: Kotlin 선택, 5년 경력, IntelliJ IDEA 사용
            submitResponse(surveyId, questions, "Kotlin", "5년", listOf("IntelliJ IDEA"))
            
            // 응답 2: Java 선택, 10년 경력, Eclipse와 IntelliJ IDEA 사용
            submitResponse(surveyId, questions, "Java", "10년", listOf("Eclipse", "IntelliJ IDEA"))
            
            // 응답 3: Python 선택, 3년 경력, VS Code 사용
            submitResponse(surveyId, questions, "Python", "3년", listOf("VS Code"))
        }

        context("질문 제목으로 검색할 때") {
            it("부분 일치하는 질문의 응답들을 반환해야 한다") {
                RestAssured
                    .given()
                    .queryParam("questionTitle", "프로그래밍 언어")
                    .`when`()
                    .get("/api/v1/surveys/$surveyId/responses")
                    .then()
                    .statusCode(200)
                    .body("totalElements", `is`(3))
                    .body("content.size()", `is`(3))
            }

            it("일치하지 않는 질문 제목으로 검색하면 빈 결과를 반환해야 한다") {
                RestAssured
                    .given()
                    .queryParam("questionTitle", "존재하지 않는 질문")
                    .`when`()
                    .get("/api/v1/surveys/$surveyId/responses")
                    .then()
                    .statusCode(200)
                    .body("totalElements", `is`(0))
                    .body("content.size()", `is`(0))
            }
        }

        context("응답 값으로 검색할 때") {
            it("텍스트 응답에서 부분 일치하는 값을 찾아야 한다") {
                RestAssured
                    .given()
                    .queryParam("answerValue", "5년")
                    .`when`()
                    .get("/api/v1/surveys/$surveyId/responses")
                    .then()
                    .statusCode(200)
                    .body("totalElements", `is`(1))
            }

            it("선택지 텍스트에서 부분 일치하는 값을 찾아야 한다") {
                RestAssured
                    .given()
                    .queryParam("answerValue", "IntelliJ")
                    .`when`()
                    .get("/api/v1/surveys/$surveyId/responses")
                    .then()
                    .statusCode(200)
                    .body("totalElements", `is`(2)) // 응답 1, 2가 IntelliJ를 포함
            }
        }

        context("질문 제목과 응답 값으로 함께 검색할 때") {
            it("두 조건을 모두 만족하는 응답들을 반환해야 한다") {
                RestAssured
                    .given()
                    .queryParam("questionTitle", "IDE")
                    .queryParam("answerValue", "VS Code")
                    .`when`()
                    .get("/api/v1/surveys/$surveyId/responses")
                    .then()
                    .statusCode(200)
                    .body("totalElements", `is`(1))
            }
        }

        context("페이징과 함께 검색할 때") {
            it("검색 결과를 페이징하여 반환해야 한다") {
                RestAssured
                    .given()
                    .queryParam("answerValue", "년")
                    .queryParam("page", 0)
                    .queryParam("size", 2)
                    .`when`()
                    .get("/api/v1/surveys/$surveyId/responses")
                    .then()
                    .statusCode(200)
                    .body("content.size()", `is`(2))
                    .body("totalElements", `is`(3))
                    .body("totalPages", `is`(2))
            }
        }
    }
})

private fun submitResponse(
    surveyId: String,
    questions: List<Map<*, *>>,
    language: String,
    experience: String,
    ides: List<String>,
) {
    val languageQuestion = questions.find { it["title"] == "좋아하는 프로그래밍 언어는?" }!!
    val experienceQuestion = questions.find { it["title"] == "프로그래밍 경력" }!!
    val ideQuestion = questions.find { it["title"] == "사용하는 IDE" }!!

    val languageChoices = (languageQuestion["choices"] as List<Map<String, Any>>)
    val languageChoice = languageChoices.find { it["text"] == language }!!

    val ideChoices = (ideQuestion["choices"] as List<Map<String, Any>>)
    val selectedIdes = ideChoices.filter { ides.contains(it["text"] as String) }

    val submitRequest = SubmitResponseRequest(
        answers = listOf(
            SubmitResponseRequest.AnswerRequest(
                questionId = UUID.fromString(languageQuestion["id"] as String),
                selectedChoiceIds = setOf(UUID.fromString(languageChoice["id"] as String)),
            ),
            SubmitResponseRequest.AnswerRequest(
                questionId = UUID.fromString(experienceQuestion["id"] as String),
                textValue = experience,
            ),
            SubmitResponseRequest.AnswerRequest(
                questionId = UUID.fromString(ideQuestion["id"] as String),
                selectedChoiceIds = selectedIdes.map { UUID.fromString(it["id"] as String) }.toSet(),
            ),
        ),
    )

    RestAssured
        .given()
        .contentType(ContentType.JSON)
        .body(submitRequest)
        .`when`()
        .post("/api/v1/surveys/$surveyId/responses")
        .then()
        .statusCode(201)
}
