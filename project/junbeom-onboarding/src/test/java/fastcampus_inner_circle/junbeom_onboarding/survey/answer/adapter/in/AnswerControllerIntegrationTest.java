package fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.dto.AnswerRequest;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.dto.AnswerResponse;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AnswerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("설문 답변을 성공적으로 제출한다 (DB Insert)")
    @Rollback(false)
    void submitAnswer_Success() throws Exception {
        // given: 사용자가 제공한 테스트 데이터 기반으로 요청 생성
        // 1. 이름 (주관식)
        AnswerRequest.AnswerDetail nameAnswer = new AnswerRequest.AnswerDetail();
        nameAnswer.setContentId(1L);
        nameAnswer.setContentName("이름");
        nameAnswer.setContentDescribe("귀하의 성함을 입력해 주세요.");
        nameAnswer.setType("short_answer");
        nameAnswer.setValue("홍길동");
        nameAnswer.setOptions(List.of());

        // 2. 만족도 (객관식 - 단일 선택)
        AnswerRequest.AnswerDetail satisfactionAnswer = new AnswerRequest.AnswerDetail();
        satisfactionAnswer.setContentId(2L);
        satisfactionAnswer.setContentName("만족도");
        satisfactionAnswer.setContentDescribe("서비스 만족도를 선택해 주세요.");
        satisfactionAnswer.setType("radio");
        AnswerRequest.AnswerDetailOptions satisfactionOption = new AnswerRequest.AnswerDetailOptions();
        satisfactionOption.setOptionId(2L);
        satisfactionOption.setText("매우 만족");
        satisfactionAnswer.setOptions(List.of(satisfactionOption));

        // 3. 개선 의견 (주관식)
        AnswerRequest.AnswerDetail suggestionAnswer = new AnswerRequest.AnswerDetail();
        suggestionAnswer.setContentId(3L);
        suggestionAnswer.setContentName("개선 의견");
        suggestionAnswer.setContentDescribe("서비스 개선을 위한 의견을 작성해 주세요.");
        suggestionAnswer.setType("long_answer");
        suggestionAnswer.setValue("더 다양한 혜택이 있었으면 좋겠습니다.");
        suggestionAnswer.setOptions(List.of());

        // 4. 선호 기능 (객관식 - 복수 선택)
        AnswerRequest.AnswerDetail featureAnswer = new AnswerRequest.AnswerDetail();
        featureAnswer.setContentId(4L);
        featureAnswer.setContentName("선호 기능");
        featureAnswer.setContentDescribe("사용한 기능 중 선호하는 항목을 선택해 주세요.");
        featureAnswer.setType("checkbox");
        AnswerRequest.AnswerDetailOptions featureOption1 = new AnswerRequest.AnswerDetailOptions();
        featureOption1.setOptionId(9L);
        featureOption1.setText("친절한 지원");
        AnswerRequest.AnswerDetailOptions featureOption2 = new AnswerRequest.AnswerDetailOptions();
        featureOption2.setOptionId(11L);
        featureOption2.setText("다양한 옵션");
        featureAnswer.setOptions(List.of(featureOption1, featureOption2));

        AnswerRequest request = new AnswerRequest();
        request.setFormId(1L);
        request.setFormName("고객 경험을 조사하기 위한 설문지입니다.");
        request.setAnswers(List.of(nameAnswer, satisfactionAnswer, suggestionAnswer, featureAnswer));

        // when & then: 답변을 제출하고, 200 OK와 함께 answerId가 반환되는지 확인
        mockMvc.perform(post("/api/answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answerId").exists())
                .andExpect(jsonPath("$.formId").value(1L))
                .andDo(print());
    }

    @Test
    @DisplayName("formId가 없으면 답변 제출 시 400 에러가 발생한다")
    void submitAnswer_Fails_When_FormId_Is_Null() throws Exception {
        // given
        AnswerRequest request = new AnswerRequest();
        request.setFormId(null); // formId 누락
        request.setAnswers(List.of());

        // when & then
        mockMvc.perform(post("/api/answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 답변을 조회하면 404 에러가 발생한다")
    void getAnswer_Fails_When_AnswerId_Not_Found() throws Exception {
        // when & then
        mockMvc.perform(get("/api/answers/{answerId}", 99999L))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
} 