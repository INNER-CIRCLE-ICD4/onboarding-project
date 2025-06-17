package fastcampus.inguk_onboarding.form.post.application.dto;

import java.util.List;

/**
 * 설문조사 생성 및 수정 요청의 공통 데이터를 정의하는 인터페이스
 */
public interface SurveyRequestDto {
    String name();
    String description(); 
    List<CreateSurveyItemRequestDto> items();
} 