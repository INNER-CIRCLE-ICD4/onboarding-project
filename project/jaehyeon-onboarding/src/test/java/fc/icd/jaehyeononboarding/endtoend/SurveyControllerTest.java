package fc.icd.jaehyeononboarding.endtoend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fc.icd.jaehyeononboarding.common.constants.ResultCodes;
import fc.icd.jaehyeononboarding.survey.model.dto.SurveyCreateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class SurveyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void 설문_저장_매핑_테스트() throws Exception {
        String data = "{\"name\": \"설문조사 테스트\",\"description\": \"설문조사 테스트 description\",\"questions\": [{\"label\": \"성명을 입력해주세요.\",\"description\": \"\",\"input_type\": \"text\",\"required\": true},{\"label\": \"성명을 입력해주세요.\",\"description\": \"\",\"input_type\": \"long_text\",\"required\": true}, {\"label\": \"어떤 경로로 알게 되셨나요?\",\"description\": \"\",\"input_type\": \"radio\",\"required\": false,\"options\": [\"웹 검색\", \"SNS\", \"지인을 통해\"]}, {\"label\": \"관심사를 선택해주세요.\",\"description\": \"\",\"input_type\": \"checkbox\",\"required\": false,\"options\": [\"독서\", \"TV시청\", \"게임\", \"영화\"]}]}";

        SurveyCreateDTO dto = objectMapper.readValue(data, SurveyCreateDTO.class);

        mockMvc.perform(post("/api/v1/surveys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(data))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResultCodes.RC_10000));
    }

}
