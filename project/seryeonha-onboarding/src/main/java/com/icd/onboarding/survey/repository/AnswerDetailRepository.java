package com.icd.onboarding.survey.repository;

import com.icd.onboarding.survey.domain.AnswerDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerDetailRepository extends JpaRepository<AnswerDetail, Long> {
}
