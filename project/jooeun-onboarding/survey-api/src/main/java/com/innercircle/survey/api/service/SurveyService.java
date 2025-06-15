package com.innercircle.survey.api.service;

import com.innercircle.survey.api.dto.request.CreateQuestionRequest;
import com.innercircle.survey.api.dto.request.CreateSurveyRequest;
import com.innercircle.survey.api.dto.request.UpdateSurveyRequest;
import com.innercircle.survey.api.dto.response.SurveyResponse;
import com.innercircle.survey.api.exception.AccessDeniedException;
import com.innercircle.survey.api.exception.SurveyNotFoundException;
import com.innercircle.survey.domain.survey.Survey;
import com.innercircle.survey.domain.survey.SurveyQuestion;
import com.innercircle.survey.infrastructure.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 설문조사 서비스
 *
 * 설문조사 관련 비즈니스 로직을 처리합니다.
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
            Survey survey = new Survey(request.getTitle(), request.getDescription());

            // 2. 설문 항목들 생성 및 추가
            List<SurveyQuestion> questions = createQuestions(request.getQuestions());
            for (SurveyQuestion question : questions) {
                survey.addQuestion(question);
            }

            // 3. 설문조사 저장
            Survey savedSurvey = surveyRepository.save(survey);

            log.info("설문조사 생성 완료 - ID: {}, 제목: {}", savedSurvey.getId(), savedSurvey.getTitle());

            return new SurveyResponse(savedSurvey);

        } catch (Exception e) {
            log.error("설문조사 생성 실패 - 제목: {}, 오류: {}", request.getTitle(), e.getMessage(), e);
            throw new RuntimeException("설문조사 생성에 실패했습니다: " + e.getMessage(), e);
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
                throw new AccessDeniedException("설문조사 수정 권한이 없습니다.");
            }

            // 3. 기본 정보 수정
            survey.updateInfo(request.getTitle(), request.getDescription());

            // 4. 질문 수정 (기존 응답 보존을 위해 기존 질문은 비활성화하고 새 질문 추가)
            List<SurveyQuestion> newQuestions = createQuestions(request.getQuestions());
            survey.updateQuestions(newQuestions);

            // 5. 저장
            Survey updatedSurvey = surveyRepository.save(survey);

            log.info("설문조사 수정 완료 - ID: {}, 새로운 질문 수: {}", surveyId, newQuestions.size());

            return new SurveyResponse(updatedSurvey);

        } catch (OptimisticLockingFailureException e) {
            log.warn("설문조사 수정 중 낙관적 락 충돌 - ID: {}", surveyId);
            throw e; // 글로벌 예외 핸들러에서 처리
        } catch (SurveyNotFoundException | AccessDeniedException e) {
            // 이미 적절한 예외 타입이므로 그대로 전파
            throw e;
        } catch (Exception e) {
            log.error("설문조사 수정 실패 - ID: {}, 오류: {}", surveyId, e.getMessage(), e);
            throw new RuntimeException("설문조사 수정에 실패했습니다: " + e.getMessage(), e);
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
                throw new AccessDeniedException("설문조사 비활성화 권한이 없습니다.");
            }

            // 3. 비활성화
            survey.deactivate();

            // 4. 저장
            surveyRepository.save(survey);

            log.info("설문조사 비활성화 완료 - ID: {}", surveyId);

        } catch (SurveyNotFoundException | AccessDeniedException e) {
            // 이미 적절한 예외 타입이므로 그대로 전파
            throw e;
        } catch (Exception e) {
            log.error("설문조사 비활성화 실패 - ID: {}, 오류: {}", surveyId, e.getMessage(), e);
            throw new RuntimeException("설문조사 비활성화에 실패했습니다: " + e.getMessage(), e);
        }
    }

    /**
     * 설문조사 존재 여부 확인
     *
     * @param surveyId 설문조사 ID
     * @return 존재 여부
     */
    public boolean existsSurvey(String surveyId) {
        return surveyRepository.existsById(surveyId);
    }

    /**
     * 요청 DTO로부터 설문 항목들 생성
     */
    private List<SurveyQuestion> createQuestions(List<CreateQuestionRequest> questionRequests) {
        return questionRequests.stream()
                .map(this::createQuestion)
                .toList();
    }

    /**
     * 개별 설문 항목 생성
     */
    private SurveyQuestion createQuestion(CreateQuestionRequest request) {
        // 선택 옵션 검증
        request.validateOptions();
        
        if (request.getQuestionType().isChoiceType()) {
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
    }
}
