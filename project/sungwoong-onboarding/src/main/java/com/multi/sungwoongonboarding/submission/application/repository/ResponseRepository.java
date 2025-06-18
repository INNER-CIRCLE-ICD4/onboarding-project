package com.multi.sungwoongonboarding.submission.application.repository;

import com.multi.sungwoongonboarding.submission.domain.Responses;

import java.util.List;

public interface ResponseRepository {

    Responses save(Responses responses);

    List<Responses> findAll();

    Responses findById(Long id);
}