package com.group.surveyapp.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.surveyapp.domain.entity.*;
import com.group.surveyapp.dto.request.SurveyCreateRequestDto;
import com.group.surveyapp.dto.request.SurveyUpdateRequestDto;
import com.group.surveyapp.dto.request.SurveyAnswerRequestDto;
import com.group.surveyapp.dto.response.SurveyAnswerResponseDto;
import com.group.surveyapp.dto.response.SurveyResponseDto;
import com.group.surveyapp.repository.AnswerRepository;
import com.group.surveyapp.repository.AnswerSheetRepository;
import com.group.surveyapp.repository.QuestionRepository;
import com.group.surveyapp.repository.SurveyRepository;
import com.group.surveyapp.service.SurveyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SurveyServiceImpl implements SurveyService {

    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final AnswerSheetRepository answerSheetRepository;
    private final AnswerRepository answerRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    //설문조사 생성
    @Override
    public SurveyResponseDto createSurvey(SurveyCreateRequestDto requestDto) {
        Survey survey = new Survey();
        survey.setTitle(requestDto.getTitle());
        survey.setDescription(requestDto.getDescription());
        survey.setCreatedAt(LocalDateTime.now());

        List<Question> questions = requestDto.getQuestions().stream()
                .map(q -> {
                    Question entity = new Question();
                    entity.setSurvey(survey);
                    entity.setName(q.getName());
                    entity.setDescription(q.getDescription());
                    entity.setType(q.getType());
                    entity.setRequired(q.isRequired());
                    entity.setCandidates(q.getCandidates());
                    return entity;
                }).collect(Collectors.toList());

        survey.setQuestions(questions);
        surveyRepository.save(survey);

        return SurveyResponseDto.from(survey);
    }
    //설문조사 수정
    @Override
    public SurveyResponseDto updateSurvey(Long surveyId, SurveyUpdateRequestDto requestDto) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 설문 ID: " + surveyId));

        survey.setTitle(requestDto.getTitle());
        survey.setDescription(requestDto.getDescription());

        Map<Long, Question> existingMap = survey.getQuestions().stream()
                .filter(q -> q.getId() != null)
                .collect(Collectors.toMap(Question::getId, q -> q));

        List<Question> updatedList = new ArrayList<>();

        for (SurveyUpdateRequestDto.QuestionDto dto : requestDto.getQuestions()) {
            Question question = (dto.getQuestionId() != null && existingMap.containsKey(dto.getQuestionId()))
                    ? existingMap.get(dto.getQuestionId())
                    : new Question();

            question.setSurvey(survey);
            question.setName(dto.getName());
            question.setDescription(dto.getDescription());
            question.setType(dto.getType());
            question.setRequired(dto.isRequired());
            question.setCandidates(dto.getCandidates());

            updatedList.add(question);
        }

        survey.getQuestions().clear();
        survey.getQuestions().addAll(updatedList);

        return SurveyResponseDto.from(surveyRepository.save(survey));
    }
    //설문조사 응답 제출
    @Override
    public void submitAnswer(Long surveyId, SurveyAnswerRequestDto requestDto) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new IllegalArgumentException("설문조사를 찾을 수 없습니다."));

        // 필수 응답 체크
        survey.getQuestions().stream()
                .filter(Question::isRequired)
                .forEach(requiredQ -> {
                    boolean hasAnswer = requestDto.getAnswers().stream()
                            .anyMatch(a -> a.getQuestionId().equals(requiredQ.getId())
                                    && a.getAnswer() != null
                                    && !a.getAnswer().toString().isBlank());
                    if (!hasAnswer) {
                        throw new IllegalArgumentException("필수 질문에 응답하지 않았습니다: " + requiredQ.getName());
                    }
                });

        // 응답지 생성
        AnswerSheet sheet = new AnswerSheet();
        sheet.setSurvey(survey);
        sheet.setSubmittedAt(LocalDateTime.now());

        List<Answer> answers = requestDto.getAnswers().stream()
                .map(a -> {
                    Question question = survey.getQuestions().stream()
                            .filter(q -> q.getId().equals(a.getQuestionId()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("질문이 존재하지 않습니다."));

                    // 후보값 검증
                    if (question.getCandidates() != null && !question.getCandidates().isEmpty()) {
                        if (question.getType() == QuestionType.MULTI && a.getAnswer() instanceof List<?> multiList) {
                            for (Object selected : multiList) {
                                if (!question.getCandidates().contains(selected.toString())) {
                                    throw new IllegalArgumentException("허용되지 않은 선택지입니다: " + selected);
                                }
                            }
                        } else if (!question.getCandidates().contains(a.getAnswer().toString())) {
                            throw new IllegalArgumentException("허용되지 않은 선택지입니다: " + a.getAnswer());
                        }
                    }

                    Answer entity = new Answer();
                    entity.setAnswerSheet(sheet);
                    entity.setQuestion(question);

                    try {
                        if (question.getType() == QuestionType.MULTI && a.getAnswer() instanceof List<?>) {
                            entity.setAnswerValue(objectMapper.writeValueAsString(a.getAnswer()));
                        } else {
                            entity.setAnswerValue(a.getAnswer() != null ? a.getAnswer().toString() : null);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("응답 값 처리 중 오류 발생", e);
                    }

                    return entity;
                }).collect(Collectors.toList());

        sheet.setAnswers(answers);
        answerSheetRepository.save(sheet);
    }
    //설문조사 응답 조회
    @Override
    public List<SurveyAnswerResponseDto> getAnswers(Long surveyId) {
        List<AnswerSheet> sheets = answerSheetRepository.findAllBySurveyId(surveyId);
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new IllegalArgumentException("설문조사를 찾을 수 없습니다."));

        List<SurveyAnswerResponseDto> result = new ArrayList<>();
        for (AnswerSheet sheet : sheets) {
            SurveyAnswerResponseDto dto = new SurveyAnswerResponseDto();
            dto.setId(survey.getId());
            dto.setTitle(survey.getTitle());
            dto.setDescription(survey.getDescription());
            List<SurveyAnswerResponseDto.QuestionAnswerDto> questionAnswers =
                    survey.getQuestions().stream().map(q -> {
                        SurveyAnswerResponseDto.QuestionAnswerDto qDto = new SurveyAnswerResponseDto.QuestionAnswerDto();
                        qDto.setQuestionId(q.getId());
                        qDto.setName(q.getName());
                        qDto.setDescription(q.getDescription());
                        qDto.setType(q.getType());
                        qDto.setRequired(q.isRequired());
                        qDto.setCandidates(q.getCandidates());
                        sheet.getAnswers().stream()
                                .filter(a -> a.getQuestion().getId().equals(q.getId()))
                                .findFirst()
                                .ifPresent(a -> qDto.setAnswer(a.getAnswerValue()));
                        return qDto;
                    }).collect(Collectors.toList());
            dto.setQuestions(questionAnswers);
            result.add(dto);
        }
        return result;
    }
}
