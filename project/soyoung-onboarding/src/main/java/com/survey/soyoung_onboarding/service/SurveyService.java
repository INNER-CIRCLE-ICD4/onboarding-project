package com.survey.soyoung_onboarding.service;

import com.survey.soyoung_onboarding.common.exception.ApiError;
import com.survey.soyoung_onboarding.common.exception.ApiException;
import com.survey.soyoung_onboarding.dto.AnswerDto;
import com.survey.soyoung_onboarding.dto.QuestionDto;
import com.survey.soyoung_onboarding.dto.ReplyDto;
import com.survey.soyoung_onboarding.dto.SurveyDto;
import com.survey.soyoung_onboarding.entity.*;
import com.survey.soyoung_onboarding.repository.QuestionOptionRepository;
import com.survey.soyoung_onboarding.repository.QuestionRepository;
import com.survey.soyoung_onboarding.repository.ReplyRepository;
import com.survey.soyoung_onboarding.repository.SurveyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SurveyService {

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private QuestionOptionRepository questionOptionRepository;

    public void createSurvey(SurveyDto dto) {
        Survey survey = createSurveyEntity(dto);
        survey.setQuestions(createQuestionEntities(dto.getQuestions(), survey));
        surveyRepository.save(survey);
    }

    private Survey createSurveyEntity(SurveyDto dto) {
        Survey survey = new Survey();
        survey.setTitle(dto.getTitle());
        survey.setDescription(dto.getDescription());
        survey.setReg_date(new Date());
        survey.setUpdate_date(new Date());
        return survey;
    }

    private List<Question> createQuestionEntities(List<QuestionDto> questionDtos, Survey survey) {
        return questionDtos.stream().map(dto -> {
            Question q = new Question();
            q.setTitle(dto.getTitle());
            q.setDescription(dto.getDescription());
            q.setType(dto.getType());
            q.setRequired(dto.isRequired());
            q.setOptions(dto.getOptions());
            q.setStatus("0"); // 원본
            q.setSurvey(survey); // 연관관계 설정
            return q;
        }).collect(Collectors.toList());
    }

    @Transactional
    public void updateSurvey(SurveyDto dto) {
        Survey survey = surveyRepository.findById(UUID.fromString(dto.getId()))
                .orElseThrow(() -> new ApiException(ApiError.NOT_FOUND));

        survey.setTitle(dto.getTitle());
        survey.setDescription(dto.getDescription());
        survey.setUpdate_date(new Date());

        // 기존 설문 항목 상태 변경
        survey.getQuestions().forEach(q -> q.setStatus("2"));

        // 수정된 설문 항목 등록
        List<Question> newQuestions = createQuestionEntities(dto.getQuestions(), survey);
        survey.getQuestions().addAll(newQuestions);

        surveyRepository.save(survey);
    }

    @Transactional
    public void submitSurveyReply(ReplyDto dto) {
        Survey survey = surveyRepository.findById(UUID.fromString(dto.getSurvey_id()))
                .orElseThrow(() -> new ApiException(ApiError.NOT_FOUND));

        Reply reply = new Reply();
        reply.setSurvey(survey);
        reply.setName(dto.getName());
        reply.setEmail(dto.getEmail());
        reply.setReg_date(new Date());
        reply.setAnswers(this.createAnswerEntities(dto.getAnswers(), reply));

        replyRepository.save(reply);
    }

    private List<Answer> createAnswerEntities(List<AnswerDto> answerDtos, Reply reply) {
        return answerDtos.stream().map(answerDto -> {
            Question question = questionRepository.findById(answerDto.getQuestion_id())
                    .orElseThrow(() -> new ApiException(ApiError.NOT_FOUND));

            Answer answer = new Answer();
            answer.setQuestion(question);
            answer.setAnswerText(answerDto.getAnswer_text());
            answer.setReply(reply);

            boolean is_valid_option_ids = answerDto.getSelected_option_ids() != null && !answerDto.getSelected_option_ids().isEmpty();
            if (!is_valid_option_ids) return answer;
            
            // 옵션 선택형 질문인 경우
            AnswerOption answerOption = new AnswerOption();
            answerOption.setAnswer(answer);
            List<QuestionOption> selectedQuestionOptions = answerDto.getSelected_option_ids().stream()
                    .map(optionId -> questionOptionRepository.findById(optionId)
                            .orElseThrow(() -> new ApiException(ApiError.NOT_FOUND)))
                    .collect(Collectors.toList());

            answerOption.setQuestion_options(selectedQuestionOptions);
            
            answer.setSelected_options(List.of(answerOption));
            return answer;

        }).collect(Collectors.toList());
    }

    public List<ReplyDto> getSurveyReplies(UUID surveyId) {
        // 순환참조 방지를 위해 DTO 로 변환해서 리턴해야함
        List<Reply> replies = replyRepository.findAllBySurveyId(surveyId);
        return replies.stream().map(ReplyDto::convert).collect(Collectors.toList());
    }
}
