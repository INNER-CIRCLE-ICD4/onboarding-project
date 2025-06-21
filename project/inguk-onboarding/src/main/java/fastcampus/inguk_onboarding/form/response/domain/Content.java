package fastcampus.inguk_onboarding.form.response.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import fastcampus.inguk_onboarding.form.post.repository.entity.post.SurveyItemEntity;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 응답 내용 검증의 기본 클래스
 * 개별 응답 검증과 전체 응답 리스트 검증을 모두 지원
 */
@Getter
public abstract class Content {
    
    // 개별 응답 검증용 필드
    protected final String type;
    protected final String value;
    
    // 전체 응답 리스트 검증용 필드
    protected Long surveyId;
    protected Long surveyVersionId;
    protected List<ResponseItem> responseItems;
    protected List<SurveyItemEntity> surveyItems;
    
    // 개별 응답 검증용 생성자
    protected Content(String type, String value) {
        this.type = type;
        this.value = value;
        this.surveyId = null;
        this.surveyVersionId = null;
        this.responseItems = null;
        this.surveyItems = null;
    }
    
    // 전체 응답 리스트 검증용 생성자
    protected Content(Long surveyId, Long surveyVersionId, List<ResponseItem> responseItems, List<SurveyItemEntity> surveyItems) {
        this.type = null;
        this.value = null;
        this.surveyId = surveyId;
        this.surveyVersionId = surveyVersionId;
        this.responseItems = responseItems;
        this.surveyItems = surveyItems;
    }
    
    // 개별 응답 검증 메서드들
    public abstract boolean isValid();
    public abstract boolean isRequired();
    public abstract boolean isValidFormat();
    public abstract boolean isWithinRange();
    public abstract String getDisplayValue();
    
    // 개별 응답 전체 검증
    public boolean validateSingle() {
        if (isRequired() && (value == null || value.trim().isEmpty())) {
            return false;
        }
        return isValid() && isValidFormat() && isWithinRange();
    }
    
    /**
     * 전체 응답 리스트 검증 수행
     */
    public boolean validateAll() {
        if (responseItems == null || surveyItems == null) {
            return validateSingle(); // 개별 검증으로 폴백
        }
        
        return validateBasicFormat() && 
               validateWithSurveyItems() && 
               validateBusinessRules();
    }
    
    /**
     * 기본 형식 검증
     */
    protected boolean validateBasicFormat() {
        if (responseItems == null || responseItems.isEmpty()) {
            return false;
        }
        
        return responseItems.stream()
                .allMatch(item -> item != null && 
                                item.getItemOrder() != null && 
                                item.getAnswer() != null && 
                                !item.getAnswer().trim().isEmpty());
    }
    
    /**
     * 설문 항목과의 검증 (구체적인 구현은 하위 클래스에서)
     */
    protected abstract boolean validateWithSurveyItems();
    
    /**
     * 비즈니스 규칙 검증 (구체적인 구현은 하위 클래스에서)
     */
    protected abstract boolean validateBusinessRules();
    
    /**
     * 중복된 응답 순서 확인
     */
    public boolean hasDuplicateOrder() {
        if (responseItems == null) return false;
        
        Set<Integer> orders = responseItems.stream()
                .map(ResponseItem::getItemOrder)
                .collect(Collectors.toSet());
        
        return orders.size() != responseItems.size();
    }
    
    /**
     * 응답 순서가 설문 항목 순서와 일치하는지 확인
     */
    public boolean hasValidOrder() {
        if (responseItems == null || surveyItems == null) return false;
        
        Set<Integer> responseOrders = responseItems.stream()
                .map(ResponseItem::getItemOrder)
                .collect(Collectors.toSet());
        
        Set<Integer> surveyOrders = surveyItems.stream()
                .map(SurveyItemEntity::getOrder)
                .collect(Collectors.toSet());
        
        return surveyOrders.containsAll(responseOrders);
    }
    
    /**
     * 필수 응답이 모두 있는지 확인
     */
    public boolean hasAllRequiredAnswers() {
        if (responseItems == null || surveyItems == null) return false;
        
        Set<Integer> responseOrders = responseItems.stream()
                .map(ResponseItem::getItemOrder)
                .collect(Collectors.toSet());
        
        List<Integer> requiredOrders = surveyItems.stream()
                .filter(SurveyItemEntity::getRequired)
                .map(SurveyItemEntity::getOrder)
                .toList();
        
        return responseOrders.containsAll(requiredOrders);
    }
    
    // Getters
    public Long getSurveyId() {
        return surveyId;
    }
    
    public Long getSurveyVersionId() {
        return surveyVersionId;
    }
    
    public List<ResponseItem> getResponseItems() {
        return responseItems;
    }
    
    public List<SurveyItemEntity> getSurveyItems() {
        return surveyItems;
    }
}
