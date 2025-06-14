package com.INNER_CIRCLE_ICD4.innerCircle.service;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.*;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.*;
import com.INNER_CIRCLE_ICD4.innerCircle.exception.BusinessException;
import com.INNER_CIRCLE_ICD4.innerCircle.exception.ResourceNotFoundException;
import com.INNER_CIRCLE_ICD4.innerCircle.repository.ResponseRepository;
import com.INNER_CIRCLE_ICD4.innerCircle.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ResponseService {

    private final SurveyRepository surveyRepository;
    private final ResponseRepository responseRepository;

    @Transactional
    public ResponseDto saveResponse(ResponseRequest req) {
        Survey survey = surveyRepository.findById(req.surveyId())
                .orElseThrow(() -> new ResourceNotFoundException("설문이 존재하지 않습니다."));

        if (survey.getQuestions().size() != req.answers().size()) {
            throw new BusinessException("응답 개수가 설문 질문 개수와 일치하지 않습니다.");
        }

        Response resp = new Response(survey);
        for (AnswerRequest ar : req.answers()) {
            Question q = survey.getQuestions().stream()
                    .filter(x -> x.getId().equals(ar.questionId()))
                    .findAny()
                    .orElseThrow(() -> new BusinessException("존재하지 않는 질문에 대한 응답입니다."));
            Answer a = new Answer(resp, q, ar.text(), ar.selected());
            resp.getAnswers().add(a);
        }

        Response saved = responseRepository.save(resp);
        List<AnswerDto> dtoAnswers = saved.getAnswers().stream()
                .map(a -> new AnswerDto(
                        a.getQuestion().getId(),
                        a.getText(),
                        a.getSelectedOptions()
                ))
                .collect(toList());

        return new ResponseDto(saved.getId(), survey.getId(), dtoAnswers);
    }

    @Transactional(readOnly = true)
    public List<ResponseDto> findAll() {
        return responseRepository.findAll().stream()
                .map(r -> new ResponseDto(
                        r.getId(),
                        r.getSurvey().getId(),
                        r.getAnswers().stream()
                                .map(a -> new AnswerDto(
                                        a.getQuestion().getId(),
                                        a.getText(),
                                        a.getSelectedOptions()))
                                .collect(toList())
                ))
                .collect(toList());
    }
}
