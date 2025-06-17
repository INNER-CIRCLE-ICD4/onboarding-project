package fastcampus.inguk_onboarding.form.post.application.dto;

import fastcampus.inguk_onboarding.form.post.repository.entity.post.SurveyEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SurveyResponseDto() {}

    public SurveyResponseDto(SurveyEntity survey) {
        this.id = survey.getId();
        this.title = survey.getTitle();
        this.description = survey.getDescription();
        this.items = survey.getItems().stream()
                .map(SurveyItemResponse::new)
                .collect(Collectors.toList());
        this.createdAt = survey.getCreatedAt();
        this.updatedAt = survey.getUpdatedAt();
    }

}
