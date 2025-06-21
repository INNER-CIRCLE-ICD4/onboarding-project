package com.survey.soyoung_onboarding.repository;

import com.survey.soyoung_onboarding.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReplyRepository extends JpaRepository<Reply, UUID> {
    List<Reply> findAllBySurveyId(UUID surveyId);
}
