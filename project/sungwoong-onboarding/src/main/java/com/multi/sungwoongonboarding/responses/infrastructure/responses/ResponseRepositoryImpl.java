package com.multi.sungwoongonboarding.responses.infrastructure.responses;

import com.multi.sungwoongonboarding.options.application.repository.OptionsRepository;
import com.multi.sungwoongonboarding.options.domain.Options;
import com.multi.sungwoongonboarding.questions.application.repository.QuestionRepository;
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

    private final QuestionRepository questionRepository;

    @Override
    @Transactional
    public Responses save(Responses responses) {

        ResponseJpaEntity responseJpaEntity = ResponseJpaEntity.fromDomain(responses);

        for (Answers answer : responses.getAnswers()) {

            answer.setOriginalQuestion(questionRepository.findById(answer.getQuestionId()));

            AnswerJpaEntity answerJpaEntity = AnswerJpaEntity.fromDomain(answer);

            answerJpaEntity.setResponseJpaEntity(responseJpaEntity);

            if (answer.getOriginalQuestionType().isChoiceType()) {
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