package com.multi.sungwoongonboarding.responses.application.repository;

import com.multi.sungwoongonboarding.responses.domain.Responses;

import java.util.List;

public interface ResponseRepository {

    Responses save(Responses responses);

    List<Responses> findAll();
}