package com.INNER_CIRCLE_ICD4.innerCircle;


import com.INNER_CIRCLE_ICD4.innerCircle.controller.ResponseController;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.AnswerRequest;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.ResponseRequest;
import com.INNER_CIRCLE_ICD4.innerCircle.service.ResponseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ResponseController.class)
public class ResponseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResponseService responseService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 응답_저장_API_정상작동() throws Exception {
        // given
        UUID surveyId = UUID.randomUUID();
        UUID questionId = UUID.randomUUID();
        UUID choiceId1 = UUID.randomUUID();
        UUID responseId = UUID.randomUUID();

        AnswerRequest answerRequest = new AnswerRequest(
                questionId,
                "단답형 응답",
                List.of(choiceId1)
        );

        ResponseRequest request = new ResponseRequest(
                surveyId,
                List.of(answerRequest)
        );

        // mock
        Mockito.when(responseService.saveResponse(Mockito.any()))
                .thenReturn(responseId);

        // when + then
        mockMvc.perform(post("/responses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("\"" + responseId.toString() + "\""));
    }
}
