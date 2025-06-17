package fastcampus.inguk_onboarding.form.post.domain.content;

import fastcampus.inguk_onboarding.form.post.domain.Surveys.InputType;
import lombok.Getter;

import java.util.List;

/**
 * 설문 관련 공통 컨텐츠의 기본 클래스
 * name과 description을 공통으로 가집니다.
 */
@Getter
public class Content {
    protected final String name;
    protected final String description;
    
    public Content(String name, String description) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("이름은 필수입니다.");
        }
        
        this.name = name;
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Content content = (Content) obj;
        return name.equals(content.name) && 
               (description != null ? description.equals(content.description) : content.description == null);
    }
    
    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return "Content{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
} 