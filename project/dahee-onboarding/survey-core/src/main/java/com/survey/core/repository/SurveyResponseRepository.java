package com.survey.core.repository;

import com.survey.core.entity.SurveyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SurveyResponseRepository extends JpaRepository<SurveyResponse, Long> {
    // 중복 응답 체크용 메서드
    boolean existsBySurveyIdAndUuid(Long surveyId, String uuid);

    Page<SurveyResponse> findBySurveyId(Long surveyId, Pageable pageable);

}
