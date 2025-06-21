package com.example.byeongjin_onboarding.repository;

import com.example.byeongjin_onboarding.entity.FormItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormItemRepository extends JpaRepository<FormItem, Long> {
}