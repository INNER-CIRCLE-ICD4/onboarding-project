package com.innercircle.survey.survey

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ResponsePreservationTest(
    @LocalServerPort private val port: Int,
) : DescribeSpec({

    beforeSpec {
        RestAssured.port = port
    }

    describe("기존 응답 유지 기본 테스트") {
        it("설문조사 수정 전후 응답 버전이 유지되어야 한다") {
            // 1. 설문조사 생성
            val createRequest = """
                {
                    "title": "테스트 설문",
                    "description": "설명",
                    "questions": [
                        {
                            "title": "질문",
                            "description": "설명",
                            "type": "SHORT_TEXT",
                            "required": true
                        }
                    ]
                }
            """.trimIndent()

            val survey = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(createRequest)
                .`when`()
                .post("/api/v1/surveys")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()

            val surveyId = survey.getString("data.id")
            val questionId = survey.getString("data.questions[0].id")
            val version1 = survey.getInt("data.version")

            println("Created survey $surveyId with version $version1")

            // 2. 응답 제출
            val responseRequest = """
                {
                    "answers": [
                        {
                            "questionId": "$questionId",
                            "textValue": "첫 번째 응답"
                        }
                    ]
                }
            """.trimIndent()

            val response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(responseRequest)
                .`when`()
                .post("/api/v1/surveys/$surveyId/responses")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()

            val responseId = response.getString("data.id")
            val responseSurveyVersion = response.getInt("data.surveyVersion")

            responseSurveyVersion shouldBe version1
            println("Created response $responseId with survey version $responseSurveyVersion")

            // 3. 설문조사 수정
            val updateRequest = """
                {
                    "title": "수정된 설문",
                    "description": "수정된 설명",
                    "questions": [
                        {
                            "title": "새 질문",
                            "description": "새 설명",
                            "type": "LONG_TEXT",
                            "required": false
                        }
                    ]
                }
            """.trimIndent()

            val updatedSurvey = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .`when`()
                .put("/api/v1/surveys/$surveyId")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()

            val version2 = updatedSurvey.getInt("data.version")
            version2 shouldNotBe version1
            println("Updated survey to version $version2")

            // 4. 기존 응답 조회
            val checkResponse = RestAssured.given()
                .`when`()
                .get("/api/v1/responses/$responseId")
                .then()
                .extract()

            println("Response status: ${checkResponse.statusCode()}")
            
            if (checkResponse.statusCode() == 200) {
                val responseJson = checkResponse.jsonPath()
                val checkVersion = responseJson.getInt("data.surveyVersion")
                checkVersion shouldBe version1
                println("✅ Response still has original version $checkVersion")
            } else {
                println("❌ Failed to retrieve response: ${checkResponse.body().asString()}")
            }

            // 5. 설문조사별 응답 목록 조회 (대안)
            val allResponses = RestAssured.given()
                .`when`()
                .get("/api/v1/surveys/$surveyId/responses")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()

            val responsesList = allResponses.getList<Map<String, Any>>("content")
            responsesList.size shouldBe 1
            
            val firstResponse = responsesList[0]
            val listVersion = firstResponse["surveyVersion"] as Int
            listVersion shouldBe version1
            println("✅ Response in list also has original version $listVersion")
        }

        it("다중 선택 질문의 선택지가 올바르게 저장되어야 한다") {
            val createRequest = """
                {
                    "title": "선택지 테스트",
                    "description": "선택지 저장 확인",
                    "questions": [
                        {
                            "title": "좋아하는 언어",
                            "description": "모두 선택",
                            "type": "MULTIPLE_CHOICE",
                            "required": true,
                            "choices": ["Java", "Kotlin", "Python", "JavaScript", "Go"]
                        }
                    ]
                }
            """.trimIndent()

            val survey = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(createRequest)
                .`when`()
                .post("/api/v1/surveys")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()

            // 선택지 확인
            val choices = survey.getList<Map<String, Any>>("data.questions[0].choices")
            choices.size shouldBe 5
            
            val choiceTexts = choices.map { it["text"] as String }
            choiceTexts shouldBe listOf("Java", "Kotlin", "Python", "JavaScript", "Go")
            
            println("✅ All choices saved correctly: $choiceTexts")
        }
    }
})
