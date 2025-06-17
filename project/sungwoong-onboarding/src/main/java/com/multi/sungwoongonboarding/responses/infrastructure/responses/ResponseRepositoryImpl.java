package com.multi.sungwoongonboarding.responses.infrastructure.responses;

import com.multi.sungwoongonboarding.forms.application.repository.FormRepository;
import com.multi.sungwoongonboarding.forms.domain.Forms;
import com.multi.sungwoongonboarding.options.application.repository.OptionsRepository;
import com.multi.sungwoongonboarding.options.domain.Options;
import com.multi.sungwoongonboarding.questions.application.repository.QuestionRepository;
import com.multi.sungwoongonboarding.questions.domain.Questions;
import com.multi.sungwoongonboarding.responses.domain.Answers;
import com.multi.sungwoongonboarding.responses.domain.Responses;
import com.multi.sungwoongonboarding.responses.application.repository.ResponseRepository;
import com.multi.sungwoongonboarding.responses.infrastructure.answers.AnswerJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResponseRepositoryImpl implements ResponseRepository {

    private final ResponsesJpaRepository responsesJpaRepository;

    private final OptionsRepository optionsRepository;

    private final FormRepository formRepository;

    private final QuestionRepository questionRepository;


    @Override
    @Transactional
    public Responses save(Responses responses) {

        Forms forms = formRepository.findById(responses.getFormId());

        ResponseJpaEntity responseJpaEntity = ResponseJpaEntity.fromDomain(responses);
        responseJpaEntity.setFormVersion(forms.getVersion());

        for (Answers answer : responses.getAnswers()) {
            AnswerJpaEntity answerJpaEntity = AnswerJpaEntity.fromDomain(answer);
            answerJpaEntity.setResponseJpaEntity(responseJpaEntity);
            Questions question = questionRepository.findById(answer.getQuestionId());

            if (question.getQuestionType().isChoiceType()) {
                Options options = optionsRepository.findById(answer.getOptionId());
                answerJpaEntity.setOptions(options);
            }
        }
        responsesJpaRepository.save(responseJpaEntity);

        return responseJpaEntity.toDomain();
    }

    @Override
    public List<Responses> findAll() {
        return responsesJpaRepository.findAll().stream().map(ResponseJpaEntity::toDomain).toList();
    }

    @Override
    public Responses findById(Long id) {

        Optional<ResponseJpaEntity> byId = responsesJpaRepository.findById(id);
        return byId.map(ResponseJpaEntity::toDomain).orElse(null);

    }
}