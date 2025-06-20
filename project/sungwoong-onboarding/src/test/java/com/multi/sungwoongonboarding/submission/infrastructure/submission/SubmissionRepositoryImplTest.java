package com.multi.sungwoongonboarding.submission.infrastructure.submission;

import com.multi.sungwoongonboarding.forms.application.repository.FormRepository;
import com.multi.sungwoongonboarding.forms.domain.Forms;
import com.multi.sungwoongonboarding.options.application.repository.OptionsRepository;
import com.multi.sungwoongonboarding.options.domain.Options;
import com.multi.sungwoongonboarding.questions.domain.Questions;
import com.multi.sungwoongonboarding.submission.domain.Answers;
import com.multi.sungwoongonboarding.submission.domain.SelectedOption;
import com.multi.sungwoongonboarding.submission.domain.Submission;
import com.multi.sungwoongonboarding.submission.infrastructure.answers.AnswerJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.multi.sungwoongonboarding.questions.domain.Questions.QuestionType.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubmissionRepositoryImplTest {

    @Mock
    private FormRepository formRepository;

    @Mock
    private SubmissionJpaRepository submissionJpaRepository;

    @Mock
    private OptionsRepository optionsRepository;



    @InjectMocks
    private SubmissionRepositoryImpl submissionRepository;


    private Forms 설문지_조회(Long formId) {

        List<Questions> questions = List.of(
                Questions.builder()
                        .id(1L)
                        .questionType(SINGLE_CHOICE)
                        .options(List.of(
                                Options.builder().id(1L).optionText("옵션1").build(),
                                Options.builder().id(2L).optionText("옵션2").build(),
                                Options.builder().id(3L).optionText("옵션3").build()
                        ))
                        .isRequired(true)
                        .build(),

                Questions.builder()
                        .id(2L)
                        .questionType(MULTIPLE_CHOICE)
                        .options(List.of(
                                Options.builder().id(4L).optionText("옵션4").build(),
                                Options.builder().id(5L).optionText("옵션5").build(),
                                Options.builder().id(6L).optionText("옵션6").build()
                        ))
                        .isRequired(true)
                        .build());

        return Forms.builder()
                .id(formId)
                .title("설문지1")
                .version(1)
                .userId("sungwoong")
                .description("설문지 설명")
                .questions(questions)
                .build();
    }


    @Test
    @DisplayName("Submission : 응답 제출 기능")
    public void submitSubmission() {

        //Given
        Answers answers = Answers.builder()
                .id(1L)
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
                .id(1L)
                .formId(1L)
                .userId("sungwoong")
                .answers(List.of(answers))
                .build();

        SubmissionJpaEntity submissionJpaEntity = SubmissionJpaEntity.fromDomainWithFormVersion(submission, submission.getFormVersion());
        ArgumentCaptor<SubmissionJpaEntity> captor = ArgumentCaptor.forClass(SubmissionJpaEntity.class);

        // form 조회
        when(formRepository.findById(submission.getId())).thenReturn(설문지_조회(submission.getFormId()));
        // submission jpa 저장
        when(submissionJpaRepository.save(any(SubmissionJpaEntity.class))).thenReturn(submissionJpaEntity);

        //When
        Long save = submissionRepository.save(submission);

        //Then
        verify(formRepository).findById(1L);
        verify(submissionJpaRepository).save(any(SubmissionJpaEntity.class));
        verify(submissionJpaRepository).save(captor.capture());

        SubmissionJpaEntity captorEntity = captor.getValue();
        assertThat(captorEntity.getId()).isEqualTo(1L);
        assertThat(captorEntity.getUserId()).isEqualTo("sungwoong");
        assertThat(captorEntity.getFormVersion()).isEqualTo(1);
    }

    @Test
    @DisplayName("Submission : formId에 해당하는 응답지를 가져온다.")
    public void formId로_조회하기() {

        //Given
        Long formId = 1L;

        List<SubmissionJpaEntity> mockSubmissions = List.of(
                SubmissionJpaEntity.builder()
                        .id(1L)
                        .formId(formId)
                        .userId("sungwoong")
                        .formVersion(1)
                        .answers(List.of(
                                AnswerJpaEntity.builder()
                                        .questionId(1L)
                                        .optionId(1L)
                                        .build()
                        ))
                        .build(),
                SubmissionJpaEntity.builder()
                        .id(1L)
                        .formId(formId)
                        .userId("sungwoong_1")
                        .formVersion(1)
                        .answers(List.of(
                                AnswerJpaEntity.builder()
                                        .questionId(1L)
                                        .optionId(1L)
                                        .build(),
                                AnswerJpaEntity.builder()
                                        .questionId(1L)
                                        .optionId(2L)
                                        .build()
                        ))
                        .build()
        );


        Map<Long, Options> optionsMap = new HashMap<>();
        optionsMap.put(1L, Options.builder().id(1L).optionText("비프").build());
        optionsMap.put(2L, Options.builder().id(2L).optionText("치킨").build());
        optionsMap.put(3L, Options.builder().id(3L).optionText("맥도날드").build());
        optionsMap.put(4L, Options.builder().id(4L).optionText("맘스터치").build());
        optionsMap.put(5L, Options.builder().id(5L).optionText("버거킹").build());


        //When
        when(submissionJpaRepository.findByFormId(any(Long.class))).thenReturn(mockSubmissions);
        when(optionsRepository.getOptionMapByFormId(any(Long.class))).thenReturn(optionsMap);
        List<Submission> byFormId = submissionRepository.findByFormId(formId);


        //Then
        assertThat(byFormId).hasSize(2);
        assertThat(byFormId).extracting(Submission::getFormId).containsOnly(formId);
        assertThat(byFormId).hasSize(2);
        assertThat(byFormId.get(0).getAnswers()).hasSize(1);
        assertThat(byFormId.get(1).getAnswers()).hasSize(1);
        assertThat(byFormId.get(1).getAnswers().get(0).getSelectedOptions()).hasSize(2);
        verify(submissionJpaRepository).findByFormId(formId);
    }
}