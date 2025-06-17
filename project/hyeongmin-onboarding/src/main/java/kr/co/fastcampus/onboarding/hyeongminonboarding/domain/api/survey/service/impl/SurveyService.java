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
import java.util.*;
import java.util.stream.Collectors;


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
        SurveyUpdateRequest req = request.getBody();

        // 기존 Survey 조회 및 업데이트
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new EntityNotFoundException("Survey not found: " + surveyId));
        survey.setTitle(req.getTitle());
        survey.setDescription(req.getDescription());
        survey.setVersion(survey.getVersion() + 1);
        survey = surveyRepository.save(survey);

        // 기존 질문 조회
        List<Question> existingQuestions =
                questionRepository.findAllBySurveyId(surveyId);
        Map<Long, Question> existingMap = existingQuestions.stream()
                .collect(Collectors.toMap(Question::getId, q -> q));

        List<Question> finalQuestions = new ArrayList<>();
        List<QuestionOption> finalOptions = new ArrayList<>();

        // 요청으로 넘어온 질문들 처리..!
        for (SurveyUpdateRequest.QuestionSurveyRequest qr : req.getQuestions()) {
            Question question;
            if (qr.getId() != null && existingMap.containsKey(qr.getId())) {
                // 수정: 기존 질문 재활용
                question = existingMap.remove(qr.getId());
                question.setTitle(qr.getTitle());
                question.setDetail(qr.getDetail());
                question.setType(qr.getType());
                question.setRequired(qr.isRequired());
                question = questionRepository.save(question);

                // 옵션은 전부 삭제 후 재생성
                questionOptionRepository.deleteAllByQuestion(question);
            } else {
                // 신규: insert
                question = questionRepository.save(
                        Question.builder()
                                .survey(survey)
                                .title(qr.getTitle())
                                .detail(qr.getDetail())
                                .type(qr.getType())
                                .required(qr.isRequired())
                                .build()
                );
            }
            finalQuestions.add(question);

            // 옵션 생성
            if (qr.getType().isChoiceType() && qr.getOptions() != null) {
                for (String val : qr.getOptions()) {
                    finalOptions.add(
                            QuestionOption.builder()
                                    .question(question)
                                    .optionValue(val)
                                    .build()
                    );
                }
            }
        }

        // 삭제 대상 질문들은 soft-delete
        if (!existingMap.isEmpty()) {
            List<Question> toDelete = new ArrayList<>(existingMap.values());
            // 옵션 먼저 삭제
            questionOptionRepository.deleteAllByQuestionIn(toDelete);
            // 질문 soft-delete (@SQLDelete)
            questionRepository.deleteAll(toDelete);
        }

        // 최종 옵션 일괄 저장
        List<QuestionOption> savedOptions = questionOptionRepository.saveAll(finalOptions);

        // DTO 조립 후 반환
        Survey finalSurvey = survey;
        return assemblerFactory.assemble(
                SurveyResponseDto.class,
                ctx -> {
                    ctx.put(SurveyContextKey.SURVEY_CONTEXT_KEY, finalSurvey);
                    ctx.put(SurveyContextKey.QUESTION_LIST_CONTEXT_KEY, finalQuestions);
                    ctx.put(SurveyContextKey.QUESTION_OPTION_LIST_CONTEXT_KEY, savedOptions);
                }
        );
    }

    @Override
    @Transactional
    public void submitSurveyAnswer(Long surveyId, BaseRequest<SubmitSurveyAnswersRequest> request) {
        SubmitSurveyAnswersRequest req = request.getBody();

        // --- 중복 질문 응답 체크
        List<Long> questionIds = req.getAnswers().stream()
                .map(SubmitAnswerRequest::getQuestionId)
                .toList();

        Set<Long> uniqueIds = new HashSet<>(questionIds);
        if(questionIds.size() != uniqueIds.size()){
            throw new SurveyException(ErrorCode.SURVEY_QUESTION_DUPLICATED);
        }

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
    public SurveyWithAnswersResponseDto getSurveyWithAnswerResponses(Long surveyId) {
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

    @Override
    @Transactional(readOnly = true)
    public SurveyResponseDto getSurveyResponse(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new EntityNotFoundException("Survey not found: " + surveyId));

        List<Question> questions = questionRepository.findAllBySurveyId(surveyId);

        List<QuestionOption> options = questionOptionRepository.findAllByQuestionIn(questions);

        return assemblerFactory.assemble(
                SurveyResponseDto.class,
                ctx -> {
                    ctx.put(SurveyContextKey.SURVEY_CONTEXT_KEY, survey);
                    ctx.put(SurveyContextKey.QUESTION_LIST_CONTEXT_KEY, questions);
                    ctx.put(SurveyContextKey.QUESTION_OPTION_LIST_CONTEXT_KEY, options);
                }
        );
    }
}
