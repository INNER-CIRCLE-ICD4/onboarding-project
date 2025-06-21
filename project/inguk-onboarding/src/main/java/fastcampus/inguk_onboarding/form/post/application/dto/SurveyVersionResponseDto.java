package fastcampus.inguk_onboarding.form.post.application.dto;

import fastcampus.inguk_onboarding.form.post.repository.entity.post.SurveyVersionEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class SurveyVersionResponseDto {

    private Long id;
    private String versionCode;
    private LocalDateTime createdAt;
    

    public SurveyVersionResponseDto() {}

    public SurveyVersionResponseDto(SurveyVersionEntity version) {
        this.id = version.getId();
        this.versionCode = version.getVersionCode();
        this.createdAt = version.getCreatedAt();
    }
} 