package com.multi.sungwoongonboarding.questions.application.repository;

import com.multi.sungwoongonboarding.questions.domain.Questions;

import java.util.List;
import java.util.Map;

public interface QuestionRepository {

    Questions findById(Long id);

    List<Questions> findByFormId(Long formId, Character deleted);

    Map<Long, Questions> getQuestionMapByFormId(Long formId, Character deleted);
}
