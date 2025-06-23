package com.innercircle.onboarding.changzune_onboarding.survey.service;

import com.innercircle.onboarding.changzune_onboarding.survey.domain.Question;
import com.innercircle.onboarding.changzune_onboarding.survey.domain.Survey;
import com.innercircle.onboarding.changzune_onboarding.survey.dto.SurveyListResponse;
import com.innercircle.onboarding.changzune_onboarding.survey.dto.SurveyRequest;
import com.innercircle.onboarding.changzune_onboarding.survey.dto.SurveyResponse;
import com.innercircle.onboarding.changzune_onboarding.survey.dto.SurveyUpdateRequest;
import com.innercircle.onboarding.changzune_onboarding.survey.repository.SurveyRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;


    /**
     * 기존 질문과 새 질문이 동일한지 비교
     */
    private boolean isSameQuestion(Question existing, SurveyRequest.QuestionRequest qReq) {
        // 이름이 같은지 확인
        return existing.getName().equals(qReq.getName()) &&
                // 설명이 같은지 확인
                existing.getDescription().equals(qReq.getDescription()) &&
                // 질문 타입이 같은지 확인 (예: 단답형, 장문형 등)
                existing.getType().name().equals(qReq.getType()) &&
                // 필수로 입력해야 하는지도 같은지 확인
                existing.isRequired() == qReq.isRequired() &&
                // 선택지 목록(옵션)이 같은지 확인 (객체 비교는 Objects.equals로)
                Objects.equals(existing.getOptions(), qReq.getOptions());
    }

    /**
     * 새로운 질문 엔티티 생성
     */
    private Question toNewQuestionEntity(SurveyRequest.QuestionRequest q, Survey survey) {
        Question question = new Question();
        question.setName(q.getName());
        question.setDescription(q.getDescription());
        question.setType(Question.QuestionType.valueOf(q.getType()));
        question.setRequired(q.isRequired());
        question.setOptions(q.getOptions());
        question.setSurvey(survey);
        return question;
    }


    @Transactional
    public Survey createSurvey(SurveyRequest request) {

        //새로운 객체 생성 준비
        Survey survey = new Survey();

        //설문지 제목 입력으로 받기
        survey.setTitle(request.getTitle());
        //설문지 설명 입력으로 받기
        survey.setDescription(request.getDescription());

        List<Question> questionList = new ArrayList<>();
        //설문질문이 들어간 리스트 만들어 여러건이니까 리스트

        //향상된 for를 썻어
        for (SurveyRequest.QuestionRequest q : request.getQuestions()) {
            //선물 질문 받기
            Question question = new Question();
            //질문이름
            question.setName(q.getName());
            //질문 설명
            question.setDescription(q.getDescription());
            //질문 타입
            question.setType(Question.QuestionType.valueOf(q.getType()));
            // 질문 필수 조건
            question.setRequired(q.isRequired());

            // 선택형일 때만 옵션 설정

            if (question.getType() == Question.QuestionType.SINGLE_CHOICE ||
                    question.getType() == Question.QuestionType.MULTIPLE_CHOICE) {
                question.setOptions(q.getOptions());
            }

            // Survey survey = new Survey(); 위에서 만든 객체저장
            question.setSurvey(survey); // 연관관계 설정
            //리스트 다대일
            questionList.add(question);
        }

        survey.setQuestions(questionList);
        return surveyRepository.save(survey); // cascade로 Question도 함께 저장
    }


    //설문 수정 API 만들기 서비스 부분

    @Transactional  // 트랜잭션 처리: 실패 시 전체 롤백
    public void updateSurvey(Long surveyId, SurveyUpdateRequest request) {
        // 1. 기존 설문조사 조회 (없으면 예외 발생)
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new IllegalArgumentException("설문조사가 존재하지 않습니다."));

        // 2. 제목과 설명 업데이트
        survey.setTitle(request.getTitle());
        survey.setDescription(request.getDescription());

        // 3. 기존 질문들을 Map으로 저장 (id → Question 객체)
        Map<Long, Question> existingQuestions = survey.getQuestions().stream()
                .collect(Collectors.toMap(Question::getId, q -> q));

        // 4. 새로운 질문 리스트 생성
        List<Question> updatedQuestions = new ArrayList<>();

        // 5. 요청에서 들어온 질문 하나씩 처리
        for (SurveyRequest.QuestionRequest qReq : request.getQuestions()) {
            // 기존 질문 ID가 있고 기존 데이터에 존재하는 경우
            if (qReq.getId() != null && existingQuestions.containsKey(qReq.getId())) {
                Question old = existingQuestions.get(qReq.getId());

                // 기존 질문과 변경 여부 판단
                if (isSameQuestion(old, qReq)) {
                    // 변경 사항이 없으면 기존 질문 재사용
                    updatedQuestions.add(old);
                } else {
                    // 변경됐으면 새 질문으로 INSERT
                    Question newQuestion = toNewQuestionEntity(qReq, survey);
                    updatedQuestions.add(newQuestion);
                }
            } else {
                // 새로 추가된 질문일 경우
                Question newQuestion = toNewQuestionEntity(qReq, survey);
                updatedQuestions.add(newQuestion);
            }
        }

        // 6. 설문에 새로운 질문 목록 설정
        survey.setQuestions(updatedQuestions);

        // 7. 저장 (JPA가 변경된 설문 + 질문 인식 후 자동 UPDATE)
        surveyRepository.save(survey);
    }


    //설문조사 단건 조회
    public SurveyResponse getSurveyById(Long id) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 설문이 존재하지 않습니다."));

        List<SurveyResponse.QuestionDto> questionDtos = survey.getQuestions().stream()
                .map(q -> new SurveyResponse.QuestionDto(
                        q.getId(),
                        q.getName(),
                        q.getDescription(),
                        q.getType().name(),
                        q.isRequired(),
                        q.getOptions()
                )).toList();

        return new SurveyResponse(
                survey.getId(),
                survey.getTitle(),
                survey.getDescription(),
                questionDtos
        );
    }

    //설문 조사 다건 조회
    public List<SurveyListResponse> getAllSurveys() {
        List<Survey> surveys = surveyRepository.findAll();

        return surveys.stream()
                .map(survey -> new SurveyListResponse(
                        survey.getId(),
                        survey.getTitle(),
                        survey.getDescription(),
                        survey.getQuestions().size()  // 질문 개수
                ))
                .toList();
    }

    //설문조사 삭제
    public void deleteSurvey(Long id) {
        if (!surveyRepository.existsById(id)) {
            throw new IllegalArgumentException("해당 설문이 존재하지 않습니다.");
        }

        surveyRepository.deleteById(id);
    }

}