package com.INNER_CIRCLE_ICD4.innerCircle;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.*;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.AnswerRequest;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.ResponseRequest;
import com.INNER_CIRCLE_ICD4.innerCircle.repository.ResponseRepository;
import com.INNER_CIRCLE_ICD4.innerCircle.repository.SurveyRepository;
import com.INNER_CIRCLE_ICD4.innerCircle.service.ResponseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ResponseServiceSnapshotTest {

    @Autowired
    private ResponseService responseService;

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void saveResponse_storesSurveySnapshot() throws Exception {
        // 설문 + 질문 생성
        Survey survey = new Survey("title", "desc");
        Question q1 = new Question("q1", "q1 desc", QuestionType.SHORT, true);

        // 양방향 연관관계 설정 (setSurvey 포함)
        survey.replaceQuestions(List.of(q1)); // 내부적으로 setSurvey 호출됨

        // 저장
        surveyRepository.save(survey); // CascadeType.ALL 로 인해 question도 저장됨

        // 저장 후 question ID 조회
        UUID questionId = survey.getQuestions().get(0).getId();

        // 응답 생성
        AnswerRequest answer = new AnswerRequest(questionId, "응답입니다", null);
        ResponseRequest req = new ResponseRequest(survey.getId(), List.of(answer));

        // 응답 저장
        UUID savedId = responseService.saveResponse(req);

        // 저장된 응답 검증
        Response saved = responseRepository.findById(savedId).orElseThrow();
        String snapshotJson = saved.getSurveySnapshot();

        // Snapshot 내용에 설문 제목, 설명, 질문 제목이 포함되어야 함
        assertThat(snapshotJson).contains("title", "desc", "q1");
    }
}
