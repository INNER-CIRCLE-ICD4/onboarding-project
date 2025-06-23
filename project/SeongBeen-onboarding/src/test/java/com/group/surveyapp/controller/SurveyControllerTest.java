package com.group.surveyapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.surveyapp.domain.entity.QuestionType;
import com.group.surveyapp.dto.request.SurveyAnswerRequestDto;
import com.group.surveyapp.dto.request.SurveyCreateRequestDto;
import com.group.surveyapp.dto.request.SurveyCreateRequestDto.QuestionDto;
import com.group.surveyapp.dto.request.SurveyUpdateRequestDto;
import com.group.surveyapp.dto.response.SurveyAnswerResponseDto;
import com.group.surveyapp.dto.response.SurveyResponseDto;
import com.group.surveyapp.service.SurveyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("removal")
@WebMvcTest(SurveyController.class)
class SurveyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SurveyService surveyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("설문 생성 API 테스트")
    void createSurvey() throws Exception {
        SurveyCreateRequestDto request = new SurveyCreateRequestDto();
        request.setTitle("테스트 설문");
        request.setDescription("테스트 설명");
        request.setQuestions(List.of(
                new QuestionDto("이름", "이름을 입력해주세요.", QuestionType.SHORT, true, null),
                new QuestionDto("개발 언어", "선호 언어는?", QuestionType.SINGLE, true, List.of("Java", "Python"))
        ));

        mockMvc.perform(post("/api/surveys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("설문 응답 제출 API 테스트")
    void submitAnswer() throws Exception {
        SurveyAnswerRequestDto.AnswerDto answer1 = new SurveyAnswerRequestDto.AnswerDto();
        answer1.setQuestionId(1L);
        answer1.setAnswer("홍길동");

        SurveyAnswerRequestDto.AnswerDto answer2 = new SurveyAnswerRequestDto.AnswerDto();
        answer2.setQuestionId(2L);
        answer2.setAnswer("Java");

        SurveyAnswerRequestDto request = new SurveyAnswerRequestDto();
        request.setAnswers(List.of(answer1, answer2));

        doNothing().when(surveyService).submitAnswer(1L, request);

        mockMvc.perform(post("/api/surveys/1/answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("설문 수정 API 테스트")
    void updateSurvey() throws Exception {
        SurveyUpdateRequestDto request = new SurveyUpdateRequestDto();
        request.setTitle("수정된 설문 제목");
        request.setDescription("수정된 설명");
        request.setQuestions(List.of(
                new SurveyUpdateRequestDto.QuestionDto(1L, "질문1", "설명1", QuestionType.SHORT, true, null),
                new SurveyUpdateRequestDto.QuestionDto(2L, "질문2", "설명2", QuestionType.SINGLE, false, List.of("A", "B"))
        ));

        // SurveyResponseDto를 Builder로 생성
        SurveyResponseDto responseDto = SurveyResponseDto.builder()
                .surveyId(1L)
                .title("수정된 설문 제목")
                .description("수정된 설명")
                .createdAt("2025-06-23T10:00:00") // 또는 null, 또는 now().toString()
                .questions(List.of()) // 실제 질문 목록이 필요하다면 추가
                .build();

        when(surveyService.updateSurvey(eq(1L), any(SurveyUpdateRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(put("/api/surveys/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("수정된 설문 제목"));
    }


    @Test
    @DisplayName("설문 응답 조회 API 테스트")
    void getResponses() throws Exception {
        SurveyAnswerResponseDto.QuestionAnswerDto q1 = new SurveyAnswerResponseDto.QuestionAnswerDto(
                1L, "이름", "이름을 입력해주세요", QuestionType.SHORT, true, null, "홍길동"
        );
        SurveyAnswerResponseDto.QuestionAnswerDto q2 = new SurveyAnswerResponseDto.QuestionAnswerDto(
                2L, "개발 언어", "선호하는 언어는?", QuestionType.SINGLE, true, List.of("Java", "Python"), "Java"
        );

        SurveyAnswerResponseDto response = new SurveyAnswerResponseDto(
                1L, "테스트 설문", "테스트 설명", List.of(q1, q2)
        );

        when(surveyService.getAnswers(1L)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/surveys/1/responses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].questions.size()").value(2))
                .andExpect(jsonPath("$[0].questions[0].name").value("이름"))
                .andExpect(jsonPath("$[0].questions[0].answer").value("홍길동"))
                .andExpect(jsonPath("$[0].questions[1].name").value("개발 언어"))
                .andExpect(jsonPath("$[0].questions[1].answer").value("Java"));
    }
}
