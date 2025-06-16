package com.icd.onboarding.survey.repository;

import com.icd.onboarding.survey.domain.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
}
