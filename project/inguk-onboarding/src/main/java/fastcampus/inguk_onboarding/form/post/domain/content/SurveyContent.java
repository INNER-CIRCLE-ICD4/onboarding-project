package fastcampus.inguk_onboarding.form.post.domain.content;

import java.util.List;

public record SurveyContent(
        String name,
        String description,
        List<Content> contents
) {
    public SurveyContent {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("설문조사 이름은 필수입니다.");
        }
        
        if (contents == null || contents.isEmpty()) {
            throw new IllegalArgumentException("설문 받을 항목은 필수입니다.");
        }
        
        if (contents.size() > 10) {
            throw new IllegalArgumentException("설문 받을 항목은 1개 ~ 10개까지 포함할 수 있습니다.");
        }
    }
    
    public int getContentCount() {
        return contents.size();
    }
    
    public boolean hasValidContentCount() {
        return contents.size() >= 1 && contents.size() <= 10;
    }
} 