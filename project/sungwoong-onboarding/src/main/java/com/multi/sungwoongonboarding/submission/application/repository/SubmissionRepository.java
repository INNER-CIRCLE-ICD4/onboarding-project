package com.multi.sungwoongonboarding.submission.application.repository;

import com.multi.sungwoongonboarding.submission.domain.Submission;

import java.util.List;

public interface SubmissionRepository {

    Submission save(Submission submission);

    List<Submission> findAll();

    Submission findById(Long id);
}