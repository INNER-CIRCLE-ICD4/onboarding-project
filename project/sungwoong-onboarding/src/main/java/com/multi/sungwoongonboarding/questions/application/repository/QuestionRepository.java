package com.multi.sungwoongonboarding.questions.application.repository;

import com.multi.sungwoongonboarding.questions.domain.Questions;

public interface QuestionRepository {

    Questions findById(Long id);
}
