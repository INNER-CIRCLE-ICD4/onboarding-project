package fastcampus.inguk_onboarding.form.post.application.dto;

import fastcampus.inguk_onboarding.form.post.repository.entity.post.SurveyEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
public class SurveyResponseDto {

    private Long id;
    private String title;
    private String description;
    private List<SurveyItemResponse> items;
    private List<SurveyVersionResponseDto> versions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SurveyResponseDto() {}

    public SurveyResponseDto(SurveyEntity survey) {
        this.id = survey.getId();
        this.title = survey.getTitle();
        this.description = survey.getDescription();
        // 최신 버전의 항목들을 가져오기 (첫 번째 버전 또는 마지막 버전)
        this.items = survey.getVersions().isEmpty() ? 
                new ArrayList<>() :
                survey.getVersions().get(0).getItems().stream()
                        .map(SurveyItemResponse::new)
                        .collect(Collectors.toList());
        this.versions = survey.getVersions().stream()
                .map(SurveyVersionResponseDto::new)
                .collect(Collectors.toList());
        this.createdAt = survey.getCreatedAt();
        this.updatedAt = survey.getUpdatedAt();
    }

}
