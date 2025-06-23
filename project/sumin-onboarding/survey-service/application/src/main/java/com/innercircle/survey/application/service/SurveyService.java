package com.innercircle.survey.application.service;


import com.innercircle.survey.common.dto.QuestionDto;
import com.innercircle.survey.common.dto.QuestionUpdateDto;
import com.innercircle.survey.common.dto.SurveyCreateDto;
import com.innercircle.survey.common.dto.SurveyUpdateDto;
import com.innercircle.survey.domain.entity.Question;
import com.innercircle.survey.domain.entity.QuestionOption;
import com.innercircle.survey.domain.entity.Survey;
import com.innercircle.survey.domain.repository.SurveyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SurveyService {
    private final SurveyRepository surveyRepository;

    public UUID createSurvey(SurveyCreateDto request) {
        Survey survey = Survey.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .build();

        for (QuestionDto q : request.getQuestions()) {
            Question question = Question.builder()
                    .title(q.getTitle())
                    .description(q.getDescription())
                    .type(q.getType())
                    .required(q.isRequired())
                    .survey(survey)
                    .build();

            if (q.getOptions() != null) {
                for (String option : q.getOptions()) {
                    QuestionOption qo = new QuestionOption(option, question);
                    question.getOptions().add(qo);
                }
            }

            survey.getQuestions().add(question);
        }

        surveyRepository.save(survey);
        return survey.getId(); // UUID 기준
    }

    public void updateSurvey(UUID surveyId, SurveyUpdateDto dto) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 설문입니다."));

        //1.타이틀 및 설명 수정
        survey.updateTitle(dto.getTitle());
        survey.updateDescription(dto.getDescription());

        // 2. 기존 질문 목록
        Map<UUID, Question> existingQuestions = survey.getQuestions().stream()
                .collect(Collectors.toMap(Question::getId, Function.identity()));

        // 3. 새로 들어온 질문 목록 처리
        for (QuestionUpdateDto questionDto : dto.getQuestions()) {
            if (questionDto.getQuestionId() == null) {
                //새 질문 추가
                Question newQuestion =Question.builder()
                        .title(questionDto.getTitle())
                        .description(questionDto.getDescription())
                        .type(questionDto.getType())
                        .required(questionDto.isRequired())
                        .survey(survey) // 필요 시
                        .build();
                //옵션 리스트 추가
                if (questionDto.getOptions() != null) {
                    for (String option : questionDto.getOptions()) {
                        QuestionOption qo = new QuestionOption(option, newQuestion);
                        newQuestion.getOptions().add(qo);
                    }
                }
                survey.addQuestion(newQuestion);
            } else {
                //기존 질문 수정
                Question existing = existingQuestions.remove(questionDto.getQuestionId());
                if (existing != null) {
                    existing.update(questionDto);
                }
            }
        }

        // 4. 삭제된 질문 제거 (남은 것들은 요청에서 빠졌다는 뜻)
        for (Question q : existingQuestions.values()) {
            survey.removeQuestion(q); // 필요 시 soft-delete
        }
    }
}
