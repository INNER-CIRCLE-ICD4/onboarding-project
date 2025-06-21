package fastcampus.inguk_onboarding.form.response.application;

import fastcampus.inguk_onboarding.form.post.jpa.JpaSurveyVersionRepository;
import fastcampus.inguk_onboarding.form.post.repository.entity.post.SurveyItemEntity;
import fastcampus.inguk_onboarding.form.post.repository.entity.post.SurveyVersionEntity;
import fastcampus.inguk_onboarding.form.response.Response;
import fastcampus.inguk_onboarding.form.response.application.dto.ResponseSurveyRequestDto;
import fastcampus.inguk_onboarding.form.response.application.interfaces.ResponseRepository;
import fastcampus.inguk_onboarding.form.response.domain.ResponseContent;
import fastcampus.inguk_onboarding.form.response.domain.ResponseItem;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SurveyResponseService {

    private final ResponseRepository responseRepository;
    private final JpaSurveyVersionRepository surveyVersionRepository;

    @Transactional
    public Response answerSurvey(Long surveyVersionId, ResponseSurveyRequestDto dto) {
        
        // 설문 버전 조회 (Survey와 Items를 함께 fetch)
        SurveyVersionEntity surveyVersion = surveyVersionRepository.findByIdWithSurveyAndItems(surveyVersionId)
                .orElseThrow(() -> new RuntimeException("설문 버전을 찾을 수 없습니다. ID: " + surveyVersionId));
        
        Long surveyId = surveyVersion.getSurvey().getId();
        List<SurveyItemEntity> surveyItems = surveyVersion.getItems();
        
        // 설문 항목이 없는 경우 확인
        if (surveyItems.isEmpty()) {
            throw new RuntimeException("설문 항목이 없습니다. 설문 버전 ID: " + surveyVersionId);
        }
        
        // 응답 값 검증
        validateSurveyResponse(surveyId, surveyVersionId, dto, surveyItems);
        
        // DTO를 도메인 객체로 변환
        Response response = Response.from(surveyId, surveyVersionId, dto);
        
        // 응답 저장
        return responseRepository.save(response);
    }

    @Transactional
    public Response getResponse(Long responseId) {
        try {
            log.info("응답 조회 시작 - responseId: {}", responseId);
            Response response = responseRepository.findById(responseId);
            log.info("응답 조회 성공 - responseId: {}, response: {}", responseId, response);
            return response;
        } catch (Exception e) {
            log.error("응답 조회 실패 - responseId: {}, 오류: {}", responseId, e.getMessage(), e);
            throw new RuntimeException("응답 조회 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    /**
     * 설문조사 전체 응답 조회
     */
    @Transactional
    public List<Response> getSurveyResponses(Long surveyId) {
        try {
            log.info("설문조사 전체 응답 조회 시작 - surveyId: {}", surveyId);
            List<Response> responses = responseRepository.findBySurveyId(surveyId);
            log.info("설문조사 전체 응답 조회 성공 - surveyId: {}, 응답 수: {}", surveyId, responses.size());
            return responses;
        } catch (Exception e) {
            log.error("설문조사 전체 응답 조회 실패 - surveyId: {}, 오류: {}", surveyId, e.getMessage(), e);
            throw new RuntimeException("설문조사 응답 조회 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }
    
    /**
     * 설문 응답 검색 (항목 이름과 응답 값 기반)
     */
    @Transactional
    public List<Response> searchSurveyResponses(Long surveyId, String itemTitle, String answerValue) {
        try {
            log.info("설문조사 응답 검색 시작 - surveyId: {}, itemTitle: {}, answerValue: {}", 
                    surveyId, itemTitle, answerValue);
            
            // 먼저 전체 응답을 조회
            List<Response> allResponses = responseRepository.findBySurveyId(surveyId);
            
            // 설문 버전과 항목 정보를 조회하여 필터링에 사용
            List<SurveyVersionEntity> surveyVersions = surveyVersionRepository.findBySurveyIdWithItems(surveyId);
            
            // 검색 조건에 맞는 응답들 필터링
            List<Response> filteredResponses = allResponses.stream()
                    .filter(response -> matchesSearchCriteria(response, surveyVersions, itemTitle, answerValue))
                    .toList();
            
            log.info("설문조사 응답 검색 완료 - surveyId: {}, 검색 결과: {}개", surveyId, filteredResponses.size());
            return filteredResponses;
            
        } catch (Exception e) {
            log.error("설문조사 응답 검색 실패 - surveyId: {}, 오류: {}", surveyId, e.getMessage(), e);
            throw new RuntimeException("설문조사 응답 검색 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }
    
    /**
     * 응답이 검색 조건에 맞는지 확인
     * 삭제된 항목에 대한 응답도 안전하게 처리
     */
    private boolean matchesSearchCriteria(Response response, List<SurveyVersionEntity> surveyVersions, 
                                        String itemTitle, String answerValue) {
        
        // 해당 응답의 설문 버전 찾기
        SurveyVersionEntity surveyVersion = surveyVersions.stream()
                .filter(version -> version.getId().equals(response.getSurveyVersionId()))
                .findFirst()
                .orElse(null);
        
        if (surveyVersion == null) {
            // 설문 버전이 없어도 응답은 유지되어야 함
            log.warn("설문 버전을 찾을 수 없습니다. 응답 ID: {}, 버전 ID: {}", 
                    response.getResponseId(), response.getSurveyVersionId());
            return matchesSearchCriteriaWithoutVersion(response, itemTitle, answerValue);
        }
        
        // 검색 조건과 매칭되는 응답 항목이 있는지 확인
        return response.getResponseItems().stream()
                .anyMatch(responseItem -> {
                    // 해당 순서의 설문 항목 찾기
                    SurveyItemEntity surveyItem = surveyVersion.getItems().stream()
                            .filter(item -> item.getOrder().equals(responseItem.getItemOrder()))
                            .findFirst()
                            .orElse(null);
                    
                    if (surveyItem == null) {
                        // 삭제된 항목에 대한 응답 - 응답 값만으로 검색
                        log.info("삭제된 항목에 대한 응답 발견. 응답 ID: {}, 항목 순서: {}", 
                                response.getResponseId(), responseItem.getItemOrder());
                        return matchesDeletedItemCriteria(responseItem, itemTitle, answerValue);
                    }
                    
                    // 항목 제목 매칭 (부분 검색 지원)
                    boolean titleMatches = itemTitle == null || itemTitle.trim().isEmpty() || 
                            surveyItem.getTitle().toLowerCase().contains(itemTitle.toLowerCase());
                    
                    // 응답 값 매칭 (부분 검색 지원)
                    boolean valueMatches = answerValue == null || answerValue.trim().isEmpty() || 
                            responseItem.getAnswer().toLowerCase().contains(answerValue.toLowerCase());
                    
                    return titleMatches && valueMatches;
                });
    }
    
    /**
     * 설문 버전 정보 없이 응답 검색 (레거시 응답 지원)
     */
    private boolean matchesSearchCriteriaWithoutVersion(Response response, String itemTitle, String answerValue) {
        // 항목 제목 검색은 불가능하므로 응답 값만 검색
        if (itemTitle != null && !itemTitle.trim().isEmpty()) {
            return false; // 항목 제목으로 검색할 수 없음
        }
        
        if (answerValue == null || answerValue.trim().isEmpty()) {
            return true; // 조건이 없으면 모든 응답 포함
        }
        
        return response.getResponseItems().stream()
                .anyMatch(responseItem -> 
                    responseItem.getAnswer().toLowerCase().contains(answerValue.toLowerCase()));
    }
    
    /**
     * 삭제된 항목에 대한 응답 검색 조건 확인
     */
    private boolean matchesDeletedItemCriteria(ResponseItem responseItem, String itemTitle, String answerValue) {
        // 삭제된 항목은 제목으로 검색할 수 없으므로 응답 값만 확인
        if (itemTitle != null && !itemTitle.trim().isEmpty()) {
            return false; // 삭제된 항목은 제목 검색 불가
        }
        
        // 응답 값 매칭 (부분 검색 지원)
        return answerValue == null || answerValue.trim().isEmpty() || 
                responseItem.getAnswer().toLowerCase().contains(answerValue.toLowerCase());
    }
    
    /**
     * 설문 응답 검증
     */
    private void validateSurveyResponse(Long surveyId, Long surveyVersionId, ResponseSurveyRequestDto dto, List<SurveyItemEntity> surveyItems) {
        List<ResponseItem> responseItems = dto.answers().stream()
                .map(ResponseItem::from)
                .toList();

        if (responseItems.isEmpty()) {
            throw new RuntimeException("응답이 비어있습니다.");
        }
        
        // ResponseContent로 통합 검증
        ResponseContent responseContent = ResponseContent.create(
                surveyId, 
                surveyVersionId, 
                responseItems,
                surveyItems
        );
        
        // 전체 검증 수행 (validate 메서드에서 모든 검증을 수행)
        if (!responseContent.validate()) {
            throw new RuntimeException("설문 응답이 유효하지 않습니다.");
        }
        
        // 상세 비즈니스 규칙 검증 및 구체적 에러 메시지
        if (responseContent.hasDuplicateOrder()) {
            throw new RuntimeException("중복된 응답 순서가 있습니다.");
        }
        
        if (!responseContent.hasValidOrder()) {
            throw new RuntimeException("존재하지 않는 설문 항목에 대한 응답이 포함되어 있습니다.");
        }
        
        if (!responseContent.hasAllRequiredAnswers()) {
            throw new RuntimeException("필수 설문 항목에 대한 응답이 누락되었습니다.");
        }
        
        // 설문 항목과 응답의 완전 일치 검증
        if (!hasExactMatchValidation(responseItems, surveyItems)) {
            throw new RuntimeException("응답 항목이 설문 조사의 항목과 일치하지 않습니다.");
        }
    }
    
    /**
     * 설문 항목과 응답의 완전 일치 검증
     */
    private boolean hasExactMatchValidation(List<ResponseItem> responseItems, List<SurveyItemEntity> surveyItems) {
        // 설문 항목의 순서들
        Set<Integer> surveyOrders = surveyItems.stream()
                .map(SurveyItemEntity::getOrder)
                .collect(Collectors.toSet());
        
        // 응답 항목의 순서들  
        Set<Integer> responseOrders = responseItems.stream()
                .map(ResponseItem::getItemOrder)
                .collect(Collectors.toSet());
        
        // 필수 설문 항목의 순서들
        Set<Integer> requiredOrders = surveyItems.stream()
                .filter(SurveyItemEntity::getRequired)
                .map(SurveyItemEntity::getOrder)
                .collect(Collectors.toSet());
        
        // 1. 모든 필수 설문 항목에 응답이 있어야 함
        if (!responseOrders.containsAll(requiredOrders)) {
            log.warn("필수 설문 항목 누락 - 필수: {}, 응답: {}", requiredOrders, responseOrders);
            return false;
        }
        
        // 2. 모든 응답이 실제 존재하는 설문 항목이어야 함
        if (!surveyOrders.containsAll(responseOrders)) {
            log.warn("존재하지 않는 설문 항목에 대한 응답 - 설문: {}, 응답: {}", surveyOrders, responseOrders);
            return false;
        }
        
        return true;
    }
}

