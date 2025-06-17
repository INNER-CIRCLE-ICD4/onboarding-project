package kr.co.fastcampus.onboarding.hyeongminonboarding.functionTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.assembler.SurveyResponseDtoAssembler;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.assembler.SurveyWithAnswersResponseDtoAssembler;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.assembler.keys.SurveyContextKey;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.request.SurveyCreateRequest;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.request.SurveyUpdateRequest;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.response.SurveyResponseDto;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.response.SurveyWithAnswersResponseDto;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.util.AnswerSnapshotSerializer;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.*;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.enums.QuestionType;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.dto.accembler.Assembler;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.dto.accembler.impl.AssemblerFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class AssemblerTest {
    static AssemblerFactory factory;
    static Validator validator;

    @BeforeAll
    static void setUp() {
        List<Assembler<?>> assemblers = List.of(
                new SurveyResponseDtoAssembler(),
                new SurveyWithAnswersResponseDtoAssembler(new AnswerSnapshotSerializer(new ObjectMapper()))
        );
        factory = new AssemblerFactory(assemblers);

        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        validator = vf.getValidator();
    }

    @Test
    void createSurveyApi_ShouldProduceSurveyResponseDto() {
        // --- given: SurveyCreateRequest
        SurveyCreateRequest.QuestionSurveyRequest q1 =
                new SurveyCreateRequest.QuestionSurveyRequest(
                        "Q1 타이틀", "Q1 디테일", QuestionType.SHORT_TEXT, true, null);
        SurveyCreateRequest.QuestionSurveyRequest q2 =
                new SurveyCreateRequest.QuestionSurveyRequest(
                        "Q2 타이틀", null, QuestionType.MULTIPLE_CHOICE, false,
                        List.of("옵션A", "옵션B"));
        SurveyCreateRequest req = SurveyCreateRequest.builder()
                .title("설문제목")
                .description("설문설명")
                .questions(List.of(q1, q2))
                .build();

        // --- when: Service 레벨 로직(요청→Entity) 흉내
        Survey survey = Survey.builder()
                .id(1L)
                .title(req.getTitle())
                .description(req.getDescription())
                .version(1)
                .build();

        List<Question> questions = new ArrayList<>();
        List<QuestionOption> options = new ArrayList<>();
        long questionSeq = 100;
        long optionSeq = 1000;
        for (var qr : req.getQuestions()) {
            Question q = Question.builder()
                    .id(questionSeq++)
                    .survey(survey)
                    .title(qr.getTitle())
                    .detail(qr.getDetail())
                    .type(qr.getType())
                    .required(qr.isRequired())
                    .build();
            questions.add(q);

            if (qr.getType().isChoiceType()) {
                for (String val : Objects.requireNonNull(qr.getOptions())) {
                    options.add(QuestionOption.builder()
                            .id(optionSeq++)
                            .question(q)
                            .optionValue(val)
                            .build());
                }
            }
        }

        // --- then: AssemblerFactory로 DTO 조립
        SurveyResponseDto dto = factory.assemble(
                SurveyResponseDto.class,
                ctx -> {
                    ctx.put(SurveyContextKey.SURVEY_CONTEXT_KEY, survey);
                    ctx.put(SurveyContextKey.QUESTION_LIST_CONTEXT_KEY, questions);
                    ctx.put(SurveyContextKey.QUESTION_OPTION_LIST_CONTEXT_KEY, options);
                }
        );

        assertEquals(survey.getId(), dto.getSurveyId());
        assertEquals("설문제목", dto.getTitle());
        assertEquals(2, dto.getQuestions().size());

        // 두 번째 질문(MULTIPLE_CHOICE)만 옵션이 채워져야 한다
        var second = dto.getQuestions().get(1);
        assertEquals(QuestionType.MULTIPLE_CHOICE, second.getType());
        assertEquals(2, second.getOptions().size());
    }

    @Test
    void updateSurveyApi_ShouldAlsoProduceSurveyResponseDto() {
        // --- given: SurveyUpdateRequest (id는 이미 존재한다고 가정)
        SurveyUpdateRequest.QuestionSurveyRequest uq =
                new SurveyUpdateRequest.QuestionSurveyRequest(
                        null, "업데이트Q", "업데이트D", QuestionType.SINGLE_CHOICE, true,
                        List.of("O1", "O2", "O3"));
        SurveyUpdateRequest req = SurveyUpdateRequest.builder()
                .id(1L)
                .title("새설문제목")
                .description("새설문설명")
                .questions(List.of(uq))
                .build();

        // --- when: 기존 Survey Entity 로드 가정 + 요청 반영
        Survey survey = Survey.builder()
                .id(req.getId())
                .title(req.getTitle())
                .description(req.getDescription())
                .version(2)  // 수정 시 버전 증가
                .build();

        Question q = Question.builder()
                .id(500L)
                .survey(survey)
                .title(uq.getTitle())
                .detail(uq.getDetail())
                .type(uq.getType())
                .required(uq.isRequired())
                .build();
        List<Question> questions = List.of(q);

        List<QuestionOption> options = new ArrayList<>();
        long optId = 2000;
        for (String val : uq.getOptions()) {
            options.add(QuestionOption.builder()
                    .id(optId++)
                    .question(q)
                    .optionValue(val)
                    .build());
        }

        // --- then: 동일한 SurveyResponseDtoAssembler 사용
        SurveyResponseDto dto = factory.assemble(
                SurveyResponseDto.class,
                ctx -> {
                    ctx.put(SurveyContextKey.SURVEY_CONTEXT_KEY, survey);
                    ctx.put(SurveyContextKey.QUESTION_LIST_CONTEXT_KEY, questions);
                    ctx.put(SurveyContextKey.QUESTION_OPTION_LIST_CONTEXT_KEY, options);
                }
        );
        assertEquals(1, dto.getQuestions().size());
        assertEquals("업데이트Q", dto.getQuestions().get(0).getTitle());
    }

    @Test
    void getSurveyResponsesApi_ShouldProduceSurveyWithAnswersResponseDto() {
        // --- given: Survey + SurveyResponse + Answer(들)
        Survey survey = Survey.builder()
                .id(42L)
                .title("설문42")
                .description("설명42")
                .version(1)
                .build();

        SurveyResponse resp1 = SurveyResponse.builder()
                .id(100L)
                .survey(survey)
                .surveyVersion(1)
                .submittedAt(LocalDateTime.now())
                .build();
        List<SurveyResponse> responses = List.of(resp1);

        // 질문 snapshot JSON & 옵션 snapshot JSON (통상 DB에서 채워주는 값)
        String questionSnapshotJson = "{\"title\":\"Qx\",\"type\":\"SINGLE_CHOICE\",\"required\":true,\"detail\":\"Dx\"}";
        String optionSnapshotJson =
                "[{\"id\":500,\"optionValue\":\"O1\"},{\"id\":501,\"optionValue\":\"O2\"}]";

        Answer answer = Answer.builder()
                .id(999L)
                .response(resp1)
                .question(Question.builder().id(123L).build())
                .answerText(null)
                .selectedOptionIds(List.of(500L))
                .questionSnapshotJson(questionSnapshotJson)
                .optionSnapshotJson(optionSnapshotJson)
                .build();
        List<Answer> answers = List.of(answer);

        // --- when: SurveyWithAnswersResponseDtoAssembler 호출
        SurveyWithAnswersResponseDto dto = factory.assemble(
                SurveyWithAnswersResponseDto.class,
                ctx -> {
                    ctx.put(SurveyContextKey.SURVEY_CONTEXT_KEY, survey);
                    ctx.put(SurveyContextKey.SURVEY_RESPONSE_LIST_CONTEXT_KEY, responses);
                    ctx.put(SurveyContextKey.ANSWER_LIST_CONTEXT_KEY, answers);
                }
        );

        // --- then: 복원된 Snapshot 값이 제대로 반영되는지 확인
        assertEquals(1, dto.getResponses().size());
        var respDto = dto.getResponses().get(0);
        assertEquals(100L, respDto.getRespondentId());
        assertEquals(1, respDto.getAnswers().size());

        var ansDto = respDto.getAnswers().get(0);
        assertEquals(123L, ansDto.getQuestionId());
        assertEquals(List.of(500L), ansDto.getSelectedOptionIds());
        assertEquals("Qx", ansDto.getQuestionSnapshot().getTitle());
        assertEquals(2, ansDto.getOptionSnapshot().size());
    }



    @Test
    void largeVolumeAssemblyPerformance() {
        int qCount = 1000;
        int optPerQ = 5;
        Survey survey = Survey.builder().id(1L).title("bulk").description("").version(1).build();

        List<Question> questions = new ArrayList<>(qCount);
        List<QuestionOption> options = new ArrayList<>(qCount * optPerQ);
        for (long i = 0; i < qCount; i++) {
            Question q = Question.builder()
                    .id(i)
                    .survey(survey)
                    .title("Q" + i)
                    .detail("D" + i)
                    .type(QuestionType.MULTIPLE_CHOICE)
                    .required(false)
                    .build();
            questions.add(q);
            for (int j = 0; j < optPerQ; j++) {
                options.add(QuestionOption.builder()
                        .id(i * optPerQ + j)
                        .question(q)
                        .optionValue("O" + j)
                        .build());
            }
        }

        long start = System.currentTimeMillis();
        SurveyResponseDto dto = factory.assemble(
                SurveyResponseDto.class,
                ctx -> {
                    ctx.put(SurveyContextKey.SURVEY_CONTEXT_KEY, survey);
                    ctx.put(SurveyContextKey.QUESTION_LIST_CONTEXT_KEY, questions);
                    ctx.put(SurveyContextKey.QUESTION_OPTION_LIST_CONTEXT_KEY, options);
                }
        );
        long elapsed = System.currentTimeMillis() - start;
        assertEquals(qCount, dto.getQuestions().size());
        assertTrue(elapsed < 500, "조립에 너무 오래 걸립니다: " + elapsed + "ms");
    }

    @Test
    void mixedQuestionTypesAssembly() {
        Survey survey = Survey.builder().id(2L).title("mix").description("").version(1).build();
        List<Question> questions = List.of(
                Question.builder().id(1L).survey(survey).title("S").detail("").type(QuestionType.SHORT_TEXT).required(true).build(),
                Question.builder().id(2L).survey(survey).title("L").detail("").type(QuestionType.LONG_TEXT).required(false).build(),
                Question.builder().id(3L).survey(survey).title("SC").detail("").type(QuestionType.SINGLE_CHOICE).required(true).build(),
                Question.builder().id(4L).survey(survey).title("MC").detail("").type(QuestionType.MULTIPLE_CHOICE).required(false).build()
        );
        List<QuestionOption> options = List.of(
                QuestionOption.builder().id(10L).question(questions.get(2)).optionValue("A1").build(),
                QuestionOption.builder().id(11L).question(questions.get(2)).optionValue("A2").build(),
                QuestionOption.builder().id(12L).question(questions.get(3)).optionValue("B1").build(),
                QuestionOption.builder().id(13L).question(questions.get(3)).optionValue("B2").build()
        );

        SurveyResponseDto dto = factory.assemble(
                SurveyResponseDto.class,
                ctx -> {
                    ctx.put(SurveyContextKey.SURVEY_CONTEXT_KEY, survey);
                    ctx.put(SurveyContextKey.QUESTION_LIST_CONTEXT_KEY, questions);
                    ctx.put(SurveyContextKey.QUESTION_OPTION_LIST_CONTEXT_KEY, options);
                }
        );

        // SHORT_TEXT, LONG_TEXT는 옵션이 null
        assertNull(dto.getQuestions().get(0).getOptions());
        assertNull(dto.getQuestions().get(1).getOptions());
        // SINGLE_CHOICE는 1개
        assertEquals(2, dto.getQuestions().get(2).getOptions().size());
        // MULTIPLE_CHOICE는 2개
        assertEquals(2, dto.getQuestions().get(3).getOptions().size());
    }

    @Test
    void multipleResponsesScenario() {
        Survey survey = Survey.builder().id(3L).title("multi").description("").version(1).build();
        // 10개의 응답 생성
        List<SurveyResponse> responses = new ArrayList<>();
        List<Answer> answers = new ArrayList<>();
        for (long i = 0; i < 10; i++) {
            SurveyResponse r = SurveyResponse.builder()
                    .id(i)
                    .survey(survey)
                    .surveyVersion(1)
                    .submittedAt(LocalDateTime.now().minusDays(i))
                    .build();
            responses.add(r);
            // 응답당 5개 Answer 생성
            for (long j = 0; j < 5; j++) {
                String qSnap = String.format("{\"title\":\"Q%d_%d\",\"type\":\"SHORT_TEXT\",\"required\":false,\"detail\":\"D\"}", i, j);
                answers.add(Answer.builder()
                        .id(i * 10 + j)
                        .response(r)
                        .question(Question.builder().id(i * 100 + j).build())
                        .answerText("A" + j)
                        .selectedOptionIds(null)
                        .questionSnapshotJson(qSnap)
                        .optionSnapshotJson("[]")
                        .build());
            }
        }

        SurveyWithAnswersResponseDto dto = factory.assemble(
                SurveyWithAnswersResponseDto.class,
                ctx -> {
                    ctx.put(SurveyContextKey.SURVEY_CONTEXT_KEY, survey);
                    ctx.put(SurveyContextKey.SURVEY_RESPONSE_LIST_CONTEXT_KEY, responses);
                    ctx.put(SurveyContextKey.ANSWER_LIST_CONTEXT_KEY, answers);
                }
        );

        assertEquals(10, dto.getResponses().size());
        dto.getResponses().forEach(respDto ->
                assertEquals(5, respDto.getAnswers().size())
        );
    }

    @Test
    void mediumVolumePerformance() {
        int qCount = 1000;
        Survey survey = Survey.builder().id(4L).title("med").description("").version(1).build();
        List<Question> qs = new ArrayList<>();
        List<QuestionOption> opts = new ArrayList<>();
        for (long i = 0; i < qCount; i++) {
            Question q = Question.builder()
                    .id(i)
                    .survey(survey)
                    .title("Q" + i)
                    .detail("")
                    .type(QuestionType.SINGLE_CHOICE)
                    .required(false)
                    .build();
            qs.add(q);
            opts.add(QuestionOption.builder().id(i).question(q).optionValue("O").build());
        }

        long t0 = System.nanoTime();
        factory.assemble(
                SurveyResponseDto.class,
                ctx -> {
                    ctx.put(SurveyContextKey.SURVEY_CONTEXT_KEY, survey);
                    ctx.put(SurveyContextKey.QUESTION_LIST_CONTEXT_KEY, qs);
                    ctx.put(SurveyContextKey.QUESTION_OPTION_LIST_CONTEXT_KEY, opts);
                }
        );
        long durationMs = (System.nanoTime() - t0) / 1_000_000;
        assertTrue(durationMs < 100, "조립이 너무 느립니다: " + durationMs + "ms");
    }

}
