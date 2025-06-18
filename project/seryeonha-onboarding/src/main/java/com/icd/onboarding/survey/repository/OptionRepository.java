package com.icd.onboarding.survey.repository;

import com.icd.onboarding.survey.domain.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findAllByQuestionIdIn(List<Long> questionIds);
}
