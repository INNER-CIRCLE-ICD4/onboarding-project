package fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.entity.AnswerJpaEntity;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.mapper.AnswerToDomainMapper;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.mapper.AnswerToEntityMapper;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.repository.AnswerJpaRepository;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.model.Answer;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AnswerRepositoryImpl implements AnswerRepository {

    private final AnswerJpaRepository answerJpaRepository;

    @Override
    public Answer save(Answer answer) {
        AnswerJpaEntity entity = AnswerToEntityMapper.toJpaEntity(answer);
        return AnswerToDomainMapper.toDomain(answerJpaRepository.save(entity));
    }

    @Override
    public Optional<Answer> findById(Long id) {
        return answerJpaRepository.findById(id)
                .map(AnswerToDomainMapper::toDomain);
    }

    @Override
    public List<Answer> findByFormId(Long formId) {
        return answerJpaRepository.findByFormId(formId)
                .stream()
                .map(AnswerToDomainMapper::toDomain)
                .toList();
    }
}
