package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.service.impl;

import jakarta.persistence.EntityNotFoundException;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.assembler.SurveyResponseDtoAssembler;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.assembler.keys.SurveyContextKey;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.request.SubmitAnswerRequest;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.request.SubmitSurveyAnswersRequest;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.request.SurveyCreateRequest;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.request.SurveyUpdateRequest;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.response.SurveyResponseDto;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.response.SurveyWithAnswersResponseDto;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.service.ISurveyService;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.util.AnswerSnapshotSerializer;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.*;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.enums.QuestionType;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.exception.SurveyException;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.repository.*;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.dto.accembler.impl.AssemblerFactory;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.dto.request.BaseRequest;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.exception.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service
@RequiredArgsConstructor
public class SurveyService implements ISurveyService {

    private final AnswerRepository answerRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final QuestionRepository questionRepository;
    private final SurveyRepository surveyRepository;
    private final SurveyResponseRepository surveyResponseRepository;
    private final SurveyResponseDtoAssembler surveyAssembler;
    private final AssemblerFactory assemblerFactory;
    private final AnswerSnapshotSerializer serializer;


    @Transactional
    @Override
    public SurveyResponseDto createSurvey(BaseRequest<SurveyCreateRequest> req) {
        SurveyCreateRequest request = req.getBody();

        // Survey 저장
        Survey survey = surveyRepository.save(
                Survey.builder()
                        .title(request.getTitle())
                        .description(request.getDescription())
                        .version(1)
                        .build()
        );

        // Question 엔티티 일괄 생성
        List<Question> questions = request.getQuestions().stream()
                .map(qr -> Question.builder()
                        .survey(survey)
                        .title(qr.getTitle())
                        .detail(qr.getDetail())
                        .type(qr.getType())
                        .required(qr.isRequired())
                        .build()
                ).toList();

        questions = questionRepository.saveAll(questions);

        // QuestionOption 엔티티 일괄 생성
        List<QuestionOption> allOptions = new ArrayList<>();
        for (int i = 0; i < request.getQuestions().size(); i++) {
            var qr = request.getQuestions().get(i);
            if (qr.getType().isChoiceType() && qr.getOptions() != null) {
                Question parent = questions.get(i);
                qr.getOptions().stream()
                        .map(val -> QuestionOption.builder()
                                .question(parent)
                                .optionValue(val)
                                .build()
                        )
                        .forEach(allOptions::add);
            }
        }

        List<QuestionOption> options = questionOptionRepository.saveAll(allOptions);

        // DTO 조립해서 반환
        List<Question> finalQuestions = questions;
        return assemblerFactory.assemble(
                SurveyResponseDto.class,
                ctx -> {
                    ctx.put(SurveyContextKey.SURVEY_CONTEXT_KEY, survey);
                    ctx.put(SurveyContextKey.QUESTION_LIST_CONTEXT_KEY, finalQuestions);
                    ctx.put(SurveyContextKey.QUESTION_OPTION_LIST_CONTEXT_KEY, options);
                }
        );
    }

    @Override
    @Transactional
    public SurveyResponseDto updateSurvey(Long surveyId, BaseRequest<SurveyUpdateRequest> request) {
        return null;
    }

    @Override
    @Transactional
    public void submitSurveyAnswer(Long surveyId, BaseRequest<SubmitSurveyAnswersRequest> request) {
        SubmitSurveyAnswersRequest req = request.getBody();

        // 설문 조사 존재 확인
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new EntityNotFoundException("Survey not found: " + surveyId));

        // SurveyResponse 생성
        SurveyResponse resp = surveyResponseRepository.save(
                SurveyResponse.builder()
                        .survey(survey)
                        .surveyVersion(survey.getVersion())
                        .submittedAt(LocalDateTime.now())
                        .build()
        );

        // 요청으로 넘어온 모든 답변 처리
        for (SubmitAnswerRequest ansReq : req.getAnswers()) {
            // 질문 조회
            Question question = questionRepository.findById(ansReq.getQuestionId())
                    .orElseThrow(() -> new EntityNotFoundException("Question not found: " + ansReq.getQuestionId()));


            if(
                    (question.getType() == QuestionType.SINGLE_CHOICE ||
                            question.getType() == QuestionType.MULTIPLE_CHOICE ) &&
                            CollectionUtils.isEmpty(ansReq.getSelectedOptionIds())
            ){
                throw new SurveyException(ErrorCode.SURVEY_QUESTION_OPTION_EMPTY);
            }
            // 스냅샷 JSON
            String qSnap = serializer.serializeQuestion(question);
            String optsSnap = serializer.serializeOptions(
                    questionOptionRepository.findByQuestionId(question.getId())
            );

            // Answer 엔티티 빌드
            Answer.AnswerBuilder ab = Answer.builder()
                    .response(resp)
                    .question(question)
                    .questionSnapshotJson(qSnap)
                    .optionSnapshotJson(optsSnap);

            if (ansReq.getAnswerText() != null) {
                ab.answerText(ansReq.getAnswerText());
            }
            if (ansReq.getSelectedOptionIds() != null) {
                ab.selectedOptionIds(ansReq.getSelectedOptionIds());
            }

            answerRepository.save(ab.build());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public SurveyWithAnswersResponseDto getSurveyResponses(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new EntityNotFoundException("Survey not found: " + surveyId));

        List<SurveyResponse> responses =
                surveyResponseRepository.findAllBySurveyIdOrderBySubmittedAtAsc(surveyId);

        List<Answer> answers = answerRepository.findAllByResponseIn(responses);

        return assemblerFactory.assemble(
                SurveyWithAnswersResponseDto.class,
                ctx -> {
                    ctx.put(SurveyContextKey.SURVEY_CONTEXT_KEY, survey);
                    ctx.put(SurveyContextKey.SURVEY_RESPONSE_LIST_CONTEXT_KEY, responses);
                    ctx.put(SurveyContextKey.ANSWER_LIST_CONTEXT_KEY, answers);
                }
        );
    }
}
