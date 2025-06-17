package fastcampus.inguk_onboarding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class IngukOnboardingApplication {

	public static void main(String[] args) {
		SpringApplication.run(IngukOnboardingApplication.class, args);
	}

}
