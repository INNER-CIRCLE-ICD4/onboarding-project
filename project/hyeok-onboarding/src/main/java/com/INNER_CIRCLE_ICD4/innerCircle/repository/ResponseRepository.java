package com.INNER_CIRCLE_ICD4.innerCircle.repository;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ResponseRepository extends JpaRepository<Response, UUID> {}
