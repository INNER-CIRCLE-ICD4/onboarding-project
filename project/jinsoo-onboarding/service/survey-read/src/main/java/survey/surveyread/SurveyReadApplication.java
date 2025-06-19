package survey.surveyread;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = "survey")
@EnableJpaRepositories(basePackages = "survey")
@SpringBootApplication
public class SurveyReadApplication {
    public static void main(String[] args) {
        SpringApplication.run(SurveyReadApplication.class, args);
    }
}
