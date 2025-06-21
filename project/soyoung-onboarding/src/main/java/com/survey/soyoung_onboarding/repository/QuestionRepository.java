package com.survey.soyoung_onboarding.repository;

import com.survey.soyoung_onboarding.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question,Long> {

}
