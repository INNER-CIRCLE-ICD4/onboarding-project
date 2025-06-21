package com.icd.onboarding.survey.service;

import com.icd.onboarding.survey.domain.Option;
import com.icd.onboarding.survey.domain.Question;
import com.icd.onboarding.survey.domain.Survey;
import com.icd.onboarding.survey.dto.OptionDto;
import com.icd.onboarding.survey.dto.QuestionDto;
import com.icd.onboarding.survey.dto.SurveyDto;
import com.icd.onboarding.survey.repository.OptionRepository;
import com.icd.onboarding.survey.repository.QuestionRepository;
import com.icd.onboarding.survey.repository.SurveyRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final EntityManager entityManager;

    @Transactional
    public void createSurvey(SurveyDto.Create req) {
        log.info("SurveyService.createSurvey req : {}", req);

        Survey survey = surveyRepository.save(req.toEntity());
        entityManager.flush();

        createQuestions(survey, req.getQuestions());
    }

    private void createQuestions(Survey survey, List<QuestionDto.Create> questionDtos) {
        Integer tempId = 0;
        List<Question> questions = new ArrayList<>();

        Map<Integer, List<OptionDto.Create>> optionMap = new HashMap<>();
        for (QuestionDto.Create qDto : questionDtos) {
            Question question = qDto.toEntity(survey, tempId);
            questions.add(question);
            optionMap.put(tempId, qDto.getOptions());
            tempId++;
        }
        questionRepository.saveAll(questions);

        List<Option> options = new ArrayList<>();
        for (Question q : questions) {
            for (OptionDto.Create oDto : optionMap.get(q.getTempId())) {
                Option option = oDto.toEntity(q);
                options.add(option);
            }
        }
        optionRepository.saveAll(options);
    }

    public SurveyDto.Read getSurvey(Long id) {
        Survey survey = surveyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Survey not found"));
        return SurveyDto.Read.of(survey);
    }

    @Transactional
    public void updateSurvey(Long id, SurveyDto.Update req) {
        Survey savedSurvey = surveyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Survey not found"));

        savedSurvey.updateName(req.getName());
        savedSurvey.updateDescription(req.getDescription());

        if (!isQuestionChanged(savedSurvey.getQuestions(), req.getQuestions())) return;

        log.info("# questions are changed, version will be updated");

        try {
            entityManager.lock(savedSurvey, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            entityManager.flush();
        } catch (OptimisticLockException e) {
            log.error("Optimistic lock exception : {}", e.getMessage());
            throw e;
        }

        log.info("# new questions will be saved");
        createQuestions(savedSurvey, req.getQuestions());

    }

    private boolean isQuestionChanged(List<Question> savedQuestions, List<QuestionDto.Create> reqQuestions) {
        log.info("# check questions or options are changed");

        // 1. Questions 비교 (orderNum 기준)
        Map<Integer, Question> savedQuestionMap = savedQuestions.stream()
                .collect(Collectors.toMap(Question::getOrderNum, Function.identity()));

        Map<Integer, QuestionDto.Create> reqQuestionMap = reqQuestions.stream()
                .collect(Collectors.toMap(QuestionDto.Create::getOrderNum, Function.identity()));

        // 1-1. orderNum 집합 비교
        if (!savedQuestionMap.keySet().equals(reqQuestionMap.keySet())) return true;

        // 1-2. 각 orderNum별 질문 비교
        for (Integer orderNum : savedQuestionMap.keySet()) {
            Question savedQ = savedQuestionMap.get(orderNum);
            QuestionDto.Create reqQ = reqQuestionMap.get(orderNum);

            if (!Objects.equals(savedQ.getName(), reqQ.getName())) return true;
            if (!Objects.equals(savedQ.getDescription(), reqQ.getDescription())) return true;
            if (savedQ.getType() != reqQ.getType()) return true;
            if (savedQ.isRequired() != reqQ.isRequired()) return true;
            if (savedQ.isActive() != reqQ.isActive()) return true;

            // 2. Options 비교 (orderNum 기준)
            Map<Integer, Option> savedOptionMap = savedQ.getOptions().stream()
                    .collect(Collectors.toMap(Option::getOrderNum, Function.identity()));

            Map<Integer, OptionDto.Create> reqOptionMap = reqQ.getOptions().stream()
                    .collect(Collectors.toMap(OptionDto.Create::getOrderNum, Function.identity()));

            // 2-1. option orderNum 집합 비교
            if (!savedOptionMap.keySet().equals(reqOptionMap.keySet())) return true;

            // 2-2. 각 orderNum별 옵션 비교
            for (Integer optionOrderNum : savedOptionMap.keySet()) {
                Option savedO = savedOptionMap.get(optionOrderNum);
                OptionDto.Create reqO = reqOptionMap.get(optionOrderNum);

                if (!Objects.equals(savedO.getContent(), reqO.getContent())) return true;
                if (savedO.isActive() != reqO.isActive()) return true;
            }
        }

        log.info("# questions not changed");
        return false;
    }
}
