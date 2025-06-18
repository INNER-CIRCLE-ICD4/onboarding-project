package com.multi.sungwoongonboarding.submission.application;

import com.multi.sungwoongonboarding.forms.infrastructure.FormJpaRepository;
import com.multi.sungwoongonboarding.forms.infrastructure.FormsJpaEntity;
import com.multi.sungwoongonboarding.questions.infrastructure.QuestionJpaEntity;
import com.multi.sungwoongonboarding.submission.dto.AnswerCreateRequest;
import com.multi.sungwoongonboarding.submission.dto.SubmissionCreateRequest;
import com.multi.sungwoongonboarding.submission.infrastructure.submission.SubmissionJpaEntity;
import com.multi.sungwoongonboarding.submission.infrastructure.submission.SubmissionJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "/sql/form_insert_data.sql")
@Transactional
class SubmissionServiceTest {

    @Autowired
    FormJpaRepository formJpaRepository;

    @Autowired
    SubmissionService submissionService;

    @Autowired
    private SubmissionJpaRepository submissionJpaRepository;


    @Test
    @DisplayName("sql 파일 테스트")
    public void sqlTest() {

        //Given
        Optional<FormsJpaEntity> byId = formJpaRepository.findById(1L);

        //Expected
        Assertions.assertThat(byId.isPresent()).isTrue();
        Assertions.assertThat(byId.get().getQuestions().size()).isEqualTo(3);
        Assertions.assertThat(byId.get().getQuestions().get(0).getOptions().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("ResponseService 응답지 생성 기능 - 성공")
    public void submitResponseTest() {
        // Given
        // 여기에 응답 제출을 위한 테스트 데이터를 준비합니다.
        FormsJpaEntity formsJpaEntity = formJpaRepository.findById(1L).orElseThrow(AssertionError::new);
        List<QuestionJpaEntity> questions = formsJpaEntity.getQuestions();

        SubmissionCreateRequest submissionCreateRequest = SubmissionCreateRequest.builder()
                .formId(formsJpaEntity.getId())
                .userId("123햅버거러버123")
                .answerCreateRequests(
                        List.of(
                                AnswerCreateRequest.builder()
                                        .questionId(questions.get(0).getId())
                                        .optionId(questions.get(0).getOptions().get(0).getId())
                                        .build(),
                                AnswerCreateRequest.builder()
                                        .questionId(questions.get(1).getId())
                                        .optionId(questions.get(1).getOptions().get(0).getId())
                                        .build(),
                                AnswerCreateRequest.builder()
                                        .questionId(questions.get(1).getId())
                                        .optionId(questions.get(1).getOptions().get(1).getId())
                                        .build(),
                                AnswerCreateRequest.builder()
                                        .questionId(3L)
                                        .answerText("햄버거는 다 맛있다.")
                                        .build()
                        )).build();

        // When
        // 여기에 응답 제출 메소드를 호출합니다.
        submissionService.submitResponse(submissionCreateRequest);
        Optional<SubmissionJpaEntity> byId = submissionJpaRepository.findById(1L);

        // Then
        // 여기에 응답 제출 결과를 검증하는 로직을 작성합니다.
        Assertions.assertThat(byId.isPresent()).isTrue();
        Assertions.assertThat(byId.get().getId()).isEqualTo(1L);
        Assertions.assertThat(byId.get().getAnswers().size()).isEqualTo(4L);
    }
}