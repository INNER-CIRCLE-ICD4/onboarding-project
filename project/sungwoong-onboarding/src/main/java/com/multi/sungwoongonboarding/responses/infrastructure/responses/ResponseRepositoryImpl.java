package com.multi.sungwoongonboarding.responses.infrastructure.responses;

import com.multi.sungwoongonboarding.questions.infrastructure.QuestionJpaRepository;
import com.multi.sungwoongonboarding.responses.domain.Responses;
import com.multi.sungwoongonboarding.responses.application.repository.ResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResponseRepositoryImpl implements ResponseRepository {

    private final ResponsesJpaRepository responsesJpaRepository;

    @Override
    @Transactional
    public Responses save(Responses responses) {
        ResponseJpaEntity responseJpaEntity = ResponseJpaEntity.fromDomain(responses);
        responsesJpaRepository.save(responseJpaEntity);
        return responseJpaEntity.toDomain();
    }

    @Override
    public List<Responses> findAll() {
        return responsesJpaRepository.findAll().stream().map(ResponseJpaEntity::toDomain).toList();
    }
}