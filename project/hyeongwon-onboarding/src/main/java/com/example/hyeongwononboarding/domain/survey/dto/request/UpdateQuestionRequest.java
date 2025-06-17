package com.example.hyeongwononboarding.domain.survey.dto.request;

import com.example.hyeongwononboarding.domain.survey.entity.QuestionInputType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 설문지 수정 시 사용되는 질문 요청 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateQuestionRequest implements QuestionRequest {
    /**
     * 질문 ID (기존 질문 수정 시 사용)
     */
    private UUID id;
    
    @NotBlank(message = "질문 제목은 필수입니다.")
    @Size(max = 255, message = "질문 제목은 255자를 초과할 수 없습니다.")
    private String name;
    
    private String description;
    
    @NotNull(message = "질문 유형은 필수입니다.")
    private QuestionInputType inputType;
    
    @NotNull(message = "필수 여부는 필수입니다.")
    private Boolean isRequired;
    
    @NotNull(message = "질문 순서는 필수입니다.")
    @Positive(message = "질문 순서는 양수여야 합니다.")
    private Integer order;
    
    @Valid
    private List<QuestionOptionDto> options;

    @Override
    public List<QuestionOptionRequest> getOptions() {
        if (options == null) {
            return List.of();
        }
        return options.stream()
                .map(option -> (QuestionOptionRequest) option)
                .collect(Collectors.toList());
    }
    
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * 설문지 수정 시 사용되는 질문 옵션 요청 DTO
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionOptionDto implements QuestionOptionRequest {
        private String id;
        
        @NotBlank(message = "옵션 내용은 필수입니다.")
        @Size(max = 255, message = "옵션 텍스트는 255자를 초과할 수 없습니다.")
        private String text;
        
        @NotNull(message = "옵션 순서는 필수입니다.")
        @Positive(message = "옵션 순서는 양수여야 합니다.")
        private Integer order;
        
        @Override
        public String getId() {
            return id != null ? id : UUID.randomUUID().toString();
        }
    }
}
