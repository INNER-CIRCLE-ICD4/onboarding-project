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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
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
    void 스냅샷이_정상적으로_저장된다() throws Exception {
        // given
        Survey survey = new Survey("설문 제목", "설문 설명");
        Question question = new Question("질문 제목", "질문 설명", QuestionType.SHORT, true);

        // ID 직접 설정 ❌ → 제거
        survey.addQuestion(question); // 양방향 관계 설정됨
        surveyRepository.save(survey); // 여기서 question도 함께 저장됨

        // 저장된 question의 ID 가져오기
        UUID questionId = survey.getQuestions().get(0).getId();

        // 응답 생성
        AnswerRequest answer = new AnswerRequest(questionId, "답변입니다", null);
        ResponseRequest request = new ResponseRequest(survey.getId(), List.of(answer));

        // when
        UUID responseId = responseService.saveResponse(request);

        // then
        Response savedResponse = responseRepository.findById(responseId)
                .orElseThrow(() -> new IllegalStateException("응답이 저장되지 않았습니다."));

        String snapshot = savedResponse.getSurveySnapshot();

        assertThat(snapshot).contains("설문 제목");
        assertThat(snapshot).contains("설문 설명");
        assertThat(snapshot).contains("질문 제목");
    }

}
