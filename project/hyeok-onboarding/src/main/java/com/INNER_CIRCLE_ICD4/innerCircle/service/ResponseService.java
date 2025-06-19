package com.INNER_CIRCLE_ICD4.innerCircle.service;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.*;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.*;
import com.INNER_CIRCLE_ICD4.innerCircle.exception.BusinessException;
import com.INNER_CIRCLE_ICD4.innerCircle.exception.ResourceNotFoundException;
import com.INNER_CIRCLE_ICD4.innerCircle.repository.ResponseRepository;
import com.INNER_CIRCLE_ICD4.innerCircle.repository.SurveyRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;

    /**
     * ✨ 설문 응답 저장
     */
    @Transactional
    public UUID saveResponse(ResponseRequest req) {
        Survey survey = surveyRepository.findById(req.surveyId())
                .orElseThrow(() -> new ResourceNotFoundException("설문이 존재하지 않습니다."));

        if (survey.getQuestions().size() != req.answers().size()) {
            throw new BusinessException("응답 개수가 설문 질문 개수와 일치하지 않습니다.");
        }

        String snapshotJson;
        try {
            SurveySnapshotDto snapshotDto = SurveySnapshotDto.from(survey);
            snapshotJson = objectMapper.writeValueAsString(snapshotDto);
        } catch (JsonProcessingException e) {
            throw new BusinessException("설문 스냅샷 직렬화 중 오류가 발생했습니다.");
        }

        Response response = new Response(survey, snapshotJson);

        for (AnswerRequest ar : req.answers()) {
            Question question = survey.getQuestions().stream()
                    .filter(q -> q.getId().equals(ar.questionId()))
                    .findAny()
                    .orElseThrow(() -> new BusinessException("존재하지 않는 질문에 대한 응답입니다."));

            // 필수 응답 유효성 검사
            if (question.isRequired()) {
                boolean isEmpty = switch (question.getType()) {
                    case SHORT, LONG -> ar.text() == null || ar.text().isBlank();
                    case SINGLE_CHOICE, MULTIPLE_CHOICE -> ar.selectedOptions() == null || ar.selectedOptions().isEmpty();
                };
                if (isEmpty) {
                    throw new BusinessException("필수 질문에 응답이 없습니다. 질문 ID: " + question.getId());
                }
            }

            // 타입별 응답 유효성 검사
            switch (question.getType()) {
                case SHORT, LONG -> {
                    if (ar.text() == null || ar.text().isBlank()) {
                        throw new BusinessException("텍스트 응답이 비어 있습니다. 질문 ID: " + question.getId());
                    }
                }
                case SINGLE_CHOICE -> {
                    if (ar.selectedOptions() == null || ar.selectedOptions().size() != 1) {
                        throw new BusinessException("단일 선택형 질문에는 정확히 하나의 선택지가 필요합니다. 질문 ID: " + question.getId());
                    }
                }
                case MULTIPLE_CHOICE -> {
                    if (ar.selectedOptions() == null || ar.selectedOptions().isEmpty()) {
                        throw new BusinessException("다중 선택형 질문에는 하나 이상의 선택지가 필요합니다. 질문 ID: " + question.getId());
                    }
                }
                default -> throw new BusinessException("알 수 없는 질문 타입입니다. 질문 ID: " + question.getId());
            }
            // ✨ 필수 질문에 대한 응답 누락 체크
            if (question.isRequired()) {
                boolean isEmpty = switch (question.getType()) {
                    case SHORT, LONG -> ar.text() == null || ar.text().isBlank();
                    case SINGLE_CHOICE, MULTIPLE_CHOICE -> ar.selectedOptions() == null || ar.selectedOptions().isEmpty();
                };
                if (isEmpty) {
                    throw new BusinessException("필수 질문에 응답이 없습니다. 질문 ID: " + question.getId());
                }
            }

            // 6. Answer 객체 생성 및 응답에 추가
            Answer answer = new Answer(response, question, ar.text(), ar.selectedOptions());
            response.getAnswers().add(answer);
        }

        Response saved = responseRepository.save(response);
        return saved.getId();
    }
    /**
     * ✨ 응답 ID로 단일 응답 조회
     */
    @Transactional(readOnly = true)
    public ResponseDto findById(UUID responseId) {
        Response response = responseRepository.findById(responseId)
                .orElseThrow(() -> new ResourceNotFoundException("응답이 존재하지 않습니다."));

        return new ResponseDto(
                response.getId(),
                response.getSurvey().getId(),
                response.getAnswers().stream()
                        .map(a -> new AnswerDto(
                                a.getQuestion().getId(),
                                a.getText(),
                                a.getSelectedOptions() == null ? null :
                                        a.getSelectedOptions().stream()
                                                .map(UUID::toString)
                                                .collect(toList())
                        ))
                        .collect(toList())
        );
    }
    /**
     * ✨ 설문별 응답 전체 조회
     */
    @Transactional(readOnly = true)
    public List<ResponseDetail> findBySurveyId(UUID surveyId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("설문이 존재하지 않습니다."));

        return responseRepository.findBySurveyId(surveyId).stream()
                .map(ResponseDetail::from)
                .collect(toList());
    }

    /**
     * ✨ 전체 응답 요약 조회
     */
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

    /**
     * ✨ findBySurveyId의 alias
     */
    public List<ResponseDetail> getResponses(UUID surveyId) {
        return findBySurveyId(surveyId);
    }
}
