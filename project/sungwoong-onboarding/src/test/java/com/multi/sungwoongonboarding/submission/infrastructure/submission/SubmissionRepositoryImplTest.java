package com.multi.sungwoongonboarding.submission.infrastructure.submission;

import com.multi.sungwoongonboarding.submission.application.repository.SubmissionRepository;
import com.multi.sungwoongonboarding.submission.domain.Answers;
import com.multi.sungwoongonboarding.submission.domain.SelectedOption;
import com.multi.sungwoongonboarding.submission.domain.Submission;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "/sql/form_insert_data.sql")
class SubmissionRepositoryImplTest {
    @Autowired
    SubmissionRepository submissionRepository;


    @Test
    @DisplayName("응답 제출 기능")
    public void submitSubmission() {

        //Given

        Answers answers = Answers.builder()
                .questionId(1L)
                .selectedOptions(List.of(
                        SelectedOption.builder()
                                .optionId(1L)
                                .build(),
                        SelectedOption.builder()
                                .optionId(1L)
                                .build(),
                        SelectedOption.builder()
                                .optionId(1L)
                                .build()))
                .build();


        Submission submission = Submission.builder()
                .formId(1L)
                .userId("sungwoong")
                .answers(List.of(answers))
                .build();

        //When
        Long save = submissionRepository.save(submission);

        //Given
        Assertions.assertNotNull(save);

    }
  
}