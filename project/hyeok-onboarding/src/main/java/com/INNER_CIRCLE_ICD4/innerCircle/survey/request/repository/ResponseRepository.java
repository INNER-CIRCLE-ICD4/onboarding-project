package com.INNER_CIRCLE_ICD4.innerCircle.survey.request.repository;

import com.INNER_CIRCLE_ICD4.innerCircle.survey.request.domain.Response;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponseRepository extends JpaRepository<Response, Long> {
}