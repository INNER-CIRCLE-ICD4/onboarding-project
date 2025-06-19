package fastcampus.inguk_onboarding.form.response.domain;

import fastcampus.inguk_onboarding.form.post.domain.Surveys.InputType;
import fastcampus.inguk_onboarding.form.post.repository.entity.post.SurveyItemEntity;
import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 설문 응답 내용 검증 클래스
 * Content 클래스를 상속받아 전체 응답 리스트 검증 기능을 구현
 */
@Getter
public class ResponseContent extends Content {

    private final Long surveyId;
    private final Long surveyVersionId;
    private final List<ResponseItem> responseItems;
    private final List<SurveyItemEntity> surveyItems; // 실제 설문 항목들
    
    @Builder
    public ResponseContent(String type, String value, Long surveyId, Long surveyVersionId, 
                          List<ResponseItem> responseItems, List<SurveyItemEntity> surveyItems) {
        super(type != null ? type : "SURVEY_RESPONSE", value != null ? value : "");
        this.surveyId = surveyId;
        this.surveyVersionId = surveyVersionId;
        this.responseItems = responseItems;
        this.surveyItems = surveyItems;
    }
    
    public static ResponseContent create(Long surveyId, Long surveyVersionId, 
                                       List<ResponseItem> responseItems, List<SurveyItemEntity> surveyItems) {
        return ResponseContent.builder()
                .type("SURVEY_RESPONSE")
                .value("응답 내용")
                .surveyId(surveyId)
                .surveyVersionId(surveyVersionId)
                .responseItems(responseItems)
                .surveyItems(surveyItems)
                .build();
    }
    
    /**
     * 메인 검증 메서드 - Content의 validateAll 사용
     */
    public boolean validate() {
        return validateAll();
    }

    // Content 클래스의 추상 메서드 구현 (개별 응답 검증용 - 사용하지 않음)
    @Override
    public boolean isValid() {
        return true; // 전체 검증에서는 사용하지 않음
    }

    @Override
    public boolean isRequired() {
        return false; // 전체 검증에서는 사용하지 않음
    }

    @Override
    public boolean isValidFormat() {
        return true; // 전체 검증에서는 사용하지 않음
    }

    @Override
    public boolean isWithinRange() {
        return true; // 전체 검증에서는 사용하지 않음
    }

    @Override
    public String getDisplayValue() {
        return ""; // 전체 검증에서는 사용하지 않음
    }

    /**
     * 설문 항목과의 검증 구현
     */
    @Override
    protected boolean validateWithSurveyItems() {
        return responseItems.stream()
                .allMatch(this::validateResponseItem);
    }

    /**
     * 비즈니스 규칙 검증 구현
     */
    @Override
    protected boolean validateBusinessRules() {
        return !hasDuplicateOrder() && 
               hasValidOrder() && 
               hasAllRequiredAnswers();
    }

    /**
     * 개별 응답 항목 검증
     */
    private boolean validateResponseItem(ResponseItem responseItem) {
        // 해당 순서의 설문 항목 찾기
        SurveyItemEntity surveyItem = surveyItems.stream()
                .filter(item -> item.getOrder().equals(responseItem.getItemOrder()))
                .findFirst()
                .orElse(null);

        if (surveyItem == null) {
            return false; // 해당 순서의 설문 항목이 없음
        }

        // InputType에 따른 검증
        return validateByInputType(responseItem.getAnswer(), surveyItem);
    }

    /**
     * InputType에 따른 응답 값 검증
     */
    private boolean validateByInputType(String answer, SurveyItemEntity surveyItem) {
        InputType inputType = surveyItem.getInputType();
        
        switch (inputType) {
            case SHORT_TYPE:
                return validateShortType(answer);
            case LONG_TYPE:
                return validateLongType(answer);
            case SINGLE_TYPE:
                return validateSingleType(answer, surveyItem.getOptions());
            case MULTIPLE_TYPE:
                return validateMultipleType(answer, surveyItem.getOptions());
            default:
                return false;
        }
    }

    /**
     * SHORT_TYPE 검증 (1~100자)
     */
    private boolean validateShortType(String answer) {
        return answer != null && 
               answer.length() >= 1 && 
               answer.length() <= 100;
    }

    /**
     * LONG_TYPE 검증 (1~1000자)
     */
    private boolean validateLongType(String answer) {
        return answer != null && 
               answer.length() >= 1 && 
               answer.length() <= 1000;
    }

    /**
     * SINGLE_TYPE 검증 (제공된 옵션 중 하나)
     */
    private boolean validateSingleType(String answer, List<String> options) {
        return answer != null && 
               options != null && 
               options.contains(answer);
    }

    /**
     * MULTIPLE_TYPE 검증 (제공된 옵션 중 여러 개, 콤마로 구분)
     */
    private boolean validateMultipleType(String answer, List<String> options) {
        if (answer == null || options == null) {
            return false;
        }

        String[] selectedOptions = answer.split(",");
        return Arrays.stream(selectedOptions)
                .map(String::trim)
                .allMatch(options::contains);
    }
    
    // 중복된 응답 순서가 있는지 확인
    public boolean hasDuplicateOrder() {
        if (responseItems == null) {
            return false;
        }
        
        long distinctOrderCount = responseItems.stream()
                .map(ResponseItem::getItemOrder)
                .distinct()
                .count();
                
        return distinctOrderCount != responseItems.size();
    }
    
    // 응답 순서가 설문 항목 순서와 일치하는지 확인
    public boolean hasValidOrder() {
        if (responseItems == null || surveyItems == null) {
            return false;
        }
        
        List<Integer> responseOrders = responseItems.stream()
                .map(ResponseItem::getItemOrder)
                .sorted()
                .toList();
                
        List<Integer> surveyOrders = surveyItems.stream()
                .map(SurveyItemEntity::getOrder)
                .sorted()
                .toList();
                
        return responseOrders.equals(surveyOrders);
    }
    
    // 필수 응답이 모두 있는지 확인
    public boolean hasAllRequiredAnswers() {
        if (surveyItems == null || responseItems == null) {
            return false;
        }
        
        Map<Integer, ResponseItem> responseMap = responseItems.stream()
                .collect(Collectors.toMap(ResponseItem::getItemOrder, item -> item));
        
        return surveyItems.stream()
                .filter(SurveyItemEntity::getRequired)
                .allMatch(surveyItem -> {
                    ResponseItem response = responseMap.get(surveyItem.getOrder());
                    return response != null && 
                           response.getAnswer() != null && 
                           !response.getAnswer().trim().isEmpty();
                });
    }
}
