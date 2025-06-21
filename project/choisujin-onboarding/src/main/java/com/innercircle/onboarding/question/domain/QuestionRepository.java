package com.innercircle.onboarding.question.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    Question findBySeqAndIsDeletedFalse(Long seq);

}
