package com.INNER_CIRCLE_ICD4.innerCircle.service;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.Choice;
import com.INNER_CIRCLE_ICD4.innerCircle.domain.Question;
import com.INNER_CIRCLE_ICD4.innerCircle.domain.Survey;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.*;
import com.INNER_CIRCLE_ICD4.innerCircle.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;

    @Transactional
    public SurveyResponse createSurvey(SurveyRequest req) {
        Survey survey = new Survey(req.title(), req.description());

        for (QuestionRequest qr : req.questions()) {
            Question q = new Question(
                    qr.title(),
                    qr.description(),
                    qr.type(),
                    qr.required()
            );
            survey.addQuestion(q);

            if (qr.choices() != null) {
                for (int i = 0; i < qr.choices().size(); i++) {
                    q.addChoice(new Choice(qr.choices().get(i), i));
                }
            }
        }

        Survey saved = surveyRepository.save(survey);
        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public SurveyResponse findById(UUID id) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 설문을 찾을 수 없습니다: " + id));
        return toDto(survey);
    }

    @Transactional
    public SurveyResponse update(UUID id, SurveyUpdateRequest req) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 설문을 찾을 수 없습니다: " + id));

        // 제목과 설명 업데이트
        survey.update(req.title(), req.description());

        // 질문 교체
        List<Question> newQuestions = req.questions().stream().map(qur -> {
            Question q = new Question(
                    qur.title(),
                    qur.description(),
                    qur.type(),
                    qur.required()
            );
            if (qur.choices() != null) {
                for (int i = 0; i < qur.choices().size(); i++) {
                    q.addChoice(new Choice(qur.choices().get(i), i));
                }
            }
            return q;
        }).collect(Collectors.toList());

        survey.replaceQuestions(newQuestions);
        return toDto(survey);
    }

    private SurveyResponse toDto(Survey s) {
        List<QuestionResponse> qs = s.getQuestions().stream()
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
                s.getId(),
                s.getTitle(),
                s.getDescription(),
                s.getVersion(),
                qs
        );
    }
}
