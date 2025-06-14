package com.INNER_CIRCLE_ICD4.innerCircle.service;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.*;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.*;
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

    // 🔍 설문 전체 조회
    public List<SurveyResponse> findAll() {
        return surveyRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // 🔍 설문 ID로 조회
    public SurveyResponse findById(UUID id) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("설문이 존재하지 않습니다."));
        return toDto(survey);
    }

    // ✅ 설문 생성
    public Survey createSurvey(SurveyRequest request) {
        Survey survey = new Survey(request.title(), request.description());

        for (int i = 0; i < request.questions().size(); i++) {
            QuestionRequest q = request.questions().get(i);
            Question question = new Question(
                    q.title(), q.description(),
                    QuestionType.valueOf(q.type()), q.required()
            );
            question.setSurvey(survey);

            if (q.choices() != null) {
                for (int j = 0; j < q.choices().size(); j++) {
                    Choice choice = new Choice(q.choices().get(j), j);
                    choice.setQuestion(question);
                    question.getChoices().add(choice);
                }
            }

            survey.getQuestions().add(question);
        }

        return surveyRepository.save(survey);
    }

    // 🔄 Entity → DTO 변환
    private SurveyResponse toDto(Survey survey) {
        return new SurveyResponse(
                survey.getId(),
                survey.getTitle(),
                survey.getDescription(),
                survey.getVersion(),
                survey.getQuestions().stream()
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
                                        .toList()
                        ))
                        .toList()
        );
    }
}
