package com.multi.sungwoongonboarding.submission.infrastructure.submission;

import com.multi.sungwoongonboarding.submission.domain.Answers;
import com.multi.sungwoongonboarding.submission.domain.SelectedOption;
import com.multi.sungwoongonboarding.submission.domain.Submission;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
@Sql(scripts = "/sql/submission_insert_data.sql")
class SubmissionJpaRepositoryTest {

    @Autowired
    SubmissionJpaRepository submissionJpaRepository;

    @Autowired
    EntityManager entityManager;


    @Test
    @DisplayName("Submission :  Jpa 저장")
    public void jpa_저장() {
        //Given
        Answers answers = Answers.builder()
                .questionId(1L)
                .selectedOptions(List.of(
                        SelectedOption.builder()
                                .optionId(1L)
                                .build(),
                        SelectedOption.builder()
                                .optionId(2L)
                                .build(),
                        SelectedOption.builder()
                                .optionId(3L)
                                .build()))
                .build();
        Submission submission = Submission.builder()
                .formId(1L)
                .userId("sungwoong222")
                .answers(List.of(answers))
                .build();

        SubmissionJpaEntity submissionJpaEntity = SubmissionJpaEntity.fromDomainWithFormVersion(submission, 1);


        //When
        SubmissionJpaEntity save = submissionJpaRepository.save(submissionJpaEntity);

        entityManager.flush();
        entityManager.clear();

        List<SubmissionJpaEntity> byFormId = submissionJpaRepository.findByFormId(save.getId(), null, null);

        //Then
        assertThat(byFormId).isNotNull();
        assertThat(byFormId).hasSize(4);
        assertThat(byFormId.get(0).getAnswers()).hasSize(3);
        assertThat(byFormId.get(0).getAnswers().get(2).getOptionId()).isEqualTo(3L);
    }

    @Test
    @DisplayName("Submission : JPA 조회")
    public void jpa_조회하기() {

        //When
        List<SubmissionJpaEntity> all = submissionJpaRepository.findAll();

        //Then
        assertThat(all).isNotNull();
        assertThat(all).hasSize(3);
    }

    @Test
    @DisplayName("Submission : formId로 조회")
    public void findById() {

        //Given
        Long formId = 1L;

        //When
        List<SubmissionJpaEntity> byFormId = submissionJpaRepository.findByFormId(formId, null, null);

        //Then
        assertThat(byFormId).isNotNull();
        assertThat(byFormId).hasSize(3);
        assertThat(byFormId.get(0).getAnswers()).hasSize(1);
        assertThat(byFormId.get(1).getAnswers()).hasSize(1);
        assertThat(byFormId.get(2).getAnswers()).hasSize(3);
    }

}