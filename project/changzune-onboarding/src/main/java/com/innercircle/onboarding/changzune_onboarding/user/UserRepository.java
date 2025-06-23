package com.innercircle.onboarding.changzune_onboarding.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // username으로 사용자 정보를 가져오는 메서드
    Optional<User> findByUsername(String username);
}