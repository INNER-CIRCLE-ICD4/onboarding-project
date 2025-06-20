package fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.dto.AnswerResponse;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AnswerControllerQueryIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("formId=1로 전체 응답을 조회하고 실제 데이터를 모두 출력한다")
    @Rollback(false)
    void printAllAnswersByFormId1() throws Exception {
        // when & then: formId=1로 전체 조회
        MvcResult mvcResult = mockMvc.perform(get("/api/answers")
                        .param("formId", "1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        System.out.println("응답 JSON: " + responseBody);

        List<AnswerResponse> responses = objectMapper.readValue(
            responseBody,
            objectMapper.getTypeFactory().constructCollectionType(List.class, AnswerResponse.class)
        );
        System.out.println("파싱된 AnswerResponse 목록:");
        responses.forEach(System.out::println);
    }
} 