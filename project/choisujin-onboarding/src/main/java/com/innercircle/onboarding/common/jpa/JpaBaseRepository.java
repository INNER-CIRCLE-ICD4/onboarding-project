package com.innercircle.onboarding.common.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;


/**
 * org.springframework.data.jpa.repository
 * org.springframework.data.querydsl
 * JPA 기본 CRUD 와 QueryDSL 동적 쿼리 기능을 모두 사용가능.
 * Bean 으로 생성하지 않고, 상속받아 사용한다.
 */
@NoRepositoryBean
public interface JpaBaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, QuerydslPredicateExecutor<T> {
}

