package fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.dto.AnswerRequest;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.dto.AnswerResponse;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.application.service.AnswerService;
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
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AnswerControllerTest {
    @Mock
    private AnswerService answerService;
    @InjectMocks
    private AnswerController answerController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(answerController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("응답 제출 API - 정상 케이스")
    void submitAnswer_ValidRequest_ReturnsResult() throws Exception {
        String json = """
        {
          \"formId\": 1,
          \"answers\": [
            {\"contentId\": 10, \"answerValue\": \"홍길동\"},
            {\"contentId\": 11, \"optionId\": 100, \"answerValue\": null}
          ]
        }
        """;
        AnswerResponse resultDto = new AnswerResponse();
        resultDto.setAnswerId(1L);
        resultDto.setFormId(1L);
        resultDto.setSubmittedAt(LocalDateTime.now());
        AnswerResponse.AnswerDetailDto answer1 = new AnswerResponse.AnswerDetailDto();
        answer1.setContentId(10L);
        answer1.setAnswerValue("홍길동");
        AnswerResponse.AnswerDetailDto answer2 = new AnswerResponse.AnswerDetailDto();
        answer2.setContentId(11L);
        answer2.setOptionId(100L);
        resultDto.setAnswers(List.of(answer1, answer2));
        when(answerService.submitAnswer(any(AnswerRequest.class))).thenReturn(resultDto);
        mockMvc.perform(post("/api/answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.formId").value(1));
    }

    @Test
    @DisplayName("응답 조회 API - 정상 케이스")
    void getAnswers_ValidRequest_ReturnsList() throws Exception {
        AnswerResponse resultDto = new AnswerResponse();
        resultDto.setAnswerId(1L);
        resultDto.setFormId(1L);
        resultDto.setSubmittedAt(LocalDateTime.now());
        AnswerResponse.AnswerDetailDto answer1 = new AnswerResponse.AnswerDetailDto();
        answer1.setContentId(10L);
        answer1.setAnswerValue("홍길동");
        resultDto.setAnswers(List.of(answer1));
        when(answerService.getAnswers(1L)).thenReturn(List.of(resultDto));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/answers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].formId").value(1));
    }

    @Test
    @DisplayName("응답 제출 API - 필수값 누락시 400 반환")
    void submitAnswer_MissingRequiredField_ReturnsBadRequest() throws Exception {
        String json = """
        {
          \"formId\": null,
          \"answers\": []
        }
        """;
        mockMvc.perform(post("/api/answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("formId와 answers는 필수입니다."));
    }
} 