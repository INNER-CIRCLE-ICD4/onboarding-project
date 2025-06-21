package com.innercircle.onboarding.changzune_onboarding.survey.repository;

import com.innercircle.onboarding.changzune_onboarding.survey.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    // 설문 ID로 응답 리스트 조회
    List<Answer> findBySurveyId(Long surveyId);

    List<Answer> findBySurveyIdAndQuestionNameAndAnswerValue(Long surveyId, String questionName, String answerValue);
}