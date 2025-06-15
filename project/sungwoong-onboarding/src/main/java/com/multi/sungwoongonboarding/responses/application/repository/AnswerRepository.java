package com.multi.sungwoongonboarding.responses.application.repository;

import com.multi.sungwoongonboarding.responses.domain.Answers;

import java.util.List;

public interface AnswerRepository {

    Answers save(Answers answers);

    List<Answers> saveAll(List<Answers> answers);

    List<Answers> findAll();
}