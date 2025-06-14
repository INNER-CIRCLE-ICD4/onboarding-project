package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import jakarta.persistence.EntityManager;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.repository.SurveyRepositoryCustom;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.QSurvey;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.Survey;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


public class SurveyRepositoryCustomImpl implements SurveyRepositoryCustom {
    private final EntityManager em;
    private final QSurvey qs = QSurvey.survey;
    public SurveyRepositoryCustomImpl(EntityManager em) {
        this.em = em;
    }
    @Override
    @Transactional(readOnly = true)
    public Optional<Survey> findByIdQueryDsl(Long id) {
        Survey survey = new JPAQueryFactory(em)
                .selectFrom(qs)
                .where(qs.id.eq(id))
                .fetchOne();
        return Optional.ofNullable(survey);
    }

    @Override
    @Transactional
    public long updateNameById(Long id, String name) {
        return new JPAUpdateClause(em, qs)
                .set(qs.name, name)
                .where(qs.id.eq(id))
                .execute();
    }
}
