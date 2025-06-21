package com.innercircle.onboarding.changzune_onboarding.user;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/**
 * 사용자 정보를 담는 User 엔티티 클래스입니다.
 * 이 클래스는 Spring Security와 연동하여 인증/인가에 사용됩니다.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 사용자 고유 ID (자동 생성)

    @Column(nullable = false, unique = true)
    private String username; // 사용자 로그인 ID (중복 불가)

    @Column(nullable = false)
    private String password; // 암호화된 비밀번호

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles; // 사용자의 권한 목록 (예: ROLE_ADMIN, ROLE_USER)
}