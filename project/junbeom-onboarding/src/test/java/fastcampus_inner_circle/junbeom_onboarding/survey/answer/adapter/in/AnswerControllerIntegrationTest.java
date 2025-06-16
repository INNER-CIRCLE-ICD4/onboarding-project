package fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AnswerControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("응답 제출 API - 실제 DB에 insert 통합 테스트")
    @Rollback(false)
    void submitAnswer_IntegrationTest() throws Exception {
        String json = """
        {
          \"formId\": 1,
          \"answers\": [
            {\"contentId\": 10, \"answerValue\": \"홍길동\"},
            {\"contentId\": 11, \"optionId\": 100, \"answerValue\": null}
          ]
        }
        """;
        mockMvc.perform(post("/api/answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.formId").value(1));
        // 실제로 DB에 insert가 일어났는지는 별도 리포지토리로 조회해도 됨
    }
} 