package fastcampus.inguk_onboarding.form.post.domain.content;

import fastcampus.inguk_onboarding.form.post.domain.Surveys.InputType;
import lombok.Getter;

import java.util.List;

/**
 * 개별 설문 항목을 나타내는 클래스
 * Content를 상속받아 설문 항목의 상세 정보를 가집니다.
 */
@Getter
public class SurveyItem extends Content {
    private final InputType inputType;
    private final Boolean required;
    private final Integer order;
    private final List<String> options;
    
    public SurveyItem(String name, String description, InputType inputType, 
                     Boolean required, Integer order, List<String> options) {
        super(name, description);
        
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
        
        this.inputType = inputType;
        this.required = required;
        this.order = order;
        this.options = options;
    }
    
    private boolean isChoiceType(InputType inputType) {
        return inputType == InputType.SINGLE_TYPE || inputType == InputType.MULTIPLE_TYPE;
    }

    public boolean hasOptions() {
        return options != null && !options.isEmpty();
    }
    
    /**
     * 기존 SurveyItemEntity와 현재 SurveyItem의 내용이 다른지 확인
     */
    public boolean isDifferentFrom(String existingName, String existingDescription, 
                                  InputType existingInputType, Boolean existingRequired, 
                                  Integer existingOrder, List<String> existingOptions) {
        return !name.equals(existingName) ||
               !description.equals(existingDescription) ||
               !inputType.equals(existingInputType) ||
               !required.equals(existingRequired) ||
               !order.equals(existingOrder) ||
               areOptionsDifferent(existingOptions);
    }
    
    /**
     * 선택지 변경 여부 확인
     */
    private boolean areOptionsDifferent(List<String> existingOptions) {
        if (options == null && existingOptions == null) return false;
        if (options == null || existingOptions == null) return true;
        if (options.size() != existingOptions.size()) return true;
        
        return !options.containsAll(existingOptions) || !existingOptions.containsAll(options);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        if (!(obj instanceof SurveyItem)) return false;
        
        SurveyItem that = (SurveyItem) obj;
        return inputType == that.inputType &&
               required.equals(that.required) &&
               order.equals(that.order) &&
               (options != null ? options.equals(that.options) : that.options == null);
    }
    
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + inputType.hashCode();
        result = 31 * result + required.hashCode();
        result = 31 * result + order.hashCode();
        result = 31 * result + (options != null ? options.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return "SurveyItem{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", inputType=" + inputType +
                ", required=" + required +
                ", order=" + order +
                ", options=" + options +
                '}';
    }
} 