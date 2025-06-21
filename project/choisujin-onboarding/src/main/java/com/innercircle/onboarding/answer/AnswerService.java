package com.innercircle.onboarding.answer;

import com.innercircle.onboarding.answer.domain.Answer;
import com.innercircle.onboarding.answer.domain.AnswerDto;
import com.innercircle.onboarding.answer.domain.AnswerRepository;
import com.innercircle.onboarding.common.exceptions.CommonException;
import com.innercircle.onboarding.common.jpa.QClassService;
import com.innercircle.onboarding.common.response.ResponseStatus;
import com.innercircle.onboarding.question.QuestionService;
import com.innercircle.onboarding.question.domain.QuestionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AnswerService extends QClassService<Answer, Long> {

    private final QuestionService questionService;
    private final AnswerRepository answerRepository;

    public AnswerService(QuestionService questionService, AnswerRepository answerRepository) {
        this.questionService = questionService;
        this.answerRepository = answerRepository;
    }

    public void create(List<AnswerDto.Create> answerList) {

        if (ObjectUtils.isEmpty(answerList) || answerList.size() > 10) {
            throw new CommonException(ResponseStatus.ANSWER_COUNT_MISMATCH);
        }

        List<Long> questionSeqList = answerList.stream()
                .map(AnswerDto.Create::getQuestionSeq)
                .toList();

        List<QuestionDto.SearchOrigin> originQList = questionService.findByQuestionSeqList(questionSeqList);
        if (originQList.size() != questionSeqList.size()) {
            throw new CommonException(ResponseStatus.NOT_MATCH_QUESTION_ANSWER_COUNT);
        }

        Map<Long, String> answerMap = answerList.stream()
                .collect(Collectors.toMap(AnswerDto.Create::getQuestionSeq, AnswerDto.Create::getContent));

        List<Answer> answerEntityList
                = originQList.stream()
                .map(question -> {
                    if (question.isRequired() && !answerMap.containsKey(question.getQuestionSeq())) {
                        throw new CommonException(ResponseStatus.REQUIRED_QUESTION_NOT_ANSWERED);
                    }
                    return question.ofEntity(answerMap.get(question.getQuestionSeq()));
                })
                .collect(Collectors.toList());


        answerRepository.saveAll(answerEntityList);
    }

}
