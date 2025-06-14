package com.INNER_CIRCLE_ICD4.innerCircle.service;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.*;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.*;
import com.INNER_CIRCLE_ICD4.innerCircle.exception.ResourceNotFoundException;
import com.INNER_CIRCLE_ICD4.innerCircle.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;

    @Transactional
    public SurveyResponse createSurvey(SurveyRequest req) {
        Survey survey = new Survey(req.title(), req.description());
        req.questions().forEach(qr -> {
            Question q = new Question(qr.title(), qr.description(), qr.type(), qr.required());
            survey.addQuestion(q);
            if (qr.choices() != null) {
                for (int i = 0; i < qr.choices().size(); i++) {
                    String c = qr.choices().get(i);
                    q.addChoice(new Choice(c, i));
                }
            }
        });
        Survey saved = surveyRepository.save(survey);
        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<SurveyResponse> findAll() {
        return surveyRepository.findAll().stream().map(this::toDto).collect(toList());
    }

    @Transactional(readOnly = true)
    public SurveyResponse findById(UUID id) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("설문을 찾을 수 없습니다."));
        return toDto(survey);
    }

    private SurveyResponse toDto(Survey s) {
        List<QuestionResponse> qs = s.getQuestions().stream().map(q -> new QuestionResponse(
                q.getId(), q.getTitle(), q.getDescription(), q.getType(), q.isRequired(),
                q.getChoices().stream().map(c -> new ChoiceResponse(c.getId(), c.getText(), c.getChoiceIndex())).collect(toList())
        )).collect(toList());
        return new SurveyResponse(s.getId(), s.getTitle(), s.getDescription(), s.getVersion(), qs);
    }
}