package com.innercircle.onboarding.changzune_onboarding.security;

import com.innercircle.onboarding.changzune_onboarding.user.User;
import com.innercircle.onboarding.changzune_onboarding.user.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

// Spring Security에서 사용하는 사용자 정보 구현 클래스
@Getter
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    // 사용자의 권한(Role) 정보를 Spring Security용 객체로 변환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<UserRole> roles = user.getRoles();
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());
    }

    // 비밀번호 반환
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // 아이디(username) 반환
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 계정 만료 여부 (true: 만료 안 됨)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠김 여부 (true: 안 잠김)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 자격 증명 만료 여부 (true: 안 만료)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 활성화 여부 (true: 활성화됨)
    @Override
    public boolean isEnabled() {
        return true;
    }

}