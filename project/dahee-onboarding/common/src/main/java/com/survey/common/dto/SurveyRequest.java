package com.survey.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 설문 생성/수정시 클라이언트 → 서버로 보내는 요청 DTO
 */
@Getter
@Setter
public class SurveyRequest {
    @NotBlank
    private String seriesCode;
    @NotBlank private String title;
    private String description;

    @NotEmpty
    @Size(max=10)
    private List<Item> items;


    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isOpen;


    @Getter @Setter
    public static class Item {
        @NotBlank private String question;
        @NotNull private String type;
        @NotNull private Boolean required;
        private List<String> options;
        private String description;
    }
}
