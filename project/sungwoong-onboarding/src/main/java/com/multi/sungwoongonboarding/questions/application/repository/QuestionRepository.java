package com.multi.sungwoongonboarding.questions.application.repository;

import com.multi.sungwoongonboarding.questions.domain.Questions;

import java.util.List;
import java.util.Map;

public interface QuestionRepository {

    Questions findById(Long id);

    List<Questions> findRequiredByFormId(Long formId);

    Map<Long, Questions> getRequiredQuestionMapByFormId(Long formId);
}
