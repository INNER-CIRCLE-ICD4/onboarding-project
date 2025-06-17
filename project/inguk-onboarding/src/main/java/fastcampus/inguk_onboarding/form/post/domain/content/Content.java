package fastcampus.inguk_onboarding.form.post.domain.content;

import fastcampus.inguk_onboarding.form.post.domain.Surveys.InputType;

import java.util.List;

public record Content(
        String name,
        String description,
        InputType inputType,
        Boolean required,
        Integer order,
        List<String> options
) {
    public Content {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("항목 이름은 필수입니다.");
        }
        
        if (inputType == null) {
            throw new IllegalArgumentException("항목 입력 형태는 필수입니다.");
        }
        
        if (required == null) {
            throw new IllegalArgumentException("항목 필수 여부는 필수입니다.");
        }
        
        if (order == null) {
            throw new IllegalArgumentException("항목 순서는 필수입니다.");
        }
        
        // 선택형 타입의 경우 옵션 검증
        if (isChoiceType(inputType) && (options == null || options.isEmpty())) {
            throw new IllegalArgumentException("선택형 항목은 선택 옵션이 필요합니다.");
        }
    }
    
    private boolean isChoiceType(InputType inputType) {
        return inputType == InputType.SINGLE_TYPE || inputType == InputType.MULTIPLE_TYPE;
    }
    
    public boolean hasOptions() {
        return options != null && !options.isEmpty();
    }
} 