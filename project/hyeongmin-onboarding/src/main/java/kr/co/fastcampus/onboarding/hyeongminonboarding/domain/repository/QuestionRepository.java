package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.repository;

import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long>, QuestionRepositoryCustom {
    List<Question> findAllBySurveyId(Long surveyId);
}
