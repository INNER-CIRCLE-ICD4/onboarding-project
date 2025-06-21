package com.multi.sungwoongonboarding.submission.application.repository;

import com.multi.sungwoongonboarding.submission.domain.Answers;

import java.util.List;

public interface AnswerRepository {

    Answers save(Answers answers);

    List<Answers> saveAll(List<Answers> answers);

    List<Answers> findAll();
}