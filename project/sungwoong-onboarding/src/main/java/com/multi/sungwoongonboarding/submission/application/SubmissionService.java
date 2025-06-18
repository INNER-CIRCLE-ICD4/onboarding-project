package com.multi.sungwoongonboarding.submission.application;

import com.multi.sungwoongonboarding.submission.domain.Submission;
import com.multi.sungwoongonboarding.submission.application.repository.SubmissionRepository;
import com.multi.sungwoongonboarding.submission.dto.SubmissionCreateRequest;
import com.multi.sungwoongonboarding.submission.dto.SubmissionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepository;

    @Transactional
    public SubmissionResponse submitResponse(SubmissionCreateRequest request) {

        // 응답 저장
        Submission response = request.toDomain();
        Submission result = submissionRepository.save(response);
        return SubmissionResponse.fromDomain(result);
    }
}
