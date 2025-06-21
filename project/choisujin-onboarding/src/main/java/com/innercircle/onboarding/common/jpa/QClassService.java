package com.innercircle.onboarding.common.jpa;

import com.innercircle.onboarding.answer.domain.QAnswer;
import com.innercircle.onboarding.form.domain.QForm;
import com.innercircle.onboarding.question.domain.QQuestion;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
@NoArgsConstructor
public abstract class QClassService<T, ID extends Serializable> {

    @PersistenceContext
    protected EntityManager entityManager;

    protected JpaBaseRepository<T, ID> repository;

    protected QForm qForm = QForm.form;
    protected QQuestion qQuestion = QQuestion.question;
    protected QAnswer qAnswer = QAnswer.answer;

    public QClassService(JpaBaseRepository<T, ID> repository) {
        this.repository = repository;
    }

    protected JPAQuery<T> createQuery() {
        return new JPAQuery<>(entityManager);
    }
}

