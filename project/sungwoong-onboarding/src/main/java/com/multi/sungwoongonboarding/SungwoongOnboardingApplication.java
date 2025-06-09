package com.multi.sungwoongonboarding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SungwoongOnboardingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SungwoongOnboardingApplication.class, args);
    }

}
