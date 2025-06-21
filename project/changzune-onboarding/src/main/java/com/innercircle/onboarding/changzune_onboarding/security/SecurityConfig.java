package com.innercircle.onboarding.changzune_onboarding.security;

import com.innercircle.onboarding.changzune_onboarding.security.CustomUserDetailsService;
import com.innercircle.onboarding.changzune_onboarding.user.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Service;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                // CSRF 비활성화 (테스트용이니 실서비스에서는 주의)
                .authorizeHttpRequests(auth -> auth
                        //이게 기존 어너테이션은 한두개이고 정의하면 여기 에 넣는게 더좋음
                        //  @PreAuthorize("hasRole('ADMIN')")@PreAuthorize("hasRole('ADMIN')")
                        //  설문 생성, 수정, 삭제는 관리자만 가능
                        .requestMatchers(HttpMethod.POST, "/api/surveys").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/surveys/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/surveys/**").hasRole("ADMIN")

                        // 설문 조회 및 응답 제출은 누구나 접근 가능
                        .requestMatchers(HttpMethod.GET, "/api/surveys/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/answers").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/answers/**").permitAll()

                        .anyRequest().authenticated() // 나머지는 인증 필요
                )
                .userDetailsService(userDetailsService)
                .formLogin(login -> login
                        .defaultSuccessUrl("/swagger-ui.html", true)
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 암호화
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}