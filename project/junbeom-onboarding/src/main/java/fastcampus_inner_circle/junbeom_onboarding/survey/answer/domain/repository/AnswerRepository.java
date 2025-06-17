package fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.repository;

import fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.model.Answer;

import java.util.Optional;

public interface AnswerRepository {

    Answer save(Answer answer);

    Optional<Answer> findById(Long id);
}
