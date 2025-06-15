package com.innercircle.survey.api.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innercircle.survey.api.dto.request.CreateQuestionRequest;
import com.innercircle.survey.api.dto.request.CreateSurveyRequest;
import com.innercircle.survey.common.domain.QuestionType;
import com.innercircle.survey.domain.survey.Survey;
import com.innercircle.survey.infrastructure.repository.SurveyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 설문조사 통합 테스트
 * 
 * 실제 DB와 전체 애플리케이션 컨텍스트를 사용한 End-to-End 테스트
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@TestMethodOrder(OrderAnnotation.class)
@ActiveProfiles("test")
@Transactional
@DisplayName("설문조사 통합 테스트")
class SurveyIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SurveyRepository surveyRepository;

    @Test
    @DisplayName("설문조사 생성부터 조회까지 전체 플로우 테스트")
    void 설문조사_생성_조회_전체_플로우() throws Exception {
        // Given - 설문조사 생성 요청
        CreateSurveyRequest createRequest = new CreateSurveyRequest(
                "2024년 직원 만족도 조사",
                "직장 생활 만족도와 업무 환경 개선을 위한 조사입니다.",
                List.of(
                        new CreateQuestionRequest(
                                "현재 직무에 만족하십니까?",
                                "전반적인 직무 만족도를 평가해 주세요.",
                                QuestionType.SINGLE_CHOICE,
                                true,
                                List.of("매우 불만족", "불만족", "보통", "만족", "매우 만족")
                        ),
                        new CreateQuestionRequest(
                                "선호하는 근무 형태는?",
                                "가장 선호하는 근무 방식을 모두 선택해 주세요.",
                                QuestionType.MULTIPLE_CHOICE,
                                false,
                                List.of("재택근무", "출근", "하이브리드", "자율근무")
                        ),
                        new CreateQuestionRequest(
                                "이름을 입력해 주세요.",
                                null,
                                QuestionType.SHORT_TEXT,
                                true,
                                null
                        ),
                        new CreateQuestionRequest(
                                "개선사항이나 건의사항을 자유롭게 작성해 주세요.",
                                "더 나은 직장 환경을 위한 의견을 들려주세요.",
                                QuestionType.LONG_TEXT,
                                false,
                                null
                        )
                ),
                "admin@company.com"
        );

        // When 1 - 설문조사 생성
        String createUrl = "http://localhost:" + port + "/api/surveys";
        ResponseEntity<String> createResponse = restTemplate.postForEntity(createUrl, createRequest, String.class);
        
        // Then 1 - 생성된 설문조사 확인
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getBody()).isNotNull();
        
        var createResponseBody = objectMapper.readTree(createResponse.getBody());
        String surveyId = createResponseBody.get("id").asText();
        assertThat(surveyId).isNotEmpty();
        assertThat(createResponseBody.get("title").asText()).isEqualTo("2024년 직원 만족도 조사");
        assertThat(createResponseBody.get("description").asText()).isEqualTo("직장 생활 만족도와 업무 환경 개선을 위한 조사입니다.");
        assertThat(createResponseBody.get("questions")).isNotNull();
        assertThat(createResponseBody.get("questions")).hasSize(4);

        // DB에서 실제 저장 확인
        Survey savedSurvey = surveyRepository.findByIdWithActiveQuestions(surveyId).orElse(null);
        assertThat(savedSurvey).isNotNull();
        assertThat(savedSurvey.getTitle()).isEqualTo("2024년 직원 만족도 조사");
        assertThat(savedSurvey.getActiveQuestions()).hasSize(4);

        // When 2 - 설문조사 조회
        String getUrl = "http://localhost:" + port + "/api/surveys/" + surveyId;
        ResponseEntity<String> getResponse = restTemplate.getForEntity(getUrl, String.class);
        
        // Then 2 - 조회 결과 확인
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        
        var getResponseBody = objectMapper.readTree(getResponse.getBody());
        assertThat(getResponseBody.get("id").asText()).isEqualTo(surveyId);
        assertThat(getResponseBody.get("title").asText()).isEqualTo("2024년 직원 만족도 조사");
        assertThat(getResponseBody.get("questions")).hasSize(4);

        // 질문 순서 및 타입 확인
        var questions = getResponseBody.get("questions");
        assertThat(questions.get(0).get("questionType").asText()).isEqualTo("SINGLE_CHOICE");
        assertThat(questions.get(0).get("required").asBoolean()).isTrue();
        assertThat(questions.get(0).get("displayOrder").asInt()).isEqualTo(1);
        
        assertThat(questions.get(1).get("questionType").asText()).isEqualTo("MULTIPLE_CHOICE");
        assertThat(questions.get(1).get("required").asBoolean()).isFalse();
        assertThat(questions.get(1).get("displayOrder").asInt()).isEqualTo(2);

        // When 3 - 설문조사 존재 확인
        String existsUrl = "http://localhost:" + port + "/api/surveys/" + surveyId + "/exists";
        ResponseEntity<String> existsResponse = restTemplate.getForEntity(existsUrl, String.class);
        
        // Then 3 - 존재 확인 결과
        assertThat(existsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(existsResponse.getBody()).isNotNull();
        
        var existsResponseBody = objectMapper.readTree(existsResponse.getBody());
        assertThat(existsResponseBody.get("exists").asBoolean()).isTrue();
        assertThat(existsResponseBody.get("surveyId").asText()).isEqualTo(surveyId);
    }

    @Test
    @DisplayName("존재하지 않는 설문조사 조회 시 404 Not Found 응답")
    void 존재하지_않는_설문조사_조회_실패() throws Exception {
        // Given
        String nonExistentSurveyId = "01HK123NONEXISTENT456789";

        // When
        String getUrl = "http://localhost:" + port + "/api/surveys/" + nonExistentSurveyId;
        ResponseEntity<String> response = restTemplate.getForEntity(getUrl, String.class);
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        
        var responseBody = objectMapper.readTree(response.getBody());
        assertThat(responseBody.get("error").asText()).isEqualTo("SURVEY_NOT_FOUND");
        assertThat(responseBody.get("message").asText()).contains("설문조사를 찾을 수 없습니다");
        assertThat(responseBody.get("message").asText()).contains(nonExistentSurveyId);
    }

    @Test
    @DisplayName("존재하지 않는 설문조사 존재 확인 시 false 응답")
    void 존재하지_않는_설문조사_존재_확인() {
        // Given
        String nonExistentSurveyId = "01HK123NONEXISTENT456789";

        // When
        String existsUrl = "http://localhost:" + port + "/api/surveys/" + nonExistentSurveyId + "/exists";
        ResponseEntity<String> response = restTemplate.getForEntity(existsUrl, String.class);
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        try {
            var responseBody = objectMapper.readTree(response.getBody());
            assertThat(responseBody.get("exists").asBoolean()).isFalse();
            assertThat(responseBody.get("surveyId").asText()).isEqualTo(nonExistentSurveyId);
        } catch (Exception e) {
            throw new RuntimeException("JSON 파싱 실패", e);
        }
    }

    @Test
    @DisplayName("다양한 질문 타입을 포함한 복합 설문조사 생성 및 검증")
    void 복합_설문조사_생성_검증() throws Exception {
        // Given - 모든 질문 타입을 포함한 설문조사
        CreateSurveyRequest request = new CreateSurveyRequest(
                "종합 피드백 설문조사",
                "다양한 질문 형태를 통한 종합적인 피드백 수집",
                List.of(
                        new CreateQuestionRequest(
                                "이름",
                                "성함을 입력해 주세요.",
                                QuestionType.SHORT_TEXT,
                                true,
                                null
                        ),
                        new CreateQuestionRequest(
                                "서비스 만족도",
                                "전반적인 서비스 만족도를 선택해 주세요.",
                                QuestionType.SINGLE_CHOICE,
                                true,
                                List.of("1점", "2점", "3점", "4점", "5점")
                        ),
                        new CreateQuestionRequest(
                                "관심 분야",
                                "관심 있는 분야를 모두 선택해 주세요.",
                                QuestionType.MULTIPLE_CHOICE,
                                false,
                                List.of("기술", "디자인", "마케팅", "영업", "경영", "기타")
                        ),
                        new CreateQuestionRequest(
                                "상세 의견",
                                "서비스에 대한 상세한 의견을 작성해 주세요.",
                                QuestionType.LONG_TEXT,
                                false,
                                null
                        )
                ),
                "tester@company.com"
        );

        // When
        String url = "http://localhost:" + port + "/api/surveys";
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        
        var responseBody = objectMapper.readTree(response.getBody());
        String surveyId = responseBody.get("id").asText();
        assertThat(surveyId).isNotEmpty();

        // DB 검증
        Survey survey = surveyRepository.findByIdWithActiveQuestions(surveyId).orElse(null);
        assertThat(survey).isNotNull();
        assertThat(survey.getActiveQuestions()).hasSize(4);

        var questions = survey.getActiveQuestions();
        
        // SHORT_TEXT 검증
        assertThat(questions.get(0).getQuestionType()).isEqualTo(QuestionType.SHORT_TEXT);
        assertThat(questions.get(0).isRequired()).isTrue();
        assertThat(questions.get(0).getOptions()).isEmpty();

        // SINGLE_CHOICE 검증
        assertThat(questions.get(1).getQuestionType()).isEqualTo(QuestionType.SINGLE_CHOICE);
        assertThat(questions.get(1).getOptions()).hasSize(5);
        assertThat(questions.get(1).getOptions()).contains("1점", "5점");

        // MULTIPLE_CHOICE 검증
        assertThat(questions.get(2).getQuestionType()).isEqualTo(QuestionType.MULTIPLE_CHOICE);
        assertThat(questions.get(2).getOptions()).hasSize(6);
        assertThat(questions.get(2).getOptions()).contains("기술", "디자인", "기타");

        // LONG_TEXT 검증
        assertThat(questions.get(3).getQuestionType()).isEqualTo(QuestionType.LONG_TEXT);
        assertThat(questions.get(3).isRequired()).isFalse();
        assertThat(questions.get(3).getOptions()).isEmpty();
    }

    @Test
    @DisplayName("잘못된 요청 데이터로 설문조사 생성 시 400 Bad Request 응답")
    void 잘못된_요청_데이터_검증() throws Exception {
        // Given - 제목이 없는 잘못된 요청
        CreateSurveyRequest invalidRequest = new CreateSurveyRequest(
                "", // 빈 제목
                "설명",
                List.of(
                        new CreateQuestionRequest(
                                "질문",
                                null,
                                QuestionType.SHORT_TEXT,
                                false,
                                null
                        )
                ),
                "tester@company.com"
        );

        // When
        String url = "http://localhost:" + port + "/api/surveys";
        ResponseEntity<String> response = restTemplate.postForEntity(url, invalidRequest, String.class);
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        
        var responseBody = objectMapper.readTree(response.getBody());
        assertThat(responseBody.get("error").asText()).isEqualTo("INVALID_REQUEST");
        assertThat(responseBody.get("message").asText()).contains("입력 데이터가 유효하지 않습니다");
    }

    @Test
    @DisplayName("질문 개수 초과 시 400 Bad Request 응답")
    void 질문_개수_초과_검증() throws Exception {
        // Given - 11개의 질문 (최대 10개 초과)
        List<CreateQuestionRequest> tooManyQuestions = List.of(
                new CreateQuestionRequest("질문1", null, QuestionType.SHORT_TEXT, false, null),
                new CreateQuestionRequest("질문2", null, QuestionType.SHORT_TEXT, false, null),
                new CreateQuestionRequest("질문3", null, QuestionType.SHORT_TEXT, false, null),
                new CreateQuestionRequest("질문4", null, QuestionType.SHORT_TEXT, false, null),
                new CreateQuestionRequest("질문5", null, QuestionType.SHORT_TEXT, false, null),
                new CreateQuestionRequest("질문6", null, QuestionType.SHORT_TEXT, false, null),
                new CreateQuestionRequest("질문7", null, QuestionType.SHORT_TEXT, false, null),
                new CreateQuestionRequest("질문8", null, QuestionType.SHORT_TEXT, false, null),
                new CreateQuestionRequest("질문9", null, QuestionType.SHORT_TEXT, false, null),
                new CreateQuestionRequest("질문10", null, QuestionType.SHORT_TEXT, false, null),
                new CreateQuestionRequest("질문11", null, QuestionType.SHORT_TEXT, false, null)
        );

        CreateSurveyRequest request = new CreateSurveyRequest(
                "질문 개수 초과 테스트",
                "11개 질문 테스트",
                tooManyQuestions,
                "tester@company.com"
        );

        // When
        String url = "http://localhost:" + port + "/api/surveys";
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        
        var responseBody = objectMapper.readTree(response.getBody());
        assertThat(responseBody.get("error").asText()).isEqualTo("INVALID_REQUEST");
    }
}
