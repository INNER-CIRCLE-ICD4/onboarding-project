package fastcampus.onboarding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages="fastcampus")
@EnableJpaRepositories(basePackages = "fastcampus")  // 추가
@EntityScan(basePackages = "fastcampus")  // 추가
public class HyeminOnboardingApplication {
	public static void main(String[] args) {
		SpringApplication.run(HyeminOnboardingApplication.class, args);
	}
}
