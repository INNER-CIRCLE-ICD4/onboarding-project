package com.multi.sungwoongonboarding.submission.infrastructure.submission;

import com.multi.sungwoongonboarding.submission.domain.Answers;
import com.multi.sungwoongonboarding.submission.domain.SelectedOption;
import com.multi.sungwoongonboarding.submission.domain.Submission;
import com.multi.sungwoongonboarding.submission.infrastructure.answers.AnswerJpaEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
@Sql(scripts = "/sql/form_insert_data.sql")
class SubmissionJpaRepositoryTest {

    @Autowired
    SubmissionJpaRepository submissionJpaRepository;


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
                .userId("sungwoong")
                .answers(List.of(answers))
                .build();

        SubmissionJpaEntity submissionJpaEntity = SubmissionJpaEntity.fromDomainWithFormVersion(submission, 1);


        //When
        SubmissionJpaEntity save = submissionJpaRepository.save(submissionJpaEntity);
        List<SubmissionJpaEntity> byFormId = submissionJpaRepository.findByFormId(save.getId());

        //Then
        assertThat(byFormId).isNotNull();
        assertThat(byFormId).hasSize(1);
        assertThat(byFormId.get(0).getAnswers()).hasSize(3);
        assertThat(byFormId.get(0).getAnswers().get(2).getOptionId()).isEqualTo(3L);
    }

    @Test
    @DisplayName("Submission : Jpa 조회 기능")
    public void jpa_조회() {

        //Given
        List<Submission> submissions = Arrays.asList(
                Submission.builder()
                        .formId(1L)
                        .userId("sungwoong")
                        .answers(List.of(
                                Answers.builder()
                                        .questionId(1L)
                                        .selectedOptions(List.of(SelectedOption.builder().optionId(1L).build()))
                                        .build(),
                                Answers.builder()
                                        .questionId(2L)
                                        .selectedOptions(List.of(
                                                SelectedOption.builder().optionId(3L).build(),
                                                SelectedOption.builder().optionId(4L).build(),
                                                SelectedOption.builder().optionId(5L).build()
                                        )).build()
                        )).build(),

                Submission.builder()
                        .formId(2L)
                        .userId("sungwoong")
                        .answers(List.of(
                                Answers.builder()
                                        .questionId(1L)
                                        .selectedOptions(List.of(SelectedOption.builder().optionId(1L).build())).build(),
                                Answers.builder()
                                        .questionId(2L)
                                        .selectedOptions(List.of(
                                                SelectedOption.builder().optionId(3L).build(),
                                                SelectedOption.builder().optionId(4L).build(),
                                                SelectedOption.builder().optionId(5L).build()
                                        )).build()
                        )).build()
        );

        //When
        List<SubmissionJpaEntity> submissionJpaEntities = submissionJpaRepository.saveAll(submissions.stream().map(submission -> SubmissionJpaEntity.fromDomainWithFormVersion(submission, 1)).toList());
        List<SubmissionJpaEntity> byFormId = submissionJpaRepository.findByFormId(1L);

        //Then
        assertThat(submissionJpaEntities).isNotNull();
        assertThat(submissionJpaEntities).hasSize(2);
        assertThat(byFormId).isNotNull();
        assertThat(byFormId).hasSize(1);
        assertThat(byFormId.get(0).getAnswers()).isNotNull();
        assertThat(byFormId.get(0).getAnswers()).hasSize(4);
        assertThat(byFormId.get(0).getAnswers().get(0).getOptionId()).isEqualTo(1L);
    }

}