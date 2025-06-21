package com.innercircle.survey.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innercircle.survey.common.dto.QuestionUpdateDto;
import com.innercircle.survey.common.dto.SurveyUpdateDto;
import com.innercircle.survey.domain.entity.Question;
import com.innercircle.survey.domain.entity.Survey;
import com.innercircle.survey.domain.repository.SurveyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SurveyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SurveyRepository surveyRepository;

    @Test
    void createSurveySuccess() throws Exception {
        String json = """
                {
                  "title": "테스트 설문",
                  "description": "설문 설명입니다.",
                  "questions": [
                    {
                      "title": "당신의 성별은?",
                      "description": "",
                      "type": "SINGLE_CHOICE",
                      "required": true,
                      "options": ["남자", "여자", "기타"]
                    }
                  ]
                }
                """;

        mockMvc.perform(post("/api/surveys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.surveyId").exists());
    }

    @Test
    void updateSurvey_success() throws Exception {
        Pair<UUID, UUID> ids  = createDummySurveyAndReturnId(); //더미 설문조사
        UUID existingSurveyId = ids.getFirst();
        UUID existingQuestionId = ids.getSecond();

        SurveyUpdateDto updateDto = SurveyUpdateDto.builder()
                .title("수정된 설문 제목")
                .description("수정된 설명")
                .questions(List.of(
                        QuestionUpdateDto.builder()
                                .questionId(existingQuestionId)  // 기존 질문 수정
                                .title("질문1 수정됨")
                                .description("설명1")
                                .type("SINGLE_CHOICE")
                                .required(true)
                                .options(List.of("옵션1", "옵션2"))
                                .build(),
                        QuestionUpdateDto.builder()
                                .title("새로운 질문")
                                .description("새 설명")
                                .type("TEXT")
                                .required(false)
                                .build()
                ))
                .build();

        String json = objectMapper.writeValueAsString(updateDto);

        mockMvc.perform(put("/api/surveys/" + existingSurveyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("설문조사 수정 성공"));
    }

    private Pair<UUID, UUID> createDummySurveyAndReturnId() {
        Survey survey = Survey.builder()
                .title("테스트 설문")
                .description("테스트 설명")
                .build();

        Question question = Question.builder()
                .title("당신의 성별은?")
                .description("")
                .type("SINGLE_CHOICE")
                .required(true)
                .survey(survey)
                .build();

        survey.addQuestion(question);

        surveyRepository.save(survey);
        return Pair.of(survey.getId(), question.getId());
    }
}
