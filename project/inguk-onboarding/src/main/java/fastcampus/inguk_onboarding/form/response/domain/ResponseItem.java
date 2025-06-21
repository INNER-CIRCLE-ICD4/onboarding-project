package fastcampus.inguk_onboarding.form.response.domain;

import fastcampus.inguk_onboarding.form.post.domain.Surveys.InputType;
import fastcampus.inguk_onboarding.form.response.application.dto.ResponseSurveyItemRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ResponseItem {
    
    private final Integer itemOrder;
    private final String answer;
    
    public static ResponseItem from(ResponseSurveyItemRequestDto dto) {
        return ResponseItem.builder()
                .itemOrder(dto.item_order())
                .answer(dto.answer())
                .build();
    }
    
    // InputType에 따른 응답 검증
    public boolean isValidForInputType(InputType inputType, List<String> options) {
        if (answer == null || answer.trim().isEmpty()) {
            return false;
        }
        
        return switch (inputType) {
            case SHORT_TYPE -> isValidShortAnswer();
            case LONG_TYPE -> isValidLongAnswer();
            case SINGLE_TYPE -> isValidSingleChoice(options);
            case MULTIPLE_TYPE -> isValidMultipleChoice(options);
        };
    }
    
    // 단답형 검증
    private boolean isValidShortAnswer() {
        String trimmedAnswer = answer.trim();
        return trimmedAnswer.length() >= 1 && trimmedAnswer.length() <= 100;
    }
    
    // 장문형 검증
    private boolean isValidLongAnswer() {
        String trimmedAnswer = answer.trim();
        return trimmedAnswer.length() >= 1 && trimmedAnswer.length() <= 1000;
    }
    
    // 단일 선택 검증
    private boolean isValidSingleChoice(List<String> options) {
        if (options == null || options.isEmpty()) {
            return false;
        }
        
        String trimmedAnswer = answer.trim();
        return options.contains(trimmedAnswer);
    }
    
    // 다중 선택 검증 (콤마로 구분된 값들)
    private boolean isValidMultipleChoice(List<String> options) {
        if (options == null || options.isEmpty()) {
            return false;
        }
        
        // 콤마로 구분된 답변들을 파싱
        List<String> selectedAnswers = Arrays.stream(answer.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
        
        if (selectedAnswers.isEmpty()) {
            return false;
        }
        
        // 모든 선택된 답변이 옵션에 포함되어야 함
        return options.containsAll(selectedAnswers);
    }
    
    // 필수 항목 검증
    public boolean isValidRequired(boolean required) {
        if (!required) {
            return true; // 필수가 아니면 항상 유효
        }
        
        return answer != null && !answer.trim().isEmpty();
    }
    
    // 항목 순서 검증
    public boolean isValidOrder() {
        return itemOrder != null && itemOrder > 0;
    }
}
