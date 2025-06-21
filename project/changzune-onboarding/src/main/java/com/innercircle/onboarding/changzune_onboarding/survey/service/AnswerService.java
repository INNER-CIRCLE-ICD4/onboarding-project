package com.innercircle.onboarding.changzune_onboarding.survey.service;

import com.innercircle.onboarding.changzune_onboarding.survey.domain.Answer;
import com.innercircle.onboarding.changzune_onboarding.survey.domain.Survey;
import com.innercircle.onboarding.changzune_onboarding.survey.dto.AnswerRequest;
import com.innercircle.onboarding.changzune_onboarding.survey.dto.AnswerResponse;
import com.innercircle.onboarding.changzune_onboarding.survey.repository.AnswerRepository;
import com.innercircle.onboarding.changzune_onboarding.survey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service // 이 클래스가 Spring의 서비스 컴포넌트임을 명시함 (빈으로 등록)
@RequiredArgsConstructor // 생성자 자동 생성 (final 필드에 대해 자동 주입되는 기능
public class AnswerService {

    //리포지토리는 데이터베이스에서 접근해서 도메인에 있는 엔터리르 저장하거나 조회하는 역할을 실행
    //SQL안쓰고 Repository쓰는 이유는 JPA가 알아서 해주기 때문에 사용


    private final AnswerRepository answerRepository; // 응답 저장/조회용 JPA 리포지토리
    private final SurveyRepository surveyRepository; // 설문조사 조회용 리포지토리

    @Transactional // 이 메서드는 트랜잭션 안에서 실행되며, 실패 시 롤백됨
    public void submitAnswer(AnswerRequest dto) {
        // 요청에서 받은 설문 ID로 설문조사를 조회함. 없으면 예외 발생
        Survey survey = surveyRepository.findById(dto.getSurveyId())
                .orElseThrow(() -> new IllegalArgumentException("해당 설문조사가 존재하지 않습니다."));

        // 응답 객체를 생성하고 요청 값으로 필드들을 설정함
        Answer answer = new Answer();
        answer.setSurvey(survey); // 어떤 설문에 대한 응답인지 설정
        answer.setQuestionId(dto.getQuestionId()); // 질문 ID 설정
        answer.setQuestionName(dto.getQuestionName()); // 질문 이름 설정
        answer.setAnswerValue(dto.getAnswerValue()); // 사용자가 입력한 응답값 설정

        // 데이터베이스에 응답 저장
        answerRepository.save(answer);
    }

    // 응답 조회
    public List<AnswerResponse> getAnswers(Long surveyId, String questionName, String answerValue) {
        List<Answer> answers;

        if (questionName != null && answerValue != null) {
            answers = answerRepository.findBySurveyIdAndQuestionNameAndAnswerValue(surveyId, questionName, answerValue);
        } else {
            answers = answerRepository.findBySurveyId(surveyId);
        }

        return answers.stream()
                .map(a -> new AnswerResponse(a.getQuestionName(), a.getAnswerValue()))
                .collect(Collectors.toList());
    }

}