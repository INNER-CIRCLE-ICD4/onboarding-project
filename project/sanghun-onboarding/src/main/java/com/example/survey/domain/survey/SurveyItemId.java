package com.example.survey.domain.survey;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import lombok.NoArgsConstructor;


@NoArgsConstructor	// 기본생성자 생성
@Embeddable		/* JPA Entity 클래스에 컬럼으로 사용되기 위한 어노테이션 */
public class SurveyItemId implements Serializable{	// SurveyItem 복합키 클래스
	
	/* JPA 복합키 생성시 
	 * 
	 * 복합키 클래스는 반드시 public, Serializable을 구현하고, 
	 * 기본 생성자와 equals/hashCode를 오버라이드
	 * 
	 * 이유 : JPA 복합키 객체는 캐시키로 사용가능
	 * 영속성 컨텍스트 안에서 Map등 자료구조의 key로 저장 가능
	 * 
	 * -> 직렬화 가능한 키 객체 요구 -> Serializable 클래서 구현
	 * 
	 * 
	 * Entity 클래스에서 복합키 사용 변수에 @EmbeddedId 붙이면 끝
	 * */
	
	private Long surveyItemId;	// 설문조사 항목 아이디
	private int surveyItemSn;	// 설문조사 항목 순번(이력))
	
	// equals & hashCode는 필수
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SurveyItemId)) return false;
        SurveyItemId that = (SurveyItemId) o;
        return surveyItemSn == that.surveyItemSn &&
                Objects.equals(surveyItemId, that.surveyItemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surveyItemId, surveyItemSn);
    }
	
}
