package com.multi.sungwoongonboarding.submission.domain;

import com.multi.sungwoongonboarding.forms.domain.Forms;
import com.multi.sungwoongonboarding.questions.domain.Questions;
import com.multi.sungwoongonboarding.submission.dto.AnswerCreateRequest;
import com.multi.sungwoongonboarding.submission.dto.AnswerResponse;
import com.multi.sungwoongonboarding.submission.dto.SubmissionCreateRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static com.multi.sungwoongonboarding.questions.domain.Questions.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


public class SubmissionTest {

    @Test
    @DisplayName("Submission 도메인 - CreateRequest > Domain 변경 성공")
    public void request_to_domain() {

        //Given
        List<AnswerCreateRequest> 답변_text = List.of(
                AnswerCreateRequest.builder().questionId(1L).optionId(null).answerText("답변_text").build(),
                AnswerCreateRequest.builder().questionId(2L).optionId(1L).build(),
                AnswerCreateRequest.builder().questionId(3L).optionId(2L).build(),
                AnswerCreateRequest.builder().questionId(3L).optionId(3L).build()
        );

        SubmissionCreateRequest 제출지_요청_데이터 = SubmissionCreateRequest.builder()
                .formId(1L)
                .answerCreateRequests(답변_text)
                .userId("노성웅")
                .build();
        //When
        Submission 제출지_도메인 = 제출지_요청_데이터.toDomain();

        //Then
        assertThat(제출지_도메인).isNotNull();
        assertThat(제출지_도메인.getAnswers().size()).isEqualTo(4);
        assertThat(제출지_도메인.getFormId()).isEqualTo(1L);
        assertThat(제출지_도메인.getUserId()).isEqualTo("노성웅");
        assertThat(제출지_도메인.getAnswers().get(0).getQuestionId()).isEqualTo(1L);
        assertThat(제출지_도메인.getAnswers().get(0).getAnswerText()).isEqualTo("답변_text");

        assertThat(제출지_도메인.getAnswers().get(1).getQuestionId()).isEqualTo(2L);
        assertThat(제출지_도메인.getAnswers().get(1).getAnswerText()).isNull();

        assertThat(제출지_도메인.getAnswers().get(2).getQuestionId()).isEqualTo(3L);
        assertThat(제출지_도메인.getAnswers().get(2).getAnswerText()).isNull();

        assertThat(제출지_도메인.getAnswers().get(3).getQuestionId()).isEqualTo(3L);
        assertThat(제출지_도메인.getAnswers().get(3).getAnswerText()).isNull();
    }

    @Test
    @DisplayName("Submission 도메인 - 도메인 > 응답 객체")
    public void domain_to_response() {

        //Given
        LocalDateTime now = LocalDateTime.now();

        List<Questions> 질문_목록 = List.of(
                builder()
                        .id(1L)
                        .questionText("질문_1")
                        .questionType(QuestionType.SHORT_ANSWER)
                        .isRequired(true)
                        .deleted(false)
                        .build(),
                builder()
                        .id(2L)
                        .questionText("질문_2")
                        .questionType(QuestionType.SINGLE_CHOICE)
                        .isRequired(true)
                        .deleted(false)
                        .build()
        );

        Forms.builder()
                .title("설문지_1")
                .description("설문을 조사한다.")
                .questions(질문_목록)
                .createdAt(now)
                .version(1)
                .build();

        List<Answers> 답변_목록 = List.of(
                Answers.builder()
                        .questionId(1L)
                        .answerText("답변_1")
                        .build(),
                Answers.builder()
                        .questionId(2L)
                        .optionId(1L)
                        .build()
        );

        Submission.builder()
                .formId(1L)
                .formVersion(1)
                .formTitle("질문 제목_v1")
                .formDescription("질문 설명_v1")
                .answers(답변_목록)
                .build();

        //When

        //Then

    }

}