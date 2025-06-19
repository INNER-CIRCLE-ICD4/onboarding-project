package com.multi.sungwoongonboarding.questions.infrastructure;

import com.multi.sungwoongonboarding.forms.infrastructure.FormsJpaEntity;
import com.multi.sungwoongonboarding.questions.application.repository.QuestionRepository;
import com.multi.sungwoongonboarding.questions.domain.Questions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class QuestionRepositoryImpl implements QuestionRepository {

    private final QuestionJpaRepository questionJpaRepository;

    @Override
    public Questions findById(Long id) {
        QuestionJpaEntity questionJpaEntity = questionJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 질문입니다: " + id));
        return questionJpaEntity.toDomain();
    }


    @Override
    public List<Questions> findRequiredByFormId(Long formId) {
        return questionJpaRepository.findRequiredByFormId(formId).stream()
                .map(QuestionJpaEntity::toDomain).toList();
    }

    @Override
    public Map<Long, Questions> getRequiredQuestionMapByFormId(Long formId) {

        return findRequiredByFormId(formId).stream()
                .collect(Collectors.toMap(Questions::getId, Function.identity()));
    }
}
