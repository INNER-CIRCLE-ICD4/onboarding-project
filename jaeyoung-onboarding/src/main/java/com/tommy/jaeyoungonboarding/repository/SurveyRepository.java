package com.tommy.jaeyoungonboarding.repository;

import com.tommy.jaeyoungonboarding.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SurveyRepository extends JpaRepository<Survey, UUID> {
}
