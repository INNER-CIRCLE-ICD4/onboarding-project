package com.onboarding.api;

import com.onboarding.api.common.BaseControllerTest;
import com.onboarding.api.service.SurveyService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;

import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SurveyControllerTest extends BaseControllerTest {

    private SurveyService surveyService;

    @Test
    public void indexExample() throws Exception {
        RestAssured.given(documentationSpec).log().all()
//                .filter() // 적용될 RestDocumentationFilter 설정
                .filter(document("survey"))
                .when()
                .get("/api/surveys")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 설문지_등록_요청이_성공하면_200을_반환한다() {
        // given
        String requestJson = """
        {
          "title": "고객 만족도 조사",
          "description": "제품에 대한 만족도를 조사합니다.",
          "items": [
            {
              "title": "제품에 얼마나 만족하시나요?",
              "description": "하나만 선택할 수 있습니다.",
              "type": "SINGLE_CHOICE",
              "required": true,
              "options": [
                { "label": "매우 만족" },
                { "label": "만족" },
                { "label": "보통" },
                { "label": "불만족" }
              ]
            },
            {
              "title": "개선할 점이 있다면?",
              "type": "LONG_TEXT",
              "required": false
            }
          ]
        }
        """;

        RestAssured.given(documentationSpec).log().all()
//                .filter() // 적용될 RestDocumentationFilter 설정
                .filter(document("survey/create"))
                    .contentType("application/json")
                    .body(requestJson)
                .when()
                    .post("/api/surveys")
                .then().log().all()
                .statusCode(200);
    }
}