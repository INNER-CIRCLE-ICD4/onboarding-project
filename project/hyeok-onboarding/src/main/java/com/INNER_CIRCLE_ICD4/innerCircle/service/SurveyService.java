package com.INNER_CIRCLE_ICD4.innerCircle.service;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.Choice;
import com.INNER_CIRCLE_ICD4.innerCircle.domain.Question;
import com.INNER_CIRCLE_ICD4.innerCircle.domain.Survey;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.QuestionUpdateRequest;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.QuestionRequest;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.SurveyRequest;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.SurveyResponse;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.SurveyUpdateRequest;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.ChoiceResponse;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.QuestionResponse;
import com.INNER_CIRCLE_ICD4.innerCircle.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;

    /** 1) 설문 생성 */
    public SurveyResponse createSurvey(SurveyRequest request) {
        Survey survey = new Survey(request.title(), request.description());
        for (QuestionRequest q : request.questions()) {
            Question question = new Question(q.title(), q.description(), q.type(), q.required());
            survey.addQuestion(question);
            if (q.choices() != null) {
                for (int i = 0; i < q.choices().size(); i++) {
                    question.addChoice(new Choice(q.choices().get(i), i));
                }
            }
        }
        Survey saved = surveyRepository.save(survey);
        return toDto(saved);
    }

    /** 2) 전체 설문 조회 */
    public List<SurveyResponse> findAll() {
        return surveyRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /** 3) 단건 설문 조회 */
    public SurveyResponse findById(UUID id) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("설문을 찾을 수 없습니다."));
        return toDto(survey);
    }

    /** 4) 설문 수정 */
    public SurveyResponse updateSurvey(UUID id, SurveyUpdateRequest request) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("설문을 찾을 수 없습니다."));

        // ── 질문 변경이 빈 리스트로 왔으면 '질문 유지' ───────────────────────────────
        List<QuestionUpdateRequest> reqQs = request.getQuestions();
        if (reqQs != null && !reqQs.isEmpty()) {
            survey.getQuestions().clear();
            for (QuestionUpdateRequest q : reqQs) {
                Question question = new Question(
                        q.getTitle(),
                        q.getDescription(),
                        q.getType(),
                        q.isRequired()
                );
                if (q.getChoices() != null) {
                    for (int i = 0; i < q.getChoices().size(); i++) {
                        question.addChoice(new Choice(q.getChoices().get(i), i));
                    }
                }
                survey.addQuestion(question);
            }
        }

        // 제목·설명 및 버전 업데이트
        survey.update(request.getTitle(), request.getDescription());

        Survey saved = surveyRepository.save(survey);
        return toDto(saved);
    }

    /* Survey → SurveyResponse 변환 헬퍼 */
    private SurveyResponse toDto(Survey survey) {
        List<QuestionResponse> qs = survey.getQuestions().stream()
                .map(q -> new QuestionResponse(
                        q.getId(),
                        q.getTitle(),
                        q.getDescription(),
                        q.getType(),
                        q.isRequired(),
                        q.getChoices().stream()
                                .map(c -> new ChoiceResponse(
                                        c.getId(),
                                        c.getText(),
                                        c.getChoiceIndex()
                                ))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        return new SurveyResponse(
                survey.getId(),
                survey.getTitle(),
                survey.getDescription(),
                survey.getVersion(),
                qs
        );
    }
}
