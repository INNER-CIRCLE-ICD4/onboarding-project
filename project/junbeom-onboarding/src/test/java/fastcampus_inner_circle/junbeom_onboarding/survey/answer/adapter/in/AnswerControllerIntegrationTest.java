package fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.dto.AnswerRequest;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.dto.AnswerResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AnswerControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("응답 제출 및 조회 성공")
    void submitAndGetAnswer_success() throws Exception {
        // given
        AnswerRequest.AnswerDto answerDto = new AnswerRequest.AnswerDto();
        answerDto.setContentId(1L);
        answerDto.setOptionId(2L);
        answerDto.setAnswerValue("주관식 답변");

        AnswerRequest request = new AnswerRequest();
        request.setFormId(100L);
        request.setAnswers(List.of(answerDto));

        // when: 응답 제출
        String responseBody = mockMvc.perform(post("/api/answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.formId").value(100L))
                .andExpect(jsonPath("$.answers[0].contentId").value(1L))
                .andReturn().getResponse().getContentAsString();

        AnswerResponse response = objectMapper.readValue(responseBody, AnswerResponse.class);
        Long answerId = response.getAnswerId();

        // then: 응답 조회 (answerId 기준)
        mockMvc.perform(get("/api/answers/{answerId}", answerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.formId").value(100L))
                .andExpect(jsonPath("$.answers[0].contentId").value(1L));
    }

    @Test
    @DisplayName("DB에 실제로 응답지 insert 테스트")
    @Rollback(false)
    void insertAnswerToDb_success() throws Exception {
        // given
        AnswerRequest.AnswerDto answerDto = new AnswerRequest.AnswerDto();
        answerDto.setContentId(1L);
        answerDto.setOptionId(2L);
        answerDto.setAnswerValue("주관식 답변");

        AnswerRequest request = new AnswerRequest();
        request.setFormId(200L);
        request.setAnswers(List.of(answerDto));

        // when: 응답 제출
        String responseBody = mockMvc.perform(post("/api/answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        AnswerResponse response = objectMapper.readValue(responseBody, AnswerResponse.class);
        Long answerId = response.getAnswerId();

        // then: DB에서 조회하여 실제 저장된 데이터 검증 (answerId 기준)
        mockMvc.perform(get("/api/answers/{answerId}", answerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.formId").value(200L))
                .andExpect(jsonPath("$.answers[0].contentId").value(1L))
                .andExpect(jsonPath("$.answers[0].optionId").value(2L))
                .andExpect(jsonPath("$.answers[0].answerValue").value("주관식 답변"));
    }

    @Test
    @DisplayName("필수값 누락시 예외 발생")
    void submitAnswer_missingRequiredField() throws Exception {
        // given: formId 누락
        AnswerRequest request = new AnswerRequest();
        request.setAnswers(List.of());

        // when & then
        mockMvc.perform(post("/api/answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("존재하지 않는 answerId로 조회시 404 반환")
    void getAnswer_notFound() throws Exception {
        mockMvc.perform(get("/api/answers/{answerId}", 99999L))
                .andExpect(status().isNotFound());
    }
} 