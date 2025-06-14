package com.INNER_CIRCLE_ICD4.innerCircle.repository;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.Response;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ResponseRepository extends JpaRepository<Response, UUID> {
    // ✅ 연관관계 필드 접근 시 언더스코어 사용!
    List<Response> findAllBySurvey_Id(UUID surveyId);
}
