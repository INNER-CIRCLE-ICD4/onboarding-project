package fastcampus.inguk_onboarding.form.post.domain;

import fastcampus.inguk_onboarding.form.post.domain.content.Content;
import fastcampus.inguk_onboarding.form.post.domain.content.SurveyContent;
import fastcampus.inguk_onboarding.form.post.repository.entity.post.SurveyEntity;
import fastcampus.inguk_onboarding.form.post.repository.entity.post.SurveyVersionEntity;
import fastcampus.inguk_onboarding.form.post.repository.entity.post.SurveyItemEntity;

public class Survey {
    
    private final SurveyContent surveyContent;
    
    public Survey(SurveyContent surveyContent) {
        this.surveyContent = surveyContent;
        validateSurveyContent();
    }
    
    private void validateSurveyContent() {
        if (!surveyContent.hasValidContentCount()) {
            throw new IllegalArgumentException("설문 받을 항목은 1개 ~ 10개까지 포함할 수 있습니다. 현재 항목 수: " + surveyContent.getContentCount());
        }
    }
    
    public SurveyEntity createSurvey() {
        // 설문조사 엔티티 생성
        SurveyEntity surveyEntity = new SurveyEntity(surveyContent.name(), surveyContent.description());
        
        // 버전 생성
        String versionCode = generateVersionCode(surveyEntity);
        SurveyVersionEntity versionEntity = new SurveyVersionEntity(surveyEntity, versionCode);
        surveyEntity.addVersion(versionEntity);
        
        // 각 Content를 SurveyItemEntity로 변환하여 추가
        for (Content content : surveyContent.contents()) {
            SurveyItemEntity itemEntity = createSurveyItemEntity(content);
            versionEntity.addItem(itemEntity);
        }
        
        return surveyEntity;
    }
    
    private String generateVersionCode(SurveyEntity surveyEntity) {
        int nextVersionNumber = surveyEntity.getVersions().size() + 1;
        return "v" + nextVersionNumber;
    }
    
    private SurveyItemEntity createSurveyItemEntity(Content content) {
        SurveyItemEntity itemEntity = new SurveyItemEntity(
                content.name(),
                content.description(),
                content.inputType(),
                content.required(),
                content.order()
        );
        
        if (content.hasOptions()) {
            itemEntity.setOptions(content.options());
        }
        
        return itemEntity;
    }
    
    public SurveyContent getSurveyContent() {
        return surveyContent;
    }
}
