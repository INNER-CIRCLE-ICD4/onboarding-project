package com.innercircle.onboarding.question;

import com.innercircle.onboarding.common.exceptions.CommonException;
import com.innercircle.onboarding.common.jpa.QClassService;
import com.innercircle.onboarding.common.response.ResponseStatus;
import com.innercircle.onboarding.question.domain.QQuestion;
import com.innercircle.onboarding.question.domain.Question;
import com.innercircle.onboarding.question.domain.QuestionDto;
import com.innercircle.onboarding.question.domain.QuestionRepository;
import com.querydsl.core.types.Projections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class QuestionService extends QClassService<Question, Long> {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<QuestionDto.SearchOrigin> findByQuestionSeqList(List<Long> questionSeqList) {
        return createQuery()
                .select(
                        Projections.fields(
                                QuestionDto.SearchOrigin.class
                                , qQuestion.seq.as("questionSeq")
                                , qQuestion.content.as("originQuestion")
                                , qQuestion.isRequired))
                .from(qQuestion)
                .where(qQuestion.seq.in(questionSeqList))
                .fetch();
    }

    public void create(Long formSeq, List<QuestionDto.Create> questionList) {

        List<Question> questionEntityList = new ArrayList<>();
        int orderIdx = 0;
        for (QuestionDto.Create question : questionList) {
            questionEntityList.add(question.ofEntity(formSeq, orderIdx++));
        }

        questionRepository.saveAll(questionEntityList);

    }

    public void update(Long questionSeq, QuestionDto.Update updateDto) {

        Question origin
                = Optional.ofNullable(questionRepository.findBySeqAndIsDeletedFalse(questionSeq))
                .orElseThrow(() -> new CommonException(ResponseStatus.NOT_FOUND_DATA));

        List<Question> updateList = new ArrayList<>();
        updateList.add(updateDto.ofEntity(origin));
        updateList.add(updateDto.ofDeleted(origin));

        questionRepository.saveAll(updateList);

    }
}
