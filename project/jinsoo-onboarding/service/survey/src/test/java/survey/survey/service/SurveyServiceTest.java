package survey.survey.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import survey.survey.config.ApplicationException;
import survey.survey.config.ErrorType;
import survey.survey.entity.surveyform.SurveyForm;
import survey.survey.entity.surveyquestion.CheckCandidate;
import survey.survey.entity.surveyquestion.InputType;
import survey.survey.entity.surveyquestion.SurveyQuestion;
import survey.survey.repository.SurveyFormRepository;
import survey.survey.repository.SurveyQuestionRepository;
import survey.survey.service.response.SurveyResponse;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("SurveyService 테스트")
class SurveyServiceTest {

    @InjectMocks
    private SurveyService surveyService;

    @Mock
    private SurveyFormRepository surveyFormRepository;

    @Mock
    private SurveyQuestionRepository surveyQuestionRepository;

    @Nested
    @DisplayName("설문지 생성 테스트")
    class CreateSurveyFormTest {

        @Test
        @DisplayName("유효한 요청으로 설문지를 성공적으로 생성한다")
        void shouldCreateSurveyFormSuccessfully() {
            // given
            var request = givenValidSurveyFormRequest();
            var savedSurveyForm = givenSavedSurveyForm();
            var savedQuestions = givenMockQuestions();
            given(surveyFormRepository.save(any(SurveyForm.class))).willReturn(savedSurveyForm);
            given(surveyQuestionRepository.saveAll(anyList())).willReturn(savedQuestions);

            // when
            SurveyResponse result = surveyService.create(request);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getSurveyId()).isEqualTo(12345L);
            assertThat(result.getTitle()).isEqualTo("테스트 설문지");
            assertThat(result.getDescription()).isEqualTo("테스트용 설문지입니다");
            assertThat(result.getQuestionList()).hasSize(2);
            verify(surveyFormRepository).save(any(SurveyForm.class));
            verify(surveyQuestionRepository).saveAll(anyList());
        }

        @Test
        @DisplayName("질문이 최소 개수 미만이면 예외가 발생한다")
        void shouldThrowExceptionWhenQuestionCountBelowMinimum() {
            // given
            SurveyFormCreateRequest request = givenEmptyQuestionsRequest();
            SurveyForm mockForm = mock(SurveyForm.class);
            given(mockForm.getSurveyFormId()).willReturn(1234567890L);
            given(surveyFormRepository.save(any(SurveyForm.class))).willReturn(mockForm);

            // when & then
            assertThatThrownBy(() -> surveyService.create(request))
                    .isInstanceOf(ApplicationException.class)
                    .matches(e -> ((ApplicationException) e).getErrorType() == ErrorType.MINIMUM_QUESTION);
        }

        @Test
        @DisplayName("질문이 최대 개수를 초과하면 예외가 발생한다")
        void shouldThrowExceptionWhenQuestionCountAboveMaximum() {
            // given
            SurveyFormCreateRequest request = givenTooManyQuestionsRequest();
            SurveyForm mockForm = mock(SurveyForm.class);
            given(mockForm.getSurveyFormId()).willReturn(1234567890L);
            given(surveyFormRepository.save(any(SurveyForm.class))).willReturn(mockForm);

            // when & then
            assertThatThrownBy(() -> surveyService.create(request))
                    .isInstanceOf(ApplicationException.class)
                    .matches(e -> ((ApplicationException) e).getErrorType() == ErrorType.MAXIMUM_QUESTION);
        }

