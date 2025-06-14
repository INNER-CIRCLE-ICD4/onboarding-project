package kr.co.fastcampus.onboarding.hyeongminonboarding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
//@EnableJpaRepositories(basePackages = "kr.co.fastcampus.onboarding.hyeongminonboarding.domain.repository")
public class HyeongminOnboardingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HyeongminOnboardingApplication.class, args);
    }
}
