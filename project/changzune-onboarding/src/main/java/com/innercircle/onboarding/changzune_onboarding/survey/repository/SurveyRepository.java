package com.innercircle.onboarding.changzune_onboarding.survey.repository;

import com.innercircle.onboarding.changzune_onboarding.survey.domain.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
    // 필요 시 사용자 정의 쿼리 추가 가능
}