        @Test
        @DisplayName("선택형 질문에 후보자가 없으면 InputType 검증 예외가 발생한다")
        void shouldThrowExceptionWhenChoiceQuestionHasNoCandidates() {
            // given
            SurveyFormCreateRequest request = givenChoiceQuestionWithoutCandidatesRequest();
            SurveyForm mockForm = mock(SurveyForm.class);
            given(mockForm.getSurveyFormId()).willReturn(1234567890L);
            given(surveyFormRepository.save(any(SurveyForm.class))).willReturn(mockForm);

            // when & then
            assertThatThrownBy(() -> surveyService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("선택형 질문에는 최소 하나 이상의 선택지가 필요합니다");
        }

        @Test
        @DisplayName("텍스트 질문에 후보자가 있으면 InputType 검증 예외가 발생한다")
        void shouldThrowExceptionWhenTextQuestionHasCandidates() {
            // given
            SurveyFormCreateRequest request = givenTextQuestionWithCandidatesRequest();
            SurveyForm mockForm = mock(SurveyForm.class);
            given(mockForm.getSurveyFormId()).willReturn(1234567890L);
            given(surveyFormRepository.save(any(SurveyForm.class))).willReturn(mockForm);

            // when & then
            assertThatThrownBy(() -> surveyService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("질문에는 선택지를 추가할 수 없습니다");
        }

        @Test
        @DisplayName("생성 요청의 모든 필드가 SurveyForm 엔티티에 올바르게 매핑된다")
        void shouldMapAllFieldsCorrectlyToEntity() {
            // given
            SurveyFormCreateRequest request = givenValidSurveyFormRequest();
            given(surveyFormRepository.save(any(SurveyForm.class))).willAnswer(invocation -> invocation.getArgument(0));
            given(surveyQuestionRepository.saveAll(anyList())).willAnswer(invocation -> invocation.getArgument(0));

            // when
            surveyService.create(request);

            // then
            ArgumentCaptor<SurveyForm> formCaptor = ArgumentCaptor.forClass(SurveyForm.class);
            verify(surveyFormRepository).save(formCaptor.capture());

            SurveyForm capturedForm = formCaptor.getValue();
            assertThat(capturedForm.getTitle()).isEqualTo(request.title());
            assertThat(capturedForm.getDescription()).isEqualTo(request.description());
            assertThat(capturedForm.getSurveyId()).isEqualTo(request.surveyId());

            // 질문 확인
            ArgumentCaptor<List<SurveyQuestion>> questionsCaptor = ArgumentCaptor.forClass(List.class);
            verify(surveyQuestionRepository).saveAll(questionsCaptor.capture());

            List<SurveyQuestion> capturedQuestions = questionsCaptor.getValue();
            assertThat(capturedQuestions).hasSize(request.questionList().size());

            // 첫 번째 질문 확인
            SurveyQuestion firstQuestion = capturedQuestions.get(0);
            QuestionCreateRequest firstRequest = request.questionList().get(0);
            assertThat(firstQuestion.getName()).isEqualTo(firstRequest.name());
            assertThat(firstQuestion.getDescription()).isEqualTo(firstRequest.description());
            assertThat(firstQuestion.getInputType()).isEqualTo(firstRequest.inputType());
            assertThat(firstQuestion.isRequired()).isEqualTo(firstRequest.required());
            assertThat(firstQuestion.getSurveyFormId()).isEqualTo(capturedForm.getSurveyFormId());

            // 두 번째 질문의 후보자 확인
            SurveyQuestion secondQuestion = capturedQuestions.get(1);
            assertThat(secondQuestion.getCandidates()).hasSize(2);
        }

        // Test Data Builders (Given 메서드들)
        private SurveyFormCreateRequest givenValidSurveyFormRequest() {
            return new SurveyFormCreateRequest(
                    12345L,
                    "테스트 설문지",
                    "테스트용 설문지입니다",
                    Arrays.asList(
                            new QuestionCreateRequest(
                                    "텍스트 질문", 0, "텍스트 입력 질문입니다", InputType.SHORT_ANSWER, false, null
                            ),
                            new QuestionCreateRequest(
                                    "선택형 질문", 1, "선택지가 있는 질문입니다", InputType.MULTIPLE_CHOICE, true,
                                    Arrays.asList(
                                            new QuestionCreateRequest.CandidateCreateRequest(0, "선택지 1"),
                                            new QuestionCreateRequest.CandidateCreateRequest(1, "선택지 2")
                                    )
                            )
                    )
            );
        }

        private SurveyFormCreateRequest givenEmptyQuestionsRequest() {
            return new SurveyFormCreateRequest(
                    12345L,
                    "질문 없는 설문지",
                    "질문이 없는 설문지입니다",
                    Collections.emptyList()
            );
        }

        private SurveyFormCreateRequest givenTooManyQuestionsRequest() {
            // MAX_QUESTIONS + 1개의 질문 생성
            List<QuestionCreateRequest> questions = new java.util.ArrayList<>();
            for (int i = 0; i < 11; i++) { // 11개 질문 (최대 10개까지 허용)
                questions.add(new QuestionCreateRequest(
                        "질문 " + i, i, "설명 " + i, InputType.SHORT_ANSWER, false, null
                ));
            }

            return new SurveyFormCreateRequest(
                    12345L,
                    "너무 많은 질문",
                    "질문이 너무 많은 설문지",
                    questions
            );
        }

        private SurveyFormCreateRequest givenChoiceQuestionWithoutCandidatesRequest() {
            return new SurveyFormCreateRequest(
                    12345L,
                    "선택지 없는 선택형 질문",
                    "선택지가 없는 선택형 질문이 있는 설문지",
                    Collections.singletonList(
                            new QuestionCreateRequest(
                                    "선택지 없는 선택형 질문", 0, "선택지가 없습니다", InputType.SINGLE_CHOICE, true, null
                            )
                    )
            );
        }

        private SurveyFormCreateRequest givenTextQuestionWithCandidatesRequest() {
            return new SurveyFormCreateRequest(
                    12345L,
                    "선택지 있는 텍스트 질문",
                    "선택지가 있는 텍스트 질문이 있는 설문지",
                    Collections.singletonList(
                            new QuestionCreateRequest(
                                    "선택지 있는 텍스트 질문", 0, "선택지가 있습니다", InputType.SHORT_ANSWER, true,
                                    Collections.singletonList(
                                            new QuestionCreateRequest.CandidateCreateRequest(0, "불필요한 선택지")
                                    )
                            )
                    )
            );
        }

        private SurveyForm givenSavedSurveyForm() {
            // 모의 SurveyForm 객체 생성
            SurveyForm mockForm = mock(SurveyForm.class);

            // 필요한 메서드 스텁
            given(mockForm.getSurveyFormId()).willReturn(1234567890L);
            given(mockForm.getVersion()).willReturn(1L);
            given(mockForm.getSurveyId()).willReturn(12345L);
            given(mockForm.getTitle()).willReturn("테스트 설문지");
            given(mockForm.getDescription()).willReturn("테스트용 설문지입니다");
            given(mockForm.getCreatedAt()).willReturn(LocalDateTime.now());
            given(mockForm.getModifiedAt()).willReturn(LocalDateTime.now());

            return mockForm;
        }

        private List<SurveyQuestion> givenMockQuestions() {
            // 텍스트 질문
            SurveyQuestion textQuestion = mock(SurveyQuestion.class);
            given(textQuestion.getQuestionId()).willReturn(1111111111L);
            given(textQuestion.getQuestionIndex()).willReturn(0);
            given(textQuestion.getName()).willReturn("텍스트 질문");
            given(textQuestion.getDescription()).willReturn("텍스트 입력 질문입니다");
            given(textQuestion.getInputType()).willReturn(InputType.SHORT_ANSWER);
            given(textQuestion.isRequired()).willReturn(false);
            given(textQuestion.getCandidates()).willReturn(Collections.emptyList());

            // 선택형 질문
            SurveyQuestion choiceQuestion = mock(SurveyQuestion.class);
            given(choiceQuestion.getQuestionId()).willReturn(2222222222L);
            given(choiceQuestion.getQuestionIndex()).willReturn(1);
            given(choiceQuestion.getName()).willReturn("선택형 질문");
            given(choiceQuestion.getDescription()).willReturn("선택지가 있는 질문입니다");
            given(choiceQuestion.getInputType()).willReturn(InputType.MULTIPLE_CHOICE);
            given(choiceQuestion.isRequired()).willReturn(true);
            given(choiceQuestion.getCandidates()).willReturn(Arrays.asList(
                    CheckCandidate.of(0, "선택지 1"),
                    CheckCandidate.of(1, "선택지 2")
            ));

            return Arrays.asList(textQuestion, choiceQuestion);
        }
    }
}
