package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.repository;

import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.Question;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionOptionRepository extends JpaRepository<QuestionOption, Long>, QuestionRepositoryCustom {
    List<QuestionOption> findByQuestionId(Long id);

    void deleteAllByQuestion(Question question);

    void deleteAllByQuestionIn(List<Question> toDelete);

    List<QuestionOption> findAllByQuestionIn(List<Question> questions);
}
