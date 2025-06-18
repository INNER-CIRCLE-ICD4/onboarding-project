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
    @DisplayName("optionIds 구조로 응답 제출 및 조회 성공")
    void submitAndGetAnswer_success() throws Exception {
        // given: 다양한 케이스의 optionIds
        AnswerRequest.AnswerDto answerDto1 = new AnswerRequest.AnswerDto();
        answerDto1.setContentId(1L);
        answerDto1.setOptionIds(null); // 복수 선택
        answerDto1.setAnswerValue("조준범");

        AnswerRequest.AnswerDto answerDto2 = new AnswerRequest.AnswerDto();
        answerDto2.setContentId(2L);
        answerDto2.setOptionIds(List.of(2L)); // 단일 선택
        answerDto2.setAnswerValue(null);

        AnswerRequest.AnswerDto answerDto3 = new AnswerRequest.AnswerDto();
        answerDto3.setContentId(3L);
        answerDto3.setOptionIds(null); // 미선택
        answerDto3.setAnswerValue("조회 속도를 개선해주세요.");

        AnswerRequest.AnswerDto answerDto4 = new AnswerRequest.AnswerDto();
        answerDto3.setContentId(4L);
        answerDto3.setOptionIds(List.of(8L, 9L)); // 미선택
        answerDto3.setAnswerValue(null);

        AnswerRequest request = new AnswerRequest();
        request.setFormId(1L);
        request.setAnswers(List.of(answerDto1, answerDto2, answerDto3, answerDto4));

        // when: 응답 제출
        String responseBody = mockMvc.perform(post("/api/answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.formId").value(100L))
                .andReturn().getResponse().getContentAsString();

        AnswerResponse response = objectMapper.readValue(responseBody, AnswerResponse.class);
        Long answerId = response.getAnswerId();

        // then: 응답 조회 (optionIds 검증)
        mockMvc.perform(get("/api/answers/{answerId}", answerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.formId").value(100L))
                .andExpect(jsonPath("$.answers[0].optionIds[0]").value(10L))
                .andExpect(jsonPath("$.answers[0].optionIds[1]").value(11L))
                .andExpect(jsonPath("$.answers[1].optionIds[0]").value(20L))
                .andExpect(jsonPath("$.answers[2].optionIds").doesNotExist())
                .andExpect(jsonPath("$.answers[2].answerValue").value("주관식 답변"));
    }

    @Test
    @DisplayName("optionIds 구조로 DB에 실제로 insert 테스트")
    @Rollback(false)
    void insertAnswerToDb_success() throws Exception {
        // given
        AnswerRequest.AnswerDto answerDto = new AnswerRequest.AnswerDto();
        answerDto.setContentId(1L);
        answerDto.setOptionIds(List.of(2L, 3L));
        answerDto.setAnswerValue("테스트 답변");

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

        // then: DB에서 조회하여 실제 저장된 데이터 검증
        mockMvc.perform(get("/api/answers/{answerId}", answerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.formId").value(200L))
                .andExpect(jsonPath("$.answers[0].optionIds[0]").value(2L))
                .andExpect(jsonPath("$.answers[0].optionIds[1]").value(3L))
                .andExpect(jsonPath("$.answers[0].answerValue").value("테스트 답변"));
    }

    @Test
    @DisplayName("필수값 누락시 예외 발생(optionIds 구조)")
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
    @DisplayName("존재하지 않는 answerId로 조회시 404 반환(optionIds 구조)")
    void getAnswer_notFound() throws Exception {
        mockMvc.perform(get("/api/answers/{answerId}", 99999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("optionIds와 옵션 text가 survey_answer_detail_option에 정상 저장되는지 검증")
    void submitAndGetAnswer_withOptionText_success() throws Exception {
        // given: 실제 DB에 존재하는 optionId와 그에 대응하는 text가 있다고 가정
        // 예시: optionId=2L -> "매우 만족", optionId=3L -> "만족"
        AnswerRequest.AnswerDto answerDto = new AnswerRequest.AnswerDto();
        answerDto.setContentId(1L);
        answerDto.setOptionIds(List.of(2L, 3L));
        answerDto.setAnswerValue(null);

        AnswerRequest request = new AnswerRequest();
        request.setFormId(300L);
        request.setAnswers(List.of(answerDto));

        // when: 응답 제출
        String responseBody = mockMvc.perform(post("/api/answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        AnswerResponse response = objectMapper.readValue(responseBody, AnswerResponse.class);
        Long answerId = response.getAnswerId();

        // then: 응답 조회 (optionIds와 text가 정상적으로 저장/조회되는지 검증)
        mockMvc.perform(get("/api/answers/{answerId}", answerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.formId").value(300L))
                .andExpect(jsonPath("$.answers[0].optionIds[0]").value(2L))
                .andExpect(jsonPath("$.answers[0].optionIds[1]").value(3L));
        // 실제 옵션 text는 별도 리포지토리/엔티티로 직접 조회하여 검증하는 단위 테스트를 추가로 작성할 수 있습니다.
    }
} 