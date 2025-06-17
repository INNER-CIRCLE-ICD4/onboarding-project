package com.innercircle.survey.api.service;

import com.innercircle.survey.api.dto.request.SubmitSurveyResponseRequest;
import com.innercircle.survey.api.dto.response.SurveyResponseDetail;
import com.innercircle.survey.api.dto.response.SurveyResponseListResult;
import com.innercircle.survey.api.dto.response.SurveyResponseSubmissionResult;
import com.innercircle.survey.api.exception.DuplicateResponseException;
import com.innercircle.survey.api.exception.RequiredAnswerMissingException;
import com.innercircle.survey.api.exception.ResponseNotFoundException;
import com.innercircle.survey.api.exception.SurveyNotFoundException;
import com.innercircle.survey.common.exception.BusinessException;
import com.innercircle.survey.common.exception.ErrorCode;
import com.innercircle.survey.domain.response.SurveyAnswer;
import com.innercircle.survey.domain.response.SurveyResponse;
import com.innercircle.survey.domain.survey.Survey;
import com.innercircle.survey.domain.survey.SurveyQuestion;
import com.innercircle.survey.infrastructure.repository.SurveyRepository;
import com.innercircle.survey.infrastructure.repository.SurveyResponseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 설문조사 응답 서비스
 *
 * 설문조사 응답 제출 관련 비즈니스 로직을 처리합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SurveyResponseService {

    private final SurveyRepository surveyRepository;
    private final SurveyResponseRepository surveyResponseRepository;

    /**
     * 설문조사 응답 제출
     *
     * @param surveyId 설문조사 ID
     * @param request 응답 제출 요청
     * @return 제출 결과
     */
    @Transactional
    public SurveyResponseSubmissionResult submitResponse(String surveyId, SubmitSurveyResponseRequest request) {
        log.info("설문조사 응답 제출 시작 - 설문조사 ID: {}, 응답자: {}, 응답 개수: {}", 
                surveyId, request.getRespondentInfo(), request.getAnswers().size());

        try {
            // 1. 요청 검증
            request.validate();

            // 2. 설문조사 조회 및 검증
            Survey survey = surveyRepository.findByIdWithActiveQuestions(surveyId)
                    .orElseThrow(() -> new SurveyNotFoundException(surveyId));
            
            validateSurveyForResponse(survey);

            // 3. 중복 응답 체크 (응답자 정보가 있는 경우)
            validateDuplicateResponse(surveyId, request.getRespondentInfo());

            // 4. 응답 검증 및 변환
            List<SurveyAnswer> answers = validateAndCreateAnswers(survey, request);

            // 5. 설문조사 응답 생성 및 저장
            SurveyResponse surveyResponse = new SurveyResponse(survey, request.getRespondentInfo());
            answers.forEach(surveyResponse::addAnswer);

            SurveyResponse savedResponse = surveyResponseRepository.save(surveyResponse);

            log.info("설문조사 응답 제출 완료 - 응답 ID: {}, 설문조사 ID: {}, 답변 개수: {}", 
                    savedResponse.getId(), surveyId, answers.size());

            return new SurveyResponseSubmissionResult(savedResponse);

        } catch (SurveyNotFoundException e) {
            log.warn("설문조사 응답 제출 실패 - 설문조사를 찾을 수 없음: {}", surveyId);
            throw e;
        } catch (IllegalArgumentException e) {
            log.warn("설문조사 응답 제출 실패 - 잘못된 요청: {}, 설문조사 ID: {}", e.getMessage(), surveyId);
            throw e;
        } catch (Exception e) {
            log.error("설문조사 응답 제출 실패 - 설문조사 ID: {}, 오류: {}", surveyId, e.getMessage(), e);
            throw new RuntimeException("설문조사 응답 제출에 실패했습니다: " + e.getMessage(), e);
        }
    }

    private void validateSurveyForResponse(Survey survey) {
        if (!survey.isActive()) {
            throw new BusinessException(ErrorCode.SURVEY_NOT_ANSWERABLE,
                    String.format("비활성화된 설문조사입니다: %s", survey.getId()),
                    Map.of("surveyId", survey.getId(), "active", false));
        }

        if (!survey.isAnswerable()) {
            throw new BusinessException(ErrorCode.SURVEY_NOT_ANSWERABLE,
                    String.format("응답할 수 없는 설문조사입니다: %s (질문이 없음)", survey.getId()),
                    Map.of("surveyId", survey.getId(), "activeQuestionCount", survey.getActiveQuestions().size()));
        }
    }

    /**
     * 중복 응답 검증
     */
    private void validateDuplicateResponse(String surveyId, String respondentInfo) {
        if (respondentInfo != null && !respondentInfo.trim().isEmpty()) {
            boolean alreadyResponded = surveyResponseRepository
                    .existsBySurveyIdAndRespondentInfo(surveyId, respondentInfo.trim());
            
            if (alreadyResponded) {
                throw new DuplicateResponseException(surveyId, respondentInfo.trim());
            }
        }
    }

    /**
     * 응답 검증 및 SurveyAnswer 객체 생성
     */
    private List<SurveyAnswer> validateAndCreateAnswers(Survey survey, SubmitSurveyResponseRequest request) {
        List<SurveyQuestion> activeQuestions = survey.getActiveQuestions();
        Map<String, SurveyQuestion> questionMap = activeQuestions.stream()
                .collect(Collectors.toMap(SurveyQuestion::getId, question -> question));

        // 1. 요청된 질문 ID들이 유효한지 검증
        Set<String> requestedQuestionIds = request.getQuestionIds().stream().collect(Collectors.toSet());
        Set<String> validQuestionIds = questionMap.keySet();
        
        Set<String> invalidQuestionIds = requestedQuestionIds.stream()
                .filter(id -> !validQuestionIds.contains(id))
                .collect(Collectors.toSet());
        
        if (!invalidQuestionIds.isEmpty()) {
            throw new BusinessException(ErrorCode.QUESTION_NOT_FOUND,
                    String.format("존재하지 않는 질문 ID가 포함되어 있습니다: %s", invalidQuestionIds),
                    Map.of("invalidQuestionIds", invalidQuestionIds, "surveyId", survey.getId()));
        }

        // 2. 필수 질문 응답 체크
        List<String> missingRequiredQuestions = activeQuestions.stream()
                .filter(SurveyQuestion::isRequired)
                .filter(question -> {
                    var answer = request.getAnswerByQuestionId(question.getId());
                    return answer == null || answer.isEmpty();
                })
                .map(SurveyQuestion::getTitle)
                .toList();
        
        if (!missingRequiredQuestions.isEmpty()) {
            throw new RequiredAnswerMissingException(missingRequiredQuestions);
        }

        // 3. 응답 값 검증 및 SurveyAnswer 생성
        return request.getAnswers().stream()
                .map(answerRequest -> {
                    SurveyQuestion question = questionMap.get(answerRequest.getQuestionId());
                    
                    // 응답 값이 비어있는지 확인
                    if (answerRequest.getAnswerValues() == null || answerRequest.getAnswerValues().isEmpty()) {
                        throw new BusinessException(ErrorCode.RESPONSE_EMPTY_ANSWER,
                                String.format("질문 '%s'에 대한 응답이 비어있습니다.", question.getTitle()),
                                Map.of("questionId", question.getId(), "questionTitle", question.getTitle()));
                    }
                    
                    // 질문 타입별 응답 검증
                    validateAnswerForQuestionType(question, answerRequest.getAnswerValues());
                    
                    // 선택형 질문의 경우 선택지 유효성 검증
                    if (question.getQuestionType().isChoiceType()) {
                        validateChoiceAnswers(question, answerRequest.getAnswerValues());
                    }

                    // SurveyAnswer 생성 (스냅샷 포함)
                    return new SurveyAnswer(
                            question.getId(),
                            question.getTitle(),
                            question.getQuestionType(),
                            answerRequest.getAnswerValues(),
                            question.createSnapshot(),
                            question.getOptions()
                    );
                })
                .toList();
    }

    /**
     * 설문조사의 모든 응답 조회
     *
     * @param surveyId 설문조사 ID
     * @param includeAnswerDetails 개별 응답 상세 포함 여부
     * @return 응답 목록
     */
    public SurveyResponseListResult getResponses(String surveyId, boolean includeAnswerDetails) {
        log.info("설문조사 응답 조회 시작 - 설문조사 ID: {}, 상세 포함: {}", surveyId, includeAnswerDetails);

        try {
            // 1. 설문조사 존재 확인
            Survey survey = surveyRepository.findById(surveyId)
                    .orElseThrow(() -> new SurveyNotFoundException(surveyId));

            // 2. 응답 목록 조회
            List<SurveyResponse> responses;
            if (includeAnswerDetails) {
                responses = surveyResponseRepository.findAllBySurveyIdWithAnswers(surveyId);
            } else {
                // 응답 요약만 필요한 경우 (성능 최적화)
                responses = surveyResponseRepository.findAllBySurveyId(surveyId);
            }

            log.info("설문조사 응답 조회 완료 - 설문조사 ID: {}, 응답 개수: {}", surveyId, responses.size());

            return new SurveyResponseListResult(surveyId, survey.getTitle(), responses);

        } catch (SurveyNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("설문조사 응답 조회 실패 - 설문조사 ID: {}, 오류: {}", surveyId, e.getMessage(), e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR,
                    "설문조사 응답 조회 중 예상치 못한 오류가 발생했습니다.", e);
        }
    }

    /**
     * 개별 응답 상세 조회
     *
     * @param responseId 응답 ID
     * @return 응답 상세
     */
    public SurveyResponseDetail getResponseDetail(String responseId) {
        log.info("개별 응답 상세 조회 - 응답 ID: {}", responseId);

        try {
            SurveyResponse response = surveyResponseRepository.findByIdWithAnswers(responseId)
                    .orElseThrow(() -> new ResponseNotFoundException(responseId));

            log.info("개별 응답 상세 조회 완료 - 응답 ID: {}, 답변 개수: {}", 
                    responseId, response.getAnsweredQuestionCount());

            return new SurveyResponseDetail(response);

        } catch (ResponseNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("개별 응답 조회 실패 - 응답 ID: {}, 오류: {}", responseId, e.getMessage(), e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR,
                    "응답 조회 중 예상치 못한 오류가 발생했습니다.", e);
        }
    }

    /**
     * 설문조사 응답 개수 조회
     *
     * @param surveyId 설문조사 ID
     * @return 응답 개수
     */
    public long getResponseCount(String surveyId) {
        log.info("설문조사 응답 개수 조회 - 설문조사 ID: {}", surveyId);

        try {
            // 설문조사 존재 확인
            if (!surveyRepository.existsById(surveyId)) {
                throw new SurveyNotFoundException(surveyId);
            }

            long count = surveyResponseRepository.countBySurveyId(surveyId);
            
            log.info("설문조사 응답 개수 조회 완료 - 설문조사 ID: {}, 응답 개수: {}", surveyId, count);
            
            return count;

        } catch (SurveyNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("설문조사 응답 개수 조회 실패 - 설문조사 ID: {}, 오류: {}", surveyId, e.getMessage(), e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR,
                    "응답 개수 조회 중 예상치 못한 오류가 발생했습니다.", e);
        }
    }

    /**
     * 질문 타입별 응답 검증
     */
    private void validateAnswerForQuestionType(SurveyQuestion question, List<String> answerValues) {
        switch (question.getQuestionType()) {
            case SHORT_TEXT, LONG_TEXT, SINGLE_CHOICE -> {
                if (answerValues.size() > 1) {
                    throw new BusinessException(ErrorCode.RESPONSE_TOO_MANY_CHOICES,
                            String.format("질문 '%s'은(는) 단일 응답만 허용됩니다.", question.getTitle()),
                            Map.of("questionId", question.getId(), "questionTitle", question.getTitle(), 
                                   "answerCount", answerValues.size()));
                }
            }
            case MULTIPLE_CHOICE -> {
                // 다중 선택은 여러 응답 허용
                if (answerValues.isEmpty()) {
                    throw new BusinessException(ErrorCode.RESPONSE_EMPTY_ANSWER,
                            String.format("질문 '%s'에 대한 응답이 없습니다.", question.getTitle()),
                            Map.of("questionId", question.getId(), "questionTitle", question.getTitle()));
                }
            }
        }
    }

    /**
     * 선택형 질문의 선택지 유효성 검증
     */
    private void validateChoiceAnswers(SurveyQuestion question, List<String> answerValues) {
        List<String> validOptions = question.getOptions();
        
        List<String> invalidChoices = answerValues.stream()
                .filter(value -> !validOptions.contains(value))
                .toList();
        
        if (!invalidChoices.isEmpty()) {
            throw new BusinessException(ErrorCode.RESPONSE_INVALID_CHOICE,
                    String.format("질문 '%s'에 유효하지 않은 선택지가 포함되어 있습니다.", question.getTitle()),
                    Map.of("questionId", question.getId(), 
                           "questionTitle", question.getTitle(),
                           "invalidChoices", invalidChoices,
                           "validOptions", validOptions));
        }
    }
}
