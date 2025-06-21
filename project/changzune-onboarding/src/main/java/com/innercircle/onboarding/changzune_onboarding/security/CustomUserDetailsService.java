package com.innercircle.onboarding.changzune_onboarding.security;

import com.innercircle.onboarding.changzune_onboarding.user.User;
import com.innercircle.onboarding.changzune_onboarding.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // Spring Security가 로그인할 때 이 메서드가 호출됨
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 사용자 이름(username)으로 DB에서 사용자 정보를 찾음
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

        // 찾은 사용자 정보를 CustomUserDetails로 감싸서 리턴
        return new CustomUserDetails(user);
    }
}