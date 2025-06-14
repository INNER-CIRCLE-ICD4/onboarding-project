package com.INNER_CIRCLE_ICD4.innerCircle.service;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.*;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.*;
import com.INNER_CIRCLE_ICD4.innerCircle.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResponseService {

    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final ChoiceRepository choiceRepository;
    private final ResponseRepository responseRepository;

    public UUID saveResponse(ResponseRequest request) {
        Survey survey = surveyRepository.findById(request.surveyId())
                .orElseThrow(() -> new IllegalArgumentException("설문이 존재하지 않습니다."));

        Response response = new Response(survey, "snapshot data"); // 실제 스냅샷이 필요하다면 JSON 등으로 가공

        for (AnswerRequest ar : request.answers()) {
            Question question = questionRepository.findById(ar.questionId())
                    .orElseThrow(() -> new IllegalArgumentException("질문이 존재하지 않습니다."));

            Answer answer = new Answer(question, ar.textValue());
            answer.setResponse(response);

            if (ar.selectedChoiceIds() != null) {
                for (UUID choiceId : ar.selectedChoiceIds()) {
                    Choice choice = choiceRepository.findById(choiceId)
                            .orElseThrow(() -> new IllegalArgumentException("선택지가 존재하지 않습니다."));

                    AnswerChoice ac = new AnswerChoice(answer, choice);
                    answer.addChoice(ac);
                }
            }

            response.addAnswer(answer);
        }

        responseRepository.save(response);
        return response.getId();
    }
}
