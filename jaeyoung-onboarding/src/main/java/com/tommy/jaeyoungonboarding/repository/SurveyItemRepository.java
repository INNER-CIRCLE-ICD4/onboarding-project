package com.tommy.jaeyoungonboarding.repository;

import com.tommy.jaeyoungonboarding.entity.SurveyItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SurveyItemRepository extends JpaRepository<SurveyItem, UUID> {
}
