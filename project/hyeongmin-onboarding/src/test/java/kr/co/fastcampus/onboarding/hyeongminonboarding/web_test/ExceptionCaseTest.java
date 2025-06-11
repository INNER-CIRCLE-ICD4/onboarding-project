package kr.co.fastcampus.onboarding.hyeongminonboarding.web_test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.exception.SurveyException;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.exception.enums.ErrorCode;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
@SpringBootTest
@AutoConfigureMockMvc
@Tag("Exception Case Test")
public class ExceptionCaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("500 잘못된 형식의 JSON")
    public void whenInvalidJson_thenBadRequest() throws Exception {
        mockMvc.perform(post("/api/mock_test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ invalid json }"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(ErrorCode.INTERNAL_ERROR.getCode()));
    }

    @Test
    @DisplayName("400 INVALID_PAYLOAD")
    public void whenQuestionCountTooMany_thenBusinessError() throws Exception {
        // 11개 질문을 가진 페이로드 (최대 10개 초과)
        StringBuilder sb = new StringBuilder();
        sb.append("{ \"name\":\"Test\", \"questions\":[");
        for (int i = 0; i < 11; i++) {
            sb.append("{\"title\":\"Q").append(i).append("\",\"type\":\"SINGLE_CHOICE\"}");
            if (i < 10) sb.append(",");
        }
        sb.append("]}");

        mockMvc.perform(post("/api/mock_test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sb.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_PAYLOAD.getCode()));

    }

    // ... Controller 만들고 BaseException 테스트 진행


}
