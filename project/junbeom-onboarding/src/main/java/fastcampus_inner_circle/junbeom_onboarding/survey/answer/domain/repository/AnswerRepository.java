package fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.repository;

import java.util.List;
import java.util.Optional;

import fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.model.Answer;

public interface AnswerRepository {

    Answer save(Answer answer);

    Optional<Answer> findById(Long id);

    List<Answer> findByFormId(Long formId);
}
