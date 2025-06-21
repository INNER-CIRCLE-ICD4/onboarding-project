package fastcampus.inguk_onboarding.form.post.application.dto;

import fastcampus.inguk_onboarding.form.post.repository.entity.post.SurveyEntity;
import fastcampus.inguk_onboarding.form.post.repository.entity.post.SurveyVersionEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
        
        log.info("=== SurveyResponseDto 생성 시작 ===");
        log.info("Survey ID: {}, Title: {}", survey.getId(), survey.getTitle());
        log.info("전체 버전 수: {}", survey.getVersions().size());
        
        // 각 버전 정보 로깅
        for (int i = 0; i < survey.getVersions().size(); i++) {
            SurveyVersionEntity version = survey.getVersions().get(i);
            log.info("버전 [{}]: ID={}, Code={}, 항목 수={}", 
                i, version.getId(), version.getVersionCode(), version.getItems().size());
        }
        
        // 최신 버전의 항목들을 가져오기 (마지막 버전)
        if (survey.getVersions().isEmpty()) {
            this.items = new ArrayList<>();
            log.warn("버전이 없음 - 빈 항목 리스트 생성");
        } else {
            SurveyVersionEntity latestVersion = getLatestVersion(survey);
            log.info("최신 버전 선택: ID={}, Code={}, 항목 수={}", 
                latestVersion.getId(), latestVersion.getVersionCode(), latestVersion.getItems().size());
            
            // 각 항목 상세 로깅
            for (int i = 0; i < latestVersion.getItems().size(); i++) {
                var item = latestVersion.getItems().get(i);
                log.info("항목 [{}]: ID={}, Title={}, Order={}", 
                    i, item.getId(), item.getTitle(), item.getOrder());
            }
            
            this.items = latestVersion.getItems().stream()
                    .map(SurveyItemResponse::new)
                    .collect(Collectors.toList());
            
            log.info("최종 생성된 항목 수: {}", this.items.size());
        }
                        
        this.versions = survey.getVersions().stream()
                .map(SurveyVersionResponseDto::new)
                .collect(Collectors.toList());
        this.createdAt = survey.getCreatedAt();
        this.updatedAt = survey.getUpdatedAt();
        
        log.info("=== SurveyResponseDto 생성 완료 ===");
    }
    
    /**
     * 최신 버전을 가져옵니다
     */
    private SurveyVersionEntity getLatestVersion(SurveyEntity survey) {
        List<SurveyVersionEntity> versions = survey.getVersions();
        return versions.get(versions.size() - 1);
    }
}
