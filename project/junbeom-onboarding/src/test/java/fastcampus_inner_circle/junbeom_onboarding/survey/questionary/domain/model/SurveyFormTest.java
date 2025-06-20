package fastcampus_inner_circle.junbeom_onboarding.survey.questionary.domain.model;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

@SpringBootTest
@Transactional
class SurveyFormTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    void testSurveyFormInsert() {
        // given
        SurveyForm surveyForm = SurveyForm.builder()
                .name("테스트 설문지")
                .describe("테스트용 설문지입니다.")
                .createAt(LocalDateTime.now())
                .build();

        // when
        entityManager.persist(surveyForm);
        entityManager.flush();
        entityManager.clear();

        // then
        List<SurveyForm> foundForms = entityManager.createQuery("SELECT sf FROM SurveyForm sf", SurveyForm.class)
                .getResultList();
        
        assertThat(foundForms).hasSize(1);
        assertThat(foundForms.get(0).getName()).isEqualTo("테스트 설문지");
        assertThat(foundForms.get(0).getDescribe()).isEqualTo("테스트용 설문지입니다.");
    }
} 