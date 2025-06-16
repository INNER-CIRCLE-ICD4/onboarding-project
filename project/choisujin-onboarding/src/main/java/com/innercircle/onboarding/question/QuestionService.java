package com.innercircle.onboarding.question;

import com.innercircle.onboarding.common.exceptions.CommonException;
import com.innercircle.onboarding.common.response.ResponseStatus;
import com.innercircle.onboarding.question.domain.Question;
import com.innercircle.onboarding.question.domain.QuestionDto;
import com.innercircle.onboarding.question.domain.QuestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public void create(Long formSeq, List<QuestionDto.Create> questionList){

        List<Question> questionEntityList = new ArrayList<>();
        int orderIdx = 0;
        for (QuestionDto.Create question : questionList) {
            questionEntityList.add(question.ofEntity(formSeq, orderIdx++));
        }

        questionRepository.saveAll(questionEntityList);

    }

    public void update(Long questionSeq, QuestionDto.Update updateDto){

        Question origin
                = Optional.ofNullable(questionRepository.findBySeqAndIsDeletedFalse(questionSeq))
                .orElseThrow(() -> new CommonException(ResponseStatus.NOT_FOUND_DATA));

        List<Question> updateList = new ArrayList<>();
        updateList.add(updateDto.ofEntity(origin));
        updateList.add(updateDto.ofDeleted(origin));

        questionRepository.saveAll(updateList);

    }
}
