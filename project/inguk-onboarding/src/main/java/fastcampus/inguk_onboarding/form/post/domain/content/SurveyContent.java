package fastcampus.inguk_onboarding.form.post.domain.content;

import fastcampus.inguk_onboarding.form.post.repository.entity.post.SurveyEntity;
import fastcampus.inguk_onboarding.form.post.repository.entity.post.SurveyVersionEntity;
import fastcampus.inguk_onboarding.form.post.repository.entity.post.SurveyItemEntity;
import lombok.Getter;

import java.util.List;

/**
 * 전체 설문조사를 나타내는 클래스
 * Content를 상속받아 설문조사의 기본 정보와 설문 항목들을 가집니다.
 */
@Getter
public class SurveyContent extends Content {
    private final List<SurveyItem> items;
    
    public SurveyContent(String name, String description, List<SurveyItem> items) {
        super(name, description);
        
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("설문 받을 항목은 필수입니다.");
        }
        
        if (items.size() > 10) {
            throw new IllegalArgumentException("설문 받을 항목은 1개 ~ 10개까지 포함할 수 있습니다.");
        }
        
        this.items = items;
    }

    public int getItemCount() {
        return items.size();
    }
    
    public boolean hasValidItemCount() {
        return items.size() >= 1 && items.size() <= 10;
    }
    
    /**
     * 기존 설문조사의 기본 정보(이름, 설명)와 다른지 확인
     */
    public boolean hasBasicInfoChangedFrom(SurveyEntity existingSurvey) {
        return !name.equals(existingSurvey.getTitle()) ||
               !description.equals(existingSurvey.getDescription());
    }
    
    /**
     * 기존 설문조사의 항목들과 다른지 확인
     */
    public boolean hasItemsChangedFrom(SurveyEntity existingSurvey) {
        // 기존 버전이 없으면 새로 생성
        if (existingSurvey.getVersions().isEmpty()) {
            return true;
        }
        
        SurveyVersionEntity latestVersion = getLatestVersion(existingSurvey);
        List<SurveyItemEntity> existingItems = latestVersion.getItems();
        
        // 항목 수가 다른 경우
        if (existingItems.size() != items.size()) {
            return true;
        }
        
        // 각 항목의 내용 비교
        for (int i = 0; i < existingItems.size(); i++) {
            SurveyItemEntity existingItem = existingItems.get(i);
            SurveyItem newItem = items.get(i);
            
            if (newItem.isDifferentFrom(
                    existingItem.getTitle(),
                    existingItem.getDescription(), 
                    existingItem.getInputType(),
                    existingItem.getRequired(),
                    existingItem.getOrder(),
                    existingItem.getOptions())) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 최신 버전 조회
     */
    private SurveyVersionEntity getLatestVersion(SurveyEntity surveyEntity) {
        return surveyEntity.getVersions().get(surveyEntity.getVersions().size() - 1);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        if (!(obj instanceof SurveyContent)) return false;
        
        SurveyContent that = (SurveyContent) obj;
        return items.equals(that.items);
    }
    
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + items.hashCode();
        return result;
    }
    
    @Override
    public String toString() {
        return "SurveyContent{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", items=" + items +
                '}';
    }
} 