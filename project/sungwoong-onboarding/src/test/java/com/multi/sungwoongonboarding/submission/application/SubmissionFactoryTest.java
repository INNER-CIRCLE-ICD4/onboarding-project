package com.multi.sungwoongonboarding.submission.application;

import com.multi.sungwoongonboarding.options.domain.Options;
import com.multi.sungwoongonboarding.questions.application.repository.QuestionRepository;
import com.multi.sungwoongonboarding.questions.domain.Questions;
import com.multi.sungwoongonboarding.submission.domain.Submission;
import com.multi.sungwoongonboarding.submission.dto.AnswerCreateRequest;
import com.multi.sungwoongonboarding.submission.dto.SubmissionCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static com.multi.sungwoongonboarding.questions.domain.Questions.QuestionType.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubmissionFactoryTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private SubmissionFactory submissionFactory;

    private Questions 단일_선택_질문(Long id, boolean required) {

        List<Options> options = Arrays.asList(
                Options.builder().id(1L).optionText("옵션1").build(),
                Options.builder().id(2L).optionText("옵션1").build()
        );

        return Questions.builder()
                .id(id)
                .questionText("단일 선택 질문")
                .questionType(SINGLE_CHOICE)
                .isRequired(required)
                .options(options)
                .build();
    }

    private Questions 다중_선택_질문(Long id, boolean required) {
        List<Options> options = Arrays.asList(
                Options.builder().id(3L).optionText("옵션1").build(),
                Options.builder().id(4L).optionText("옵션1").build(),
                Options.builder().id(5L).optionText("옵션1").build()
        );

        return Questions.builder()
                .id(id)
                .questionText("복수 선택 질문")
                .questionType(MULTIPLE_CHOICE)
                .isRequired(required)
                .options(options)
                .build();
    }

    private Questions 텍스트_질문(Long id, boolean required) {
        return Questions.builder()
                .id(id)
                .questionText("텍스트 질문")
                .questionType(SHORT_ANSWER)
                .isRequired(required)
                .options(Collections.emptyList())
                .build();
    }


    @Test
    @DisplayName("Submission : 정상적인 제출 - 단일선택, 다중선택, 텍스트 모두 포함")
    public void createSubmission_성공_모든_타입() {
        //Given
        Questions 단일_선택_질문 = 단일_선택_질문(1L, true);
        Questions 다중_선택_질문 = 다중_선택_질문(2L, true);
        Questions 텍스트_질문 = 텍스트_질문(3L, true);


        Map<Long, Questions> questionMap = Map.of(
                1L, 단일_선택_질문,
                2L, 다중_선택_질문,
                3L, 텍스트_질문
        );

        when(questionRepository.getQuestionMapByFormId(1L, null)).thenReturn(questionMap);
        SubmissionCreateRequest request = SubmissionCreateRequest.builder()
                .formId(1L)
                .userId("testUser")
                .answerCreateRequests(Arrays.asList(
                        AnswerCreateRequest.builder()
                                .questionId(1L)
                                .optionIds(Arrays.asList(1L))  // 단일 선택
                                .build(),
                        AnswerCreateRequest.builder()
                                .questionId(2L)
                                .optionIds(Arrays.asList(3L, 4L))  // 복수 선택
                                .build(),
                        AnswerCreateRequest.builder()
                                .questionId(3L)
                                .answerText("텍스트 답변")  // 텍스트
                                .build()
                ))
                .build();


        //When
        Submission submission = submissionFactory.createSubmission(request);

        //Then
        assertThat(submission).isNotNull();
        assertThat(submission.getFormId()).isEqualTo(1L);
        assertThat(submission.getUserId()).isEqualTo("testUser");
    }

    @Test
    @DisplayName("Submission : 검증 실패 -> 존재하지 않는 질문")
    public void createSubmission_실패_존재하지않는질문() {
        //Given
        Map<Long, Questions> questionMap = Map.of(
                1L, 단일_선택_질문(1L, true)
        );

        when(questionRepository.getQuestionMapByFormId(1L, null)).thenReturn(questionMap);

        SubmissionCreateRequest request = SubmissionCreateRequest.builder()
                .formId(1L)
                .userId("testUser")
                .answerCreateRequests(Arrays.asList(
                        AnswerCreateRequest.builder()
                                .questionId(999L)
                                .optionIds(Arrays.asList(1L))
                                .build()
                )).build();

        //When & Then
        assertThatThrownBy(() -> submissionFactory.createSubmission(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 설문지에 없는 질문들입니다 ");
    }

    @Test
    @DisplayName("Submission : 검증 실패 -> 중복 답변 ")
    public void createSubmission_실패_중복답변() {
        //Given
        Map<Long, Questions> questionMap = Map.of(
                1L, 단일_선택_질문(1L, true)
        );

        when(questionRepository.getQuestionMapByFormId(1L, null)).thenReturn(questionMap);

        SubmissionCreateRequest request = SubmissionCreateRequest.builder()
                .formId(1L)
                .userId("testUser")
                .answerCreateRequests(Arrays.asList(
                        AnswerCreateRequest.builder()
                                .questionId(1L)
                                .optionIds(Arrays.asList(1L))
                                .build(),
                        AnswerCreateRequest.builder()
                                .questionId(1L)
                                .optionIds(Arrays.asList(2L))
                                .build()
                )).build();

        //When & Then
        assertThatThrownBy(() -> submissionFactory.createSubmission(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("같은 질문에 여러번 답할 수 없습니다.");
    }

    @Test
    @DisplayName("Submission : 검증 실패 -> 중복 옵션 정보")
    public void createSubmission_실패_중복옵션정보() {
        //Given
        Map<Long, Questions> questionMap = Map.of(
                1L, 단일_선택_질문(1L, true)
        );

        when(questionRepository.getQuestionMapByFormId(1L, null)).thenReturn(questionMap);

        SubmissionCreateRequest request = SubmissionCreateRequest.builder()
                .formId(1L)
                .userId("testUser")
                .answerCreateRequests(Arrays.asList(
                        AnswerCreateRequest.builder()
                                .questionId(1L)
                                .optionIds(Arrays.asList(2L, 2L))
                                .build()
                )).build();

        //When
        assertThatThrownBy(() -> submissionFactory.createSubmission(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("에서 중복된 옵션");
    }

    @Test
    @DisplayName("Answer : 검증 실패 -> 질문 타입별 데이터 검증 -> 1. 선택형 질문에는 옵션데이터만 있어야 한다.")
    public void createSubmission_실패_선택형질문에는_옵션데이터만있어야한다() {
        //Given
        //Given
        Map<Long, Questions> questionMap = Map.of(
                1L, 단일_선택_질문(1L, true),
                2L, 다중_선택_질문(2L, true)
        );

        when(questionRepository.getQuestionMapByFormId(1L, null)).thenReturn(questionMap);

        SubmissionCreateRequest request = SubmissionCreateRequest.builder()
                .formId(1L)
                .userId("testUser")
                .answerCreateRequests(Arrays.asList(
                        AnswerCreateRequest.builder()
                                .questionId(1L)
                                .optionIds(Arrays.asList(2L))
                                .answerText("텍스트답변")
                                .build()
                )).build();

        //When
        assertThatThrownBy(() -> submissionFactory.createSubmission(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("선택형 질문에는 텍스트 답변을 할 수 없습니다.");
    }

    @Test
    @DisplayName("Answer : 검증 실패 -> 질문 타입별 데이터 검증 -> 2. 선택형 질문 && 필수 질문은 선택한 옵션이 있어야 한다")
    public void createSubmission_실패_선택형_필수는_옵션이있어야한다() {
        //Given
        Map<Long, Questions> questionMap = Map.of(
                1L, 다중_선택_질문(1L, true)
        );

        when(questionRepository.getQuestionMapByFormId(1L, null)).thenReturn(questionMap);

        SubmissionCreateRequest request = SubmissionCreateRequest.builder()
                .formId(1L)
                .userId("testUser")
                .answerCreateRequests(Arrays.asList(
                        AnswerCreateRequest.builder()
                                .questionId(1L)
                                .optionIds(Arrays.asList())
                                .build()
                )).build();

        //When
        assertThatThrownBy(() -> submissionFactory.createSubmission(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("필수 선택형 질문은 옵션을 선택해야 합니다.");
    }

    @Test
    @DisplayName("Answer : 검증 실패 -> 질문 타입별 데이터 검증 -> 3. 텍스트 질문에는 옵션이있으면 안된다.")
    public void createSubmission_실패_텍스트질문에는_옵션이없어야한다() {
        //Given
        Map<Long, Questions> questionMap = Map.of(
                1L, 텍스트_질문(1L, true)
        );

        when(questionRepository.getQuestionMapByFormId(1L, null)).thenReturn(questionMap);

        SubmissionCreateRequest request = SubmissionCreateRequest.builder()
                .formId(1L)
                .userId("testUser")
                .answerCreateRequests(Arrays.asList(
                        AnswerCreateRequest.builder()
                                .questionId(1L)
                                .optionIds(Arrays.asList(1L))
                                .answerText("텍스트답변")
                                .build()
                )).build();

        //When
        assertThatThrownBy(() -> submissionFactory.createSubmission(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("텍스트형 질문에는 옵션을 선택할 수 없습니다.");
    }

    @Test
    @DisplayName("Answer : 검증 실패 -> 질문 타입별 데이터 검증 -> 4. 텍스트 질문에는 옵션이있으면 안된다.")
    public void createSubmission_실패_필수질문에는_텍스트답변이있어야한다() {
        //Given
        Map<Long, Questions> questionMap = Map.of(
                1L, 텍스트_질문(1L, true)
        );

        when(questionRepository.getQuestionMapByFormId(1L, null)).thenReturn(questionMap);

        SubmissionCreateRequest request = SubmissionCreateRequest.builder()
                .formId(1L)
                .userId("testUser")
                .answerCreateRequests(Arrays.asList(
                        AnswerCreateRequest.builder()
                                .questionId(1L)
                                .optionIds(Arrays.asList())
                                .answerText("")
                                .build()
                )).build();

        //When
        assertThatThrownBy(() -> submissionFactory.createSubmission(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("필수 텍스트 질문은 답변을 입력해야 합니다.");
    }

    @Test
    @DisplayName("Answer : 검증 실패 -> 선택 유형의 답변 -> 1. 선택한 옵션은 질문의 옵션에 해당해야 한다.")
    public void createSubmission_실패_선택옵션은_질문에_옵션이어야한다() {
        //Given
        Map<Long, Questions> questionMap = Map.of(
                1L, 단일_선택_질문(1L, true)
        );

        when(questionRepository.getQuestionMapByFormId(1L, null)).thenReturn(questionMap);

        SubmissionCreateRequest request = SubmissionCreateRequest.builder()
                .formId(1L)
                .userId("testUser")
                .answerCreateRequests(Arrays.asList(
                        AnswerCreateRequest.builder()
                                .questionId(1L)
                                .optionIds(Arrays.asList(3L))
                                .build()
                )).build();

        //When
        assertThatThrownBy(() -> submissionFactory.createSubmission(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("에 없는 옵션들입니다");

    }

    @Test
    @DisplayName("Answer : 검증 실패 -> 선택 유형의 답변 -> 2. 단일 질문의 규격에 맞는가?")
    public void createSubmission_실패_선택옵션의개수가_맞는가() {
        //Given
        Map<Long, Questions> questionMap = Map.of(
                1L, 단일_선택_질문(1L, true)
        );

        when(questionRepository.getQuestionMapByFormId(1L, null)).thenReturn(questionMap);

        SubmissionCreateRequest request = SubmissionCreateRequest.builder()
                .formId(1L)
                .userId("testUser")
                .answerCreateRequests(Arrays.asList(
                        AnswerCreateRequest.builder()
                                .questionId(1L)
                                .optionIds(Arrays.asList(1L, 2L))
                                .build()
                )).build();

        //When
        assertThatThrownBy(() -> submissionFactory.createSubmission(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("단일 선택 질문에는 1개의 옵션만 선택해야 합니");

    }

    @Test
    @DisplayName("Answer : 검증 실패 -> 필수 질문에 대한 답이 모두 왔는가?")
    public void createSubmission_실패_필수질문의답변이모두작성됐는가() {
        //Given
        Map<Long, Questions> questionMap = Map.of(
                1L, 단일_선택_질문(1L, true),
                2L, 단일_선택_질문(2L, true),
                3L, 단일_선택_질문(3L, true)
        );

        when(questionRepository.getQuestionMapByFormId(1L, null)).thenReturn(questionMap);

        SubmissionCreateRequest request = SubmissionCreateRequest.builder()
                .formId(1L)
                .userId("testUser")
                .answerCreateRequests(Arrays.asList(
                        AnswerCreateRequest.builder()
                                .questionId(1L)
                                .optionIds(Arrays.asList(2L))
                                .build(),
                        AnswerCreateRequest.builder()
                                .questionId(2L)
                                .optionIds(Arrays.asList(1L))
                                .build()
                )).build();

        //When
        assertThatThrownBy(() -> submissionFactory.createSubmission(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("필수 질문에 답변하지 않았습니다: [3]");

    }
}