package com.innercircle.survey.survey

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SurveyUpdateIntegrationTest(
    @LocalServerPort private val port: Int,
) : DescribeSpec({

        beforeSpec {
            RestAssured.port = port
        }

        describe("설문조사 수정 통합 테스트") {
            context("설문조사를 생성하고 수정할 때") {
                it("기존 질문이 soft delete 되고 새로운 질문이 추가되어야 한다") {
                    // Step 1: 설문조사 생성
                    val createRequestBody =
                        """
                        {
                            "title": "초기 설문조사",
                            "description": "설문조사 설명",
                            "questions": [
                                {
                                    "title": "첫 번째 질문",
                                    "description": "첫 번째 질문 설명",
                                    "type": "SHORT_TEXT",
                                    "required": true
                                },
                                {
                                    "title": "두 번째 질문",
                                    "description": "두 번째 질문 설명",
                                    "type": "SINGLE_CHOICE",
                                    "required": false,
                                    "choices": ["예", "아니오"]
                                }
                            ]
                        }
                        """.trimIndent()

                    val createResponse =
                        RestAssured.given()
                            .contentType(ContentType.JSON)
                            .body(createRequestBody)
                            .`when`()
                            .post("/api/v1/surveys")
                            .then()
                            .statusCode(201)
                            .extract()

                    val surveyId = createResponse.path<String>("data.id")
                    val initialVersion = createResponse.path<Int>("data.version")

                    // 초기 상태 확인
                    createResponse.path<List<Any>>("data.questions").size shouldBe 2

                    // Step 2: 설문조사 수정
                    val updateRequestBody =
                        """
                        {
                            "title": "수정된 설문조사",
                            "description": "수정된 설명",
                            "questions": [
                                {
                                    "title": "새로운 첫 번째 질문",
                                    "description": "새로운 질문 설명",
                                    "type": "MULTIPLE_CHOICE",
                                    "required": true,
                                    "choices": ["A", "B", "C", "D"]
                                },
                                {
                                    "title": "새로운 두 번째 질문",
                                    "description": "장문형 질문",
                                    "type": "LONG_TEXT",
                                    "required": false
                                },
                                {
                                    "title": "새로운 세 번째 질문",
                                    "description": "추가된 질문",
                                    "type": "SHORT_TEXT",
                                    "required": true
                                }
                            ]
                        }
                        """.trimIndent()

                    val updateResponse =
                        RestAssured.given()
                            .contentType(ContentType.JSON)
                            .body(updateRequestBody)
                            .`when`()
                            .put("/api/v1/surveys/$surveyId")
                            .then()
                            .statusCode(200)
                            .body("success", equalTo(true))
                            .body("message", equalTo("설문조사가 성공적으로 수정되었습니다."))
                            .body("data.title", equalTo("수정된 설문조사"))
                            .body("data.questions", hasSize<Any>(3))
                            .extract()

                    val updatedVersion = updateResponse.path<Int>("data.version")

                    // 버전이 증가했는지 확인
                    updatedVersion shouldBe initialVersion + 2 // update + updateQuestions

                    // Step 3: 수정된 설문조사 확인
                    // 조회 API에서는 삭제된 질문이 보이지 않는지 확인
                    RestAssured.given()
                        .`when`()
                        .get("/api/v1/surveys/$surveyId")
                        .then()
                        .statusCode(200)
                        .body("data.questions", hasSize<Any>(3))
                        .body("data.questions[0].title", equalTo("새로운 첫 번째 질문"))
                        .body("data.version", equalTo(updatedVersion))
                }

                it("질문이 없는 설문조사로 수정할 수 없어야 한다") {
                    // 설문조사 생성
                    val createRequestBody =
                        """
                        {
                            "title": "초기 설문조사",
                            "description": "설명",
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

                    val surveyId =
                        RestAssured.given()
                            .contentType(ContentType.JSON)
                            .body(createRequestBody)
                            .`when`()
                            .post("/api/v1/surveys")
                            .then()
                            .statusCode(201)
                            .extract()
                            .path<String>("data.id")

                    // 빈 질문 목록으로 수정 시도
                    val updateRequestBody =
                        """
                        {
                            "title": "수정된 설문조사",
                            "description": "수정된 설명",
                            "questions": []
                        }
                        """.trimIndent()

                    RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(updateRequestBody)
                        .`when`()
                        .put("/api/v1/surveys/$surveyId")
                        .then()
                        .statusCode(400)
                }
            }
        }
    })
