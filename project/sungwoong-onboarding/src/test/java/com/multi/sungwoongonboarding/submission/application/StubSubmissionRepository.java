package com.multi.sungwoongonboarding.submission.application;


import com.multi.sungwoongonboarding.submission.application.repository.SubmissionRepository;
import com.multi.sungwoongonboarding.submission.domain.Answers;
import com.multi.sungwoongonboarding.submission.domain.Submission;

import java.util.List;

public class StubSubmissionRepository implements SubmissionRepository {

    @Override
    public Submission save(Submission submission) {
        return null;
    }

    @Override
    public List<Submission> findAll() {
        return List.of();
    }

    @Override
    public Submission findById(Long id) {
        return null;
    }

    @Override
    public List<Submission> findByFormId(Long formId) {


        return List.of(
                Submission.builder()
                        .id(1L)
                        .formId(1L)
                        .formTitle("햄버거 선호도 조사")
                        .formVersion(1)
                        .formDescription("햄버거를 좋아하시나요?")
                        .answers(
                                List.of(
                                        Answers.builder()
                                                .id(1L)
                                                .submissionId(1L)
                                                .questionId(1L)
                                                .answerText("햄버거는 완전식품에 맛있다.")
                                                .build(),

                                        Answers.builder()
                                                .id(2L)
                                                .submissionId(1L)
                                                .questionId(2L)
                                                .optionId(1L)
                                                .answerText("맥도날드")
                                                .build()
                                )
                        )
                        .build(),

                Submission.builder()
                        .id(2L)
                        .formId(1L)
                        .formTitle("햄버거 선호도 조사")
                        .formVersion(1)
                        .formDescription("햄버거를 좋아하시나요?")
                        .answers(
                                List.of(
                                        Answers.builder()
                                                .id(3L)
                                                .submissionId(2L)
                                                .questionId(1L)
                                                .answerText("햄버거는 완전식품에 맛있다.")
                                                .build(),

                                        Answers.builder()
                                                .id(4L)
                                                .submissionId(2L)
                                                .questionId(2L)
                                                .optionId(2L)
                                                .answerText("버거킹")
                                                .build()
                                )
                        )
                        .build()
        );
    }

}
