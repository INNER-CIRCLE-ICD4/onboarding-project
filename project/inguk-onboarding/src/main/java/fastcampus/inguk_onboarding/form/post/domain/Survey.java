package fastcampus.inguk_onboarding.form.post.domain;

import fastcampus.inguk_onboarding.form.post.domain.content.SurveyContent;
import fastcampus.inguk_onboarding.form.post.domain.content.SurveyItem;
import fastcampus.inguk_onboarding.form.post.repository.entity.post.SurveyEntity;
import fastcampus.inguk_onboarding.form.post.repository.entity.post.SurveyVersionEntity;
import fastcampus.inguk_onboarding.form.post.repository.entity.post.SurveyItemEntity;

import java.util.List;

public class Survey {
    
    private final SurveyContent surveyContent;
    
    public Survey(SurveyContent surveyContent) {
        this.surveyContent = surveyContent;
        validateSurveyContent();
    }
    
    private void validateSurveyContent() {
        if (!surveyContent.hasValidItemCount()) {
            throw new IllegalArgumentException("설문 받을 항목은 1개 ~ 10개까지 포함할 수 있습니다. 현재 항목 수: " + surveyContent.getItemCount());
        }
    }



    public SurveyEntity createSurvey() {
        // 설문조사 엔티티 생성
        SurveyEntity surveyEntity = new SurveyEntity(surveyContent.getName(), surveyContent.getDescription());
        
        // 버전 생성
        String versionCode = generateVersionCode(surveyEntity);
        SurveyVersionEntity versionEntity = new SurveyVersionEntity(surveyEntity, versionCode);
        surveyEntity.addVersion(versionEntity);
        
        // 각 SurveyItem을 SurveyItemEntity로 변환하여 추가
        for (SurveyItem item : surveyContent.getItems()) {
            SurveyItemEntity itemEntity = createSurveyItemEntity(item);
            versionEntity.addItem(itemEntity);
        }
        
        return surveyEntity;
    }


    /**
     * 기존 설문조사를 업데이트합니다.
     * - 기본 정보만 변경된 경우: 기존 엔티티 업데이트 (새 버전 생성 X)
     * - 설문 항목이 변경된 경우: 새 버전 생성
     * 
     * @param existingSurvey 기존 설문조사 엔티티
     * @return 업데이트된 설문조사 엔티티
     */
    public SurveyEntity updateSurvey(SurveyEntity existingSurvey) {
        // SurveyContent에 위임하여 변경사항 확인
        boolean basicInfoChanged = surveyContent.hasBasicInfoChangedFrom(existingSurvey);
        boolean itemsChanged = surveyContent.hasItemsChangedFrom(existingSurvey);
        
        if (basicInfoChanged) {
            // 기본 정보 업데이트
            updateBasicInfo(existingSurvey);
        }
        
        if (itemsChanged) {
            // 새 버전 생성
            createNewVersion(existingSurvey);
        }
        
        return existingSurvey;
    }
    

    
    /**
     * 기본 정보 업데이트
     */
    private void updateBasicInfo(SurveyEntity existingSurvey) {
        existingSurvey.setTitle(surveyContent.getName());
        existingSurvey.setDescription(surveyContent.getDescription());
    }
    
    /**
     * 새 버전 생성
     */
    private void createNewVersion(SurveyEntity existingSurvey) {
        String newVersionCode = generateVersionCode(existingSurvey);
        SurveyVersionEntity newVersion = new SurveyVersionEntity(existingSurvey, newVersionCode);
        existingSurvey.addVersion(newVersion);
        
        // 새 항목들 추가
        for (SurveyItem item : surveyContent.getItems()) {
            SurveyItemEntity itemEntity = createSurveyItemEntity(item);
            newVersion.addItem(itemEntity);
        }
    }




    private String generateVersionCode(SurveyEntity surveyEntity) {
        int nextVersionNumber = surveyEntity.getVersions().size() + 1;
        return "v" + nextVersionNumber;
    }
    
    private SurveyItemEntity createSurveyItemEntity(SurveyItem item) {
        SurveyItemEntity itemEntity = new SurveyItemEntity(
                item.getName(),
                item.getDescription(),
                item.getInputType(),
                item.getRequired(),
                item.getOrder()
        );
        
        if (item.hasOptions()) {
            itemEntity.setOptions(item.getOptions());
        }
        
        return itemEntity;
    }
    
    public SurveyContent getSurveyContent() {
        return surveyContent;
    }
}
