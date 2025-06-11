package com.fastcampus.survey.questionary.adapter.in;

import com.fastcampus.survey.questionary.adapter.in.dto.InsertFormRequest;
import com.fastcampus.survey.questionary.adapter.in.exception.QuestionaryExceptionHandler;
import com.fastcampus.survey.questionary.adapter.in.mapper.SurveyFormMapper;
import com.fastcampus.survey.questionary.application.service.SurveyFormService;
import com.fastcampus.survey.questionary.domain.model.SurveyForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SurveyFormControllerTest {

    @Mock
    private SurveyFormService surveyFormService;

    @InjectMocks
    private SurveyFormController surveyFormController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(surveyFormController)
                .setControllerAdvice(new QuestionaryExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("DTO객체가 생성되는지 테스트")
    void createSurvey_ValidRequest_ReturnsSurveyForm() throws Exception {
        String json = """
        {
          "name": "고객 경험 조사",
          "describe": "고객 경험을 조사하기 위한 설문지입니다.",
          "contents": [
            {
              "name": "이름",
              "describe": "귀하의 성함을 입력해 주세요.",
              "type": "short_answer",
              "isRequired": true,
              "options": null
            },
            {
              "name": "만족도",
              "describe": "서비스 만족도를 선택해 주세요.",
              "type": "radio",
              "isRequired": true,
              "options": [
                "매우 만족",
                "만족",
                "보통",
                "불만족",
                "매우 불만족"
              ]
            },
            {
              "name": "개선 의견",
              "describe": "서비스 개선을 위한 의견을 작성해 주세요.",
              "type": "long_answer",
              "isRequired": false,
              "options": null
            },
            {
              "name": "선호 기능",
              "describe": "사용한 기능 중 선호하는 항목을 선택해 주세요.",
              "type": "checkbox",
              "isRequired": false,
              "options": [
                "빠른 응답",
                "친절한 지원",
                "사용 편의성",
                "다양한 옵션"
              ]
            }
          ]
        }
        """;

        // DTO 객체 생성 및 로그 출력
        InsertFormRequest requestDto = objectMapper.readValue(json, InsertFormRequest.class);

        System.out.println("Parsed InsertFormRequestDto: " + requestDto);

        SurveyForm surveyForm = SurveyFormMapper.toSurveyForm(objectMapper.readValue(json, InsertFormRequest.class));
        when(surveyFormService.createSurveyForm(any(InsertFormRequest.class))).thenReturn(surveyForm);

        mockMvc.perform(post("/api/surveys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("고객 경험 조사"))
                .andExpect(jsonPath("$.contents[0].type").value("SHORT_ANSWER"));
    }

    @Test
    @DisplayName("DTO객체가 생성되는지 테스트 - 무효한 요청 (항목 수 초과)")
    void createSurvey_InvalidRequest_TooManyContents_ReturnsBadRequest() throws Exception {
        String json = """
            {
              "name": "고객 경험 조사",
              "describe": "고객 경험을 조사하기 위한 설문지입니다.",
              "contents": [
                {"name": "항목1", "type": "short_answer", "isRequired": true, "options": null},
                {"name": "항목2", "type": "short_answer", "isRequired": true, "options": null},
                {"name": "항목3", "type": "short_answer", "isRequired": true, "options": null},
                {"name": "항목4", "type": "short_answer", "isRequired": true, "options": null},
                {"name": "항목5", "type": "short_answer", "isRequired": true, "options": null},
                {"name": "항목6", "type": "short_answer", "isRequired": true, "options": null},
                {"name": "항목7", "type": "short_answer", "isRequired": true, "options": null},
                {"name": "항목8", "type": "short_answer", "isRequired": true, "options": null},
                {"name": "항목9", "type": "short_answer", "isRequired": true, "options": null},
                {"name": "항목10", "type": "short_answer", "isRequired": true, "options": null},
                {"name": "항목11", "type": "short_answer", "isRequired": true, "options": null}
              ]
            }
            """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/surveys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.message").value("contents: 설문 항목은 1개 이상 10개 이하로 입력해야 합니다."));
    }





}
