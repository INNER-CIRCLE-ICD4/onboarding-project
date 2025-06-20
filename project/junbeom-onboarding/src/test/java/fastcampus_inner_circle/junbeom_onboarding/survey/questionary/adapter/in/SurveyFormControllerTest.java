package fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.in;

import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.in.SurveyFormController;
import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.in.dto.InsertFormRequest;
import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.in.exception.QuestionaryExceptionHandler;
import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.in.mapper.SurveyFormMapper;
import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.application.service.SurveyFormService;
import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.domain.model.SurveyForm;
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

import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.dto.AnswerRequest;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.dto.AnswerResponse;
import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.in.dto.InsertContentRequest;
import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.in.dto.UpdateFormRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SurveyFormControllerTest {

    @Mock
    private SurveyFormService surveyFormService;

    @InjectMocks
    private SurveyFormController surveyFormController;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
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

    @Test
    void 설문_폼_수정_후_기존_응답_보존_테스트() throws Exception {
        // 1. 설문 폼 생성
        InsertFormRequest insertForm = InsertFormRequest.builder()
                .name("설문1")
                .describe("설명1")
                .contents(List.of(
                        InsertContentRequest.builder().name("문항1").describe("desc1").type("SHORT_TEXT").isRequired(true).options(List.of()).build(),
                        InsertContentRequest.builder().name("문항2").describe("desc2").type("SINGLE_SELECT").isRequired(false).options(List.of("옵션1", "옵션2")).build()
                ))
                .build();
        String formRes = mockMvc.perform(post("/api/surveys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(insertForm)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long formId = objectMapper.readTree(formRes).get("id").asLong();

        // 2. 설문 응답 제출
        AnswerRequest.AnswerDetail answer1 = new AnswerRequest.AnswerDetail();
        answer1.setContentId(1L); answer1.setContentName("문항1"); answer1.setContentDescribe("desc1"); answer1.setType("SHORT_TEXT"); answer1.setValue("답변1");
        AnswerRequest.AnswerDetail answer2 = new AnswerRequest.AnswerDetail();
        answer2.setContentId(2L); answer2.setContentName("문항2"); answer2.setContentDescribe("desc2"); answer2.setType("SINGLE_SELECT");
        AnswerRequest.AnswerDetailOptions option = new AnswerRequest.AnswerDetailOptions();
        option.setOptionId(1L);
        option.setText("옵션1");
        answer2.setOptions(List.of(option));
        AnswerRequest answerRequest = new AnswerRequest();
        answerRequest.setFormId(formId); answerRequest.setFormName("설문1"); answerRequest.setAnswers(List.of(answer1, answer2));
        String answerRes = mockMvc.perform(post("/api/answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(answerRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long answerId = objectMapper.readTree(answerRes).get("answerId").asLong();

        // 3. 설문 폼 수정 (문항1 이름 변경, 문항2 삭제, 문항3 추가)
        UpdateFormRequest.UpdateContentRequest updated1 = UpdateFormRequest.UpdateContentRequest.builder()
                .id(1L).name("문항1-수정").describe("desc1").type("SHORT_TEXT").isRequired(true).options(List.of()).build();
        UpdateFormRequest.UpdateContentRequest newContent = UpdateFormRequest.UpdateContentRequest.builder()
                .name("문항3").describe("desc3").type("LONG_TEXT").isRequired(false).options(List.of()).build();
        UpdateFormRequest updateForm = UpdateFormRequest.builder()
                .name("설문1-수정").describe("설명1-수정")
                .contents(List.of(updated1, newContent))
                .build();
        mockMvc.perform(put("/api/surveys/" + formId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateForm)))
                .andExpect(status().isOk());

        // 4. 기존 응답 조회
        String answerGetRes = mockMvc.perform(get("/api/answers/" + answerId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        AnswerResponse answerResponse = objectMapper.readValue(answerGetRes, AnswerResponse.class);
        assertThat(answerResponse.getFormId()).isEqualTo(formId);
        assertThat(answerResponse.getAnswers()).extracting("contentName").contains("문항1"); // 기존 응답의 문항명은 변경 전 값
        assertThat(answerResponse.getAnswers()).extracting("contentName").doesNotContain("문항3"); // 신규 문항 없음
    }
}
