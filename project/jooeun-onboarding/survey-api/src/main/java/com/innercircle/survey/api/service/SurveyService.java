package com.innercircle.survey.api.service;

import com.innercircle.survey.api.dto.request.CreateQuestionRequest;
import com.innercircle.survey.api.dto.request.CreateSurveyRequest;
import com.innercircle.survey.api.dto.request.UpdateSurveyRequest;
import com.innercircle.survey.api.dto.response.SurveyResponse;
import com.innercircle.survey.api.exception.AccessDeniedException;
import com.innercircle.survey.api.exception.SurveyNotFoundException;
import com.innercircle.survey.common.exception.BusinessException;
import com.innercircle.survey.common.exception.ErrorCode;
import com.innercircle.survey.domain.survey.Survey;
import com.innercircle.survey.domain.survey.SurveyQuestion;
import com.innercircle.survey.infrastructure.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 설문조사 서비스
 *
 * 설문조사 관련 비즈니스 로직을 처리합니다.
 * 
 * 예외 처리 원칙:
 * - 모든 비즈니스 예외는 BusinessException 계열 사용
 * - RuntimeException이므로 트랜잭션 자동 롤백
 * - 컨트롤러에서 try-catch 없이 GlobalExceptionHandler에서 일괄 처리
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SurveyService {

    private final SurveyRepository surveyRepository;

    /**
     * 설문조사 생성
     *
     * @param request 설문조사 생성 요청
     * @return 생성된 설문조사
     */
    @Transactional
    public SurveyResponse createSurvey(CreateSurveyRequest request) {
        log.info("설문조사 생성 시작 - 제목: {}, 질문 수: {}", request.getTitle(), request.getQuestions().size());

        try {
            // 1. 설문조사 기본 정보 생성
            Survey survey = new Survey(request.getTitle(), request.getDescription(), request.getCreatedBy());

            // 2. 설문 항목들 생성 및 추가
            List<SurveyQuestion> questions = createQuestions(request.getQuestions());
            for (SurveyQuestion question : questions) {
                survey.addQuestion(question);
            }

            // 3. 설문조사 저장
            Survey savedSurvey = surveyRepository.save(survey);

            log.info("설문조사 생성 완료 - ID: {}, 제목: {}", savedSurvey.getId(), savedSurvey.getTitle());

            return new SurveyResponse(savedSurvey);

        } catch (IllegalArgumentException e) {
            // 비즈니스 규칙 위반을 명확한 예외로 변환
            log.warn("설문조사 생성 중 비즈니스 규칙 위반 - 제목: {}, 오류: {}", request.getTitle(), e.getMessage());
            throw new BusinessException(ErrorCode.SURVEY_CREATION_FAILED, e.getMessage(), 
                    Map.of("title", request.getTitle(), "questionsCount", request.getQuestions().size()));
        } catch (Exception e) {
            log.error("설문조사 생성 실패 - 제목: {}, 오류: {}", request.getTitle(), e.getMessage(), e);
            throw new BusinessException(ErrorCode.SURVEY_CREATION_FAILED, 
                    "설문조사 생성 중 예상치 못한 오류가 발생했습니다.", e);
        }
    }

    /**
     * 설문조사 ID로 조회
     *
     * @param surveyId 설문조사 ID
     * @return 설문조사
     */
    public SurveyResponse getSurvey(String surveyId) {
        log.info("설문조사 조회 - ID: {}", surveyId);

        Survey survey = surveyRepository.findByIdWithActiveQuestions(surveyId)
                .orElseThrow(() -> new SurveyNotFoundException(surveyId));

        return new SurveyResponse(survey);
    }

    /**
     * 설문조사 수정
     *
     * @param surveyId 설문조사 ID
     * @param request 수정 요청
     * @return 수정된 설문조사
     */
    @Transactional
    public SurveyResponse updateSurvey(String surveyId, UpdateSurveyRequest request) {
        log.info("설문조사 수정 시작 - ID: {}, 제목: {}, 수정자: {}", surveyId, request.getTitle(), request.getModifiedBy());

        try {
            // 1. 기존 설문조사 조회
            Survey survey = surveyRepository.findById(surveyId)
                    .orElseThrow(() -> new SurveyNotFoundException(surveyId));

            // 2. 생성자 권한 확인
            if (!survey.isCreatedBy(request.getModifiedBy())) {
                throw new AccessDeniedException(surveyId, request.getModifiedBy());
            }

            // 3. 비활성화된 설문조사 수정 방지
            if (!survey.isActive()) {
                throw new BusinessException(ErrorCode.SURVEY_ALREADY_INACTIVE,
                        String.format("비활성화된 설문조사는 수정할 수 없습니다: %s", surveyId),
                        Map.of("surveyId", surveyId, "active", false));
            }

            // 4. 기본 정보 수정
            survey.updateInfo(request.getTitle(), request.getDescription());

            // 5. 질문 수정 (기존 응답 보존을 위해 기존 질문은 비활성화하고 새 질문 추가)
            List<SurveyQuestion> newQuestions = createQuestions(request.getQuestions());
            survey.updateQuestions(newQuestions);

            // 6. 저장
            Survey updatedSurvey = surveyRepository.save(survey);

            log.info("설문조사 수정 완료 - ID: {}, 새로운 질문 수: {}", surveyId, newQuestions.size());

            return new SurveyResponse(updatedSurvey);

        } catch (OptimisticLockingFailureException e) {
            // JPA 낙관적 락 충돌 - GlobalExceptionHandler에서 처리
            log.warn("설문조사 수정 중 낙관적 락 충돌 - ID: {}", surveyId, e);
            throw e;
        } catch (BusinessException e) {
            // 이미 적절한 예외 타입이므로 그대로 전파
            throw e;
        } catch (IllegalArgumentException e) {
            // 비즈니스 규칙 위반
            log.warn("설문조사 수정 중 비즈니스 규칙 위반 - ID: {}, 오류: {}", surveyId, e.getMessage());
            throw new BusinessException(ErrorCode.SURVEY_UPDATE_FAILED, e.getMessage(),
                    Map.of("surveyId", surveyId, "modifiedBy", request.getModifiedBy()));
        } catch (Exception e) {
            log.error("설문조사 수정 실패 - ID: {}, 오류: {}", surveyId, e.getMessage(), e);
            throw new BusinessException(ErrorCode.SURVEY_UPDATE_FAILED,
                    "설문조사 수정 중 예상치 못한 오류가 발생했습니다.", e);
        }
    }

    /**
     * 설문조사 비활성화 (논리적 삭제)
     *
     * @param surveyId 설문조사 ID
     * @param requestedBy 요청자
     */
    @Transactional
    public void deactivateSurvey(String surveyId, String requestedBy) {
        log.info("설문조사 비활성화 시작 - ID: {}, 요청자: {}", surveyId, requestedBy);

        try {
            // 1. 기존 설문조사 조회
            Survey survey = surveyRepository.findById(surveyId)
                    .orElseThrow(() -> new SurveyNotFoundException(surveyId));

            // 2. 생성자 권한 확인
            if (!survey.isCreatedBy(requestedBy)) {
                throw new AccessDeniedException(surveyId, requestedBy);
            }

            // 3. 이미 비활성화된 설문조사 체크
            if (!survey.isActive()) {
                throw new BusinessException(ErrorCode.SURVEY_ALREADY_INACTIVE,
                        String.format("이미 비활성화된 설문조사입니다: %s", surveyId),
                        Map.of("surveyId", surveyId, "active", false));
            }

            // 4. 비활성화
            survey.deactivate();

            // 5. 저장
            surveyRepository.save(survey);

            log.info("설문조사 비활성화 완료 - ID: {}", surveyId);

        } catch (BusinessException e) {
            // 이미 적절한 예외 타입이므로 그대로 전파
            throw e;
        } catch (Exception e) {
            log.error("설문조사 비활성화 실패 - ID: {}, 오류: {}", surveyId, e.getMessage(), e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR,
                    "설문조사 비활성화 중 예상치 못한 오류가 발생했습니다.", e);
        }
    }

    /**
     * 설문조사 존재 여부 확인
     *
     * @param surveyId 설문조사 ID
     * @return 존재 여부
     */
    public boolean existsSurvey(String surveyId) {
        try {
            return surveyRepository.existsById(surveyId);
        } catch (Exception e) {
            log.warn("설문조사 존재 확인 중 오류 - ID: {}, 오류: {}", surveyId, e.getMessage(), e);
            // 존재 확인 실패는 존재하지 않는 것으로 간주
            return false;
        }
    }

    /**
     * 요청 DTO로부터 설문 항목들 생성
     */
    private List<SurveyQuestion> createQuestions(List<CreateQuestionRequest> questionRequests) {
        try {
            return questionRequests.stream()
                    .map(this::createQuestion)
                    .toList();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.QUESTION_OPTIONS_INVALID,
                    "설문 질문 생성 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    /**
     * 개별 설문 항목 생성
     */
    private SurveyQuestion createQuestion(CreateQuestionRequest request) {
        try {
            // 선택 옵션 검증
            request.validateOptions();
            
            if (request.getQuestionType().isChoiceType()) {
                // 선택지가 없는 선택형 질문 검증
                if (request.getOptions() == null || request.getOptions().isEmpty()) {
                    throw new BusinessException(ErrorCode.QUESTION_OPTIONS_REQUIRED,
                            String.format("선택형 질문에는 선택지가 필요합니다: %s", request.getTitle()));
                }
                
                return new SurveyQuestion(
                        request.getTitle(),
                        request.getDescription(),
                        request.getQuestionType(),
                        request.isRequired(),
                        request.getOptions()
                );
            } else {
                return new SurveyQuestion(
                        request.getTitle(),
                        request.getDescription(),
                        request.getQuestionType(),
                        request.isRequired()
                );
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.QUESTION_TYPE_INVALID,
                    String.format("질문 생성 중 오류가 발생했습니다: %s", request.getTitle()), e);
        }
    }
}
