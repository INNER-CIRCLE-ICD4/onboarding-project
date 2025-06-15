package kr.co.fastcampus.onboarding.hyeongminonboarding.modeling;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.NotSupportedException;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.handler.AnswerHandler;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.handler.AnswerHandlerFactory;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.request.SubmitAnswerRequest;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.*;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.enums.QuestionType;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.util.AnswerSnapshotSerializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ModelingTest {


    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AnswerHandlerFactory factory;
    @Test
    @DisplayName("단답형_답변_생성_테스트")
    void test__AnswerFlow__ShortText__WhenCreated() throws NotSupportedException {
        // given
        Question question = Question.builder()
                .id(1L)
                .type(QuestionType.SHORT_TEXT)
                .required(true)
                .title("이름을 입력해주세요")
                .build();

        SubmitAnswerRequest request = SubmitAnswerRequest.builder()
                .questionId(1L)
                .answerText("이빠")
                .build();

        // when
        AnswerHandler handler = factory.getHandler(question.getType());
        handler.validate(request, question);
        Answer answer = handler.createEntity(request, question, "question-json", "option-json");

        // then
        assertThat(answer.getQuestion().getId()).isEqualTo(1L);
        assertThat(answer.getAnswerText()).isEqualTo("이빠");
        assertThat(answer.getQuestionSnapshotJson()).isEqualTo("question-json");
    }

    @Test
    @DisplayName("단일선택형_답변_생성_테스트")
    void test__AnswerFlow__SingleChoice__WhenCreated() throws NotSupportedException {
        // given
        Question question = Question.builder()
                .id(2L)
                .type(QuestionType.SINGLE_CHOICE)
                .required(true)
                .title("성별을 선택해주세요")
                .build();

        List<QuestionOption> options = List.of(
                QuestionOption.builder().id(100L).optionValue("남자").question(question).build(),
                QuestionOption.builder().id(101L).optionValue("여자").question(question).build()
        );

        SubmitAnswerRequest request = SubmitAnswerRequest.builder()
                .questionId(2L)
                .selectedOptionIds(List.of(100L))  // 예: 남성
                .build();

        // when
        AnswerHandler handler = factory.getHandler(question.getType());
        handler.validate(request, question, options);
        Answer answer = handler.createEntity(request, question, "q-json", "opt-json");

        // then
        assertThat(answer.getSelectedOptionIds()).containsExactly(100L);
        assertThat(answer.getQuestion()).isEqualTo(question);
    }

    @Test
    @DisplayName("응답 생성 테스트 - 다중선택 생성시")
    void test__AnswerFlow__MultipleChoice__WhenCreated() throws NotSupportedException {
        // given
        Question question = Question.builder()
                .id(3L)
                .type(QuestionType.MULTIPLE_CHOICE)
                .required(false)
                .title("좋아하는 과일을 선택해주세요")
                .build();

        List<QuestionOption> options = List.of(
                QuestionOption.builder().id(200L).optionValue("사과").question(question).build(),
                QuestionOption.builder().id(201L).optionValue("바나나").question(question).build(),
                QuestionOption.builder().id(202L).optionValue("수박").question(question).build()
        );

        SubmitAnswerRequest request = SubmitAnswerRequest.builder()
                .questionId(3L)
                .selectedOptionIds(List.of(200L, 201L)) // 사과, 바나나 선택
                .build();

        // when
        AnswerHandler handler = factory.getHandler(question.getType());

        // validate(List<QuestionOption>) 지원하는지 체크해서 안전하게 다운캐스팅
        handler.validate(request, question, options);

        Answer answer = handler.createEntity(request, question, "q-json", "opt-json");

        // then
        assertThat(answer.getSelectedOptionIds()).containsExactly(200L, 201L);
    }

    @Test
    @DisplayName("전체적인 설문조사 모델 생성 테스트")
    void test__SurveyFlow__EntityOnly() throws JsonProcessingException {
        // 1. 설문 생성
        Survey survey = Survey.builder()
                .id(1L)
                .title("직무 만족도 조사")
                .description("직원들의 업무 만족도를 파악하기 위한 설문입니다.")
                .version(1)
                .build();

        Question question1 = Question.builder()
                .id(101L)
                .survey(survey)
                .title("당신의 직급은?")
                .type(QuestionType.SINGLE_CHOICE)
                .required(true)
                .build();

        QuestionOption option1 = QuestionOption.builder()
                .id(1001L)
                .question(question1)
                .optionValue("사원")
                .build();
        QuestionOption option2 = QuestionOption.builder()
                .id(1002L)
                .question(question1)
                .optionValue("대리")
                .build();

        // 2. 응답 저장 시 스냅샷
        String questionSnapshot = objectMapper.writeValueAsString(question1);
        String optionSnapshot = objectMapper.writeValueAsString(List.of(option1, option2));

        SurveyResponse response = SurveyResponse.builder()
                .id(999L)
                .survey(survey)
                .surveyVersion(survey.getVersion())
                .submittedAt(LocalDateTime.now())
                .build();

        Answer answer = Answer.builder()
                .id(888L)
                .response(response)
                .question(question1)
                .selectedOptionIds(List.of(1002L))
                .questionSnapshotJson(questionSnapshot)
                .optionSnapshotJson(optionSnapshot)
                .build();

        // 3. 질문 수정
        question1.setTitle("당신의 현재 직급은?");
        QuestionOption option3 = QuestionOption.builder()
                .id(1003L)
                .question(question1)
                .optionValue("부장")
                .build();

        // 4. 응답 스냅샷 복원 및 확인
        Question restoredQuestion = objectMapper.readValue(answer.getQuestionSnapshotJson(), Question.class);
        QuestionOption[] restoredOptions = objectMapper.readValue(answer.getOptionSnapshotJson(), QuestionOption[].class);

        assertEquals("당신의 직급은?", restoredQuestion.getTitle()); // 스냅샷 기준 확인
        assertEquals(2, restoredOptions.length);
        assertEquals("대리", restoredOptions[1].getOptionValue());
    }




    @Autowired
    private AnswerSnapshotSerializer snapshotSerializer;
    @Test
    @DisplayName("응답 스냅샷 변경 확인")
    void test__SnapshotPreservesAnswer__WhenQuestionChanges__UsedSerializer() throws JsonProcessingException {
        // 기존 질문/옵션
        Question originalQuestion = Question.builder()
                .id(1L)
                .title("성별을 선택하세요")
                .type(QuestionType.SINGLE_CHOICE)
                .required(true)
                .build();

        List<QuestionOption> originalOptions = List.of(
                QuestionOption.builder().id(1L).optionValue("남성").question(originalQuestion).build(),
                QuestionOption.builder().id(2L).optionValue("여성").question(originalQuestion).build()
        );

        // 스냅샷 직렬화
        String questionSnapshot = snapshotSerializer.serializeQuestion(originalQuestion);
        String optionSnapshot = snapshotSerializer.serializeOptions(originalOptions);

        // 응답 저장
        Answer answer = Answer.builder()
                .id(100L)
                .question(originalQuestion)
                .answerText(null)
                .selectedOptionIds(List.of(2L))
                .questionSnapshotJson(questionSnapshot)
                .optionSnapshotJson(optionSnapshot)
                .build();

        // 설문 변경 발생 (질문 수정, 옵션 추가/삭제)
        Question changedQuestion = Question.builder()
                .id(1L)
                .title("당신의 성별은?")
                .type(QuestionType.SINGLE_CHOICE)
                .required(true)
                .build();

        List<QuestionOption> changedOptions = List.of(
                QuestionOption.builder().id(2L).optionValue("여성").question(changedQuestion).build(),
                QuestionOption.builder().id(3L).optionValue("선택 안 함").question(changedQuestion).build()
        );

        // 스냅샷 역직렬화
        Question snapshotQuestion = snapshotSerializer.deserializeQuestion(answer.getQuestionSnapshotJson());
        List<QuestionOption> snapshotOptions = snapshotSerializer.deserializeOptions(answer.getOptionSnapshotJson());

        // 검증
        assertEquals("성별을 선택하세요", snapshotQuestion.getTitle());
        assertEquals(2, snapshotOptions.size());
        assertEquals("남성", snapshotOptions.get(0).getOptionValue());
        assertEquals("여성", snapshotOptions.get(1).getOptionValue());
    }
    @Test
    @DisplayName("응답 스냅샷 변경 확인")
    public void test__SnapshotPreservesAnswer__WhenQuestionChanges() throws JsonProcessingException {
        // 기존 질문/옵션
        Question originalQuestion = Question.builder()
                .id(1L)
                .title("성별을 선택하세요")
                .type(QuestionType.SINGLE_CHOICE)
                .required(true)
                .build();

        List<QuestionOption> originalOptions = List.of(
                QuestionOption.builder().id(1L).optionValue("남성").question(originalQuestion).build(),
                QuestionOption.builder().id(2L).optionValue("여성").question(originalQuestion).build()
        );

        // 응답 당시의 스냅샷 저장 (직렬화)
        String questionSnapshot = objectMapper.writeValueAsString(originalQuestion);
        String optionSnapshot = objectMapper.writeValueAsString(originalOptions);

        // 응답 저장
        Answer answer = Answer.builder()
                .id(100L)
                .question(originalQuestion)
                .answerText(null)
                .selectedOptionIds(List.of(2L))
                .questionSnapshotJson(questionSnapshot)
                .optionSnapshotJson(optionSnapshot)
                .build();

        // 설문 변경 발생 (질문 수정, 옵션 추가/삭제)
        Question changedQuestion = Question.builder()
                .id(1L)
                .title("당신의 성별은?") // 변경됨
                .type(QuestionType.SINGLE_CHOICE)
                .required(true)
                .build();

        List<QuestionOption> changedOptions = List.of(
                QuestionOption.builder().id(2L).optionValue("여성").question(changedQuestion).build(),
                QuestionOption.builder().id(3L).optionValue("선택 안 함").question(changedQuestion).build()
        );

        // 기존 응답을 스냅샷 기준으로 복원 가능해야 함
        Question snapshotQuestion = objectMapper.readValue(answer.getQuestionSnapshotJson(), Question.class);
        QuestionOption[] snapshotOptions = objectMapper.readValue(answer.getOptionSnapshotJson(), QuestionOption[].class);

        assertEquals("성별을 선택하세요", snapshotQuestion.getTitle());
        assertEquals(2, snapshotOptions.length);
        assertEquals("남성", snapshotOptions[0].getOptionValue());
        assertEquals("여성", snapshotOptions[1].getOptionValue());
    }
}
