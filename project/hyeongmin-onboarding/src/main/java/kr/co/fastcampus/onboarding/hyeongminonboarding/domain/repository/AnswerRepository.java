package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.repository;

import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.Answer;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.SurveyResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> ,AnswerRepositoryCustom {
    List<Answer> findAllByResponseIn(List<SurveyResponse> responses);
}
