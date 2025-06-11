package com.example.demo.survey.Repository;

import com.example.demo.survey.Dto.CreateSurveyDTO;
import com.example.demo.survey.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {
}
