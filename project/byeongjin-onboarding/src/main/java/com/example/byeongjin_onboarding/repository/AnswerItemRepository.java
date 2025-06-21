package com.example.byeongjin_onboarding.repository;

import com.example.byeongjin_onboarding.entity.AnswerItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerItemRepository extends JpaRepository<AnswerItem, Long> {
}