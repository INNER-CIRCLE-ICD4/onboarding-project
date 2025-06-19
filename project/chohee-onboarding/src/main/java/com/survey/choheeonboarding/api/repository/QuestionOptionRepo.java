package com.survey.choheeonboarding.api.repository;

import com.survey.choheeonboarding.api.entity.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionOptionRepo extends JpaRepository<QuestionOption, String> {

}
