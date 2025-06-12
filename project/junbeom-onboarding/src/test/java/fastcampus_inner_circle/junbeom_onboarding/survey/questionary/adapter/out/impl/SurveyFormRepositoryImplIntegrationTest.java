package fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.out.impl;

import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.out.entity.SurveyFormJpaEntity;
import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.out.repository.SurveyFormJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SurveyFormRepositoryImplIntegrationTest {

    @Autowired
    private SurveyFormJpaRepository surveyFormJpaRepository;

    @Test
    @DisplayName("SurveyFormJpaEntity 실제 DB 저장 및 조회 테스트")
    void saveAndFindById() {
        // given
        SurveyFormJpaEntity entity = SurveyFormJpaEntity.builder()
                .name("테스트 설문")
                .describe("테스트 설명")
                .createAt(LocalDateTime.now())
                .build();

        // when
        SurveyFormJpaEntity saved = surveyFormJpaRepository.save(entity);
        Optional<SurveyFormJpaEntity> found = surveyFormJpaRepository.findById(saved.getId());

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("테스트 설문");
        assertThat(found.get().getDescribe()).isEqualTo("테스트 설명");

    }
}
