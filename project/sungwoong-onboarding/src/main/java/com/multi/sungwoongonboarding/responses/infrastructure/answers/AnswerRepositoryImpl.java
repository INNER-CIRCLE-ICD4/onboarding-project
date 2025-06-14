package com.multi.sungwoongonboarding.responses.infrastructure.answers;

import com.multi.sungwoongonboarding.responses.domain.Answers;
import com.multi.sungwoongonboarding.responses.application.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnswerRepositoryImpl implements AnswerRepository {

    private final AnswersJpaRepository answersJpaRepository;

    @Override
    @Transactional
    public Answers save(Answers answers) {
        AnswerJpaEntity answerJpaEntity = AnswerJpaEntity.fromDomain(answers);
        answersJpaRepository.save(answerJpaEntity);
        return answerJpaEntity.toDomain();
    }

    @Override
    @Transactional
    public List<Answers> saveAll(List<Answers> answers) {
        List<AnswerJpaEntity> answersJpaEntities = answers.stream()
                .map(AnswerJpaEntity::fromDomain)
                .collect(Collectors.toList());
        
        List<AnswerJpaEntity> savedEntities = answersJpaRepository.saveAll(answersJpaEntities);
        return savedEntities.stream().map(AnswerJpaEntity::toDomain).toList();
    }

    @Override
    public List<Answers> findAll() {
        return answersJpaRepository.findAll().stream().map(AnswerJpaEntity::toDomain).toList();
    }
}