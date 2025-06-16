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
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ResponseService {

    private final SurveyRepository surveyRepository;
    private final ResponseRepository responseRepository;

    @Transactional
    public UUID saveResponse(ResponseRequest req) {
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

            // 유효성 검증
            switch (q.getType()) {
                case SHORT, LONG -> {
                    if (ar.text() == null || ar.text().isBlank()) {
                        throw new BusinessException("텍스트 응답이 비어 있습니다. 질문 ID: " + q.getId());
                    }
                }
                case SINGLE_CHOICE -> {
                    if (ar.selectedOptions() == null || ar.selectedOptions().size() != 1) {
                        throw new BusinessException("단일 선택형 질문에는 정확히 하나의 선택지가 필요합니다. 질문 ID: " + q.getId());
                    }
                }
                case MULTIPLE_CHOICE -> {
                    if (ar.selectedOptions() == null || ar.selectedOptions().isEmpty()) {
                        throw new BusinessException("다중 선택형 질문에는 하나 이상의 선택지가 필요합니다. 질문 ID: " + q.getId());
                    }
                }
                default -> throw new BusinessException("알 수 없는 질문 타입입니다. 질문 ID: " + q.getId());
            }

            if (q.isRequired()) {
                boolean isEmpty = switch (q.getType()) {
                    case SHORT, LONG -> ar.text() == null || ar.text().isBlank();
                    case SINGLE_CHOICE, MULTIPLE_CHOICE -> ar.selectedOptions() == null || ar.selectedOptions().isEmpty();
                };
                if (isEmpty) {
                    throw new BusinessException("필수 질문에 응답이 없습니다. 질문 ID: " + q.getId());
                }
            }

            Answer a = new Answer(resp, q, ar.text(), ar.selectedOptions());
            resp.getAnswers().add(a);
        }

        return responseRepository.save(resp).getId();
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
                                        a.getSelectedOptions() == null ? null :
                                                a.getSelectedOptions().stream()
                                                        .map(UUID::toString)
                                                        .collect(toList())
                                ))
                                .collect(toList())
                ))
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<ResponseDetail> findBySurveyId(UUID surveyId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("설문이 존재하지 않습니다."));

        return responseRepository.findBySurveyId(surveyId).stream()
                .map(ResponseDetail::from)
                .collect(toList());
    }

    // 필요 없으면 삭제 가능 (위 메서드와 동일 기능)
    public List<ResponseDetail> getResponses(UUID surveyId) {
        return findBySurveyId(surveyId);
    }
}
