package fastcampus.onboarding.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ServletComponentScan
@EnableJpaRepositories(basePackages = "fastcampus")
@EntityScan(basePackages = "fastcampus")
@ComponentScan(basePackages = "fastcampus")
@SpringBootApplication
public class HyeminOnboardingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HyeminOnboardingApplication.class, args);
	}

}
