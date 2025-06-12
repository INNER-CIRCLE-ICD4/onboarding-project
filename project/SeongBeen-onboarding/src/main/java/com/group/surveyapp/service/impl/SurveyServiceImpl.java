package com.group.surveyapp.service.impl;

import com.group.surveyapp.domain.entity.Answer;
import com.group.surveyapp.domain.entity.AnswerSheet;
import com.group.surveyapp.domain.entity.Question;
import com.group.surveyapp.domain.entity.Survey;
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
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class SurveyServiceImpl implements SurveyService {

    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final AnswerSheetRepository answerSheetRepository;
    private final AnswerRepository answerRepository;

    @Override
    public SurveyResponseDto createSurvey(SurveyCreateRequestDto requestDto) {
        Survey survey = new Survey();
        survey.setTitle(requestDto.getTitle());
        survey.setDescription(requestDto.getDescription());
        survey.setCreatedAt(LocalDateTime.now());

        // 질문 생성
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
                })
                .collect(Collectors.toList());

        survey.setQuestions(questions);
        surveyRepository.save(survey);

        // DTO 변환
        SurveyResponseDto response = new SurveyResponseDto();
        response.setSurveyId(survey.getId());
        response.setTitle(survey.getTitle());
        response.setDescription(survey.getDescription());
        response.setCreatedAt(survey.getCreatedAt().toString());
        response.setQuestions(questions.stream().map(q -> {
            SurveyResponseDto.QuestionDto dto = new SurveyResponseDto.QuestionDto();
            dto.setQuestionId(q.getId());
            dto.setName(q.getName());
            dto.setDescription(q.getDescription());
            dto.setType(q.getType());
            dto.setRequired(q.isRequired());
            dto.setCandidates(q.getCandidates());
            return dto;
        }).collect(Collectors.toList()));
        return response;
    }

    @Override
    public SurveyResponseDto updateSurvey(Long surveyId, SurveyUpdateRequestDto requestDto) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new IllegalArgumentException("설문조사를 찾을 수 없습니다."));
        survey.setTitle(requestDto.getTitle());
        survey.setDescription(requestDto.getDescription());

        // 기존 질문 삭제 후 새로 추가
        questionRepository.deleteAll(survey.getQuestions());
        List<Question> newQuestions = requestDto.getQuestions().stream()
                .map(q -> {
                    Question entity = new Question();
                    entity.setSurvey(survey);
                    entity.setName(q.getName());
                    entity.setDescription(q.getDescription());
                    entity.setType(q.getType());
                    entity.setRequired(q.isRequired());
                    entity.setCandidates(q.getCandidates());
                    return entity;
                })
                .collect(Collectors.toList());
        survey.setQuestions(newQuestions);
        surveyRepository.save(survey);

        // DTO 변환
        SurveyResponseDto response = new SurveyResponseDto();
        response.setSurveyId(survey.getId());
        response.setTitle(survey.getTitle());
        response.setDescription(survey.getDescription());
        response.setCreatedAt(survey.getCreatedAt().toString());
        response.setQuestions(newQuestions.stream().map(q -> {
            SurveyResponseDto.QuestionDto dto = new SurveyResponseDto.QuestionDto();
            dto.setQuestionId(q.getId());
            dto.setName(q.getName());
            dto.setDescription(q.getDescription());
            dto.setType(q.getType());
            dto.setRequired(q.isRequired());
            dto.setCandidates(q.getCandidates());
            return dto;
        }).collect(Collectors.toList()));
        return response;
    }

    @Override
    public void submitAnswer(Long surveyId, SurveyAnswerRequestDto requestDto) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new IllegalArgumentException("설문조사를 찾을 수 없습니다."));

        // 답변지 생성
        AnswerSheet sheet = new AnswerSheet();
        sheet.setSurvey(survey);
        sheet.setSubmittedAt(LocalDateTime.now());

        // 답변 저장
        List<Answer> answers = requestDto.getAnswers().stream()
                .map(a -> {
                    Answer entity = new Answer();
                    entity.setAnswerSheet(sheet);
                    // questionName → 실제 Question 엔티티로 변환
                    Question question = survey.getQuestions().stream()
                            .filter(q -> q.getId().equals(a.getQuestionId()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("질문이 존재하지 않습니다."));
                    entity.setQuestion(question);
                    entity.setAnswerValue(a.getAnswer() == null ? null : a.getAnswer().toString());
                    return entity;
                })
                .collect(Collectors.toList());
        sheet.setAnswers(answers);
        answerSheetRepository.save(sheet);
    }

    @Override
    @Transactional()
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
            // 각 질문+답변 정보 매핑
            List<SurveyAnswerResponseDto.QuestionAnswerDto> questionAnswers =
                    survey.getQuestions().stream().map(q -> {
                        SurveyAnswerResponseDto.QuestionAnswerDto qDto = new SurveyAnswerResponseDto.QuestionAnswerDto();
                        qDto.setQuestionId(q.getId());
                        qDto.setName(q.getName());
                        qDto.setDescription(q.getDescription());
                        qDto.setType(q.getType());
                        qDto.setRequired(q.isRequired());
                        qDto.setCandidates(q.getCandidates());
                        // 해당 답변지의 답변 찾기
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

