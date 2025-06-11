package com.innercircle.survey.response

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldNotBe
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.TestConstructor

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ResponseSubmitIntegrationTest(
    @LocalServerPort private val port: Int,
) : DescribeSpec({

        beforeSpec {
            RestAssured.port = port
        }

        describe("설문조사 응답 제출 통합 테스트") {
            it("설문조사 생성 후 응답을 제출할 수 있어야 한다") {
                // 1. 설문조사 생성
                val createSurveyRequest =
                    """
                    {
                        "title": "고객 만족도 조사",
                        "description": "서비스 개선을 위한 설문조사입니다.",
                        "questions": [
                            {
                                "title": "서비스 만족도",
                                "description": "전반적인 서비스 만족도를 평가해주세요.",
                                "type": "SINGLE_CHOICE",
                                "required": true,
                                "choices": ["매우 만족", "만족", "보통", "불만족", "매우 불만족"]
                            },
                            {
                                "title": "개선 사항",
                                "description": "서비스 개선이 필요한 부분을 자유롭게 작성해주세요.",
                                "type": "LONG_TEXT",
                                "required": false
                            }
                        ]
                    }
                    """.trimIndent()

                val surveyResponse =
                    RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(createSurveyRequest)
                        .`when`()
                        .post("/api/v1/surveys")
                        .then()
                        .statusCode(201)
                        .extract()
                        .jsonPath()

                val surveyId = surveyResponse.getString("data.id")
                val questionId1 = surveyResponse.getString("data.questions[0].id")
                val choiceId = surveyResponse.getString("data.questions[0].choices[0].id")
                val questionId2 = surveyResponse.getString("data.questions[1].id")

                surveyId shouldNotBe null
                questionId1 shouldNotBe null
                choiceId shouldNotBe null
                questionId2 shouldNotBe null

                // 2. 응답 제출
                val submitResponseRequest =
                    """
                    {
                        "respondentId": "test-user-123",
                        "answers": [
                            {
                                "questionId": "$questionId1",
                                "selectedChoiceIds": ["$choiceId"]
                            },
                            {
                                "questionId": "$questionId2",
                                "textValue": "UI가 더 직관적이면 좋겠습니다."
                            }
                        ]
                    }
                    """.trimIndent()

                RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(submitResponseRequest)
                    .`when`()
                    .post("/api/v1/surveys/$surveyId/responses")
                    .then()
                    .statusCode(201)
                    .body("success", equalTo(true))
                    .body("message", equalTo("응답이 성공적으로 제출되었습니다."))
                    .body("data.surveyId", equalTo(surveyId))
                    .body("data.respondentId", equalTo("test-user-123"))
                    .body("data.answers", hasSize<Any>(2))
                    .body("data.answers[0].questionType", equalTo("SINGLE_CHOICE"))
                    .body("data.answers[1].questionType", equalTo("LONG_TEXT"))
                    .body("data.answers[1].textValue", equalTo("UI가 더 직관적이면 좋겠습니다."))
            }

            it("필수 항목을 누락하면 400 에러가 발생해야 한다") {
                // 1. 필수 질문이 있는 설문조사 생성
                val createSurveyRequest =
                    """
                    {
                        "title": "필수 항목 테스트",
                        "description": "필수 항목 검증 테스트",
                        "questions": [
                            {
                                "title": "필수 질문",
                                "description": "반드시 답변해야 합니다.",
                                "type": "SHORT_TEXT",
                                "required": true
                            }
                        ]
                    }
                    """.trimIndent()

                val surveyResponse =
                    RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(createSurveyRequest)
                        .`when`()
                        .post("/api/v1/surveys")
                        .then()
                        .statusCode(201)
                        .extract()
                        .jsonPath()

                val surveyId = surveyResponse.getString("data.id")

                // 2. 빈 응답 제출
                val submitResponseRequest =
                    """
                    {
                        "answers": []
                    }
                    """.trimIndent()

                RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(submitResponseRequest)
                    .`when`()
                    .post("/api/v1/surveys/$surveyId/responses")
                    .then()
                    .statusCode(400)
                    .body("errorCode", equalTo("COMMON_001")) // validation 에러
            }
        }
    })
