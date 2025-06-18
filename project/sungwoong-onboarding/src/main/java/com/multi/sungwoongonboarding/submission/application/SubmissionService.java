package com.multi.sungwoongonboarding.submission.application;

import com.multi.sungwoongonboarding.forms.application.repository.FormRepository;
import com.multi.sungwoongonboarding.forms.domain.Forms;
import com.multi.sungwoongonboarding.submission.domain.Submission;
import com.multi.sungwoongonboarding.submission.application.repository.SubmissionRepository;
import com.multi.sungwoongonboarding.submission.dto.SubmissionCreateRequest;
import com.multi.sungwoongonboarding.submission.dto.SubmissionResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Builder
@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final FormRepository formRepository;

    public SubmissionResponse submitResponse(SubmissionCreateRequest request) {
        // todo 의존성 개선 예정
        // 응답 저장
        Submission response = request.toDomain();
        Submission result = submissionRepository.save(response);
        return SubmissionResponse.fromDomain(result);
    }

    public List<SubmissionResponse> getSubmissionByFormId(Long formId) {

        // todo 응답 형식 개선 필요
        Forms form = formRepository.findById(formId);
        List<Submission> submissions = submissionRepository.findByFormId(formId);

        // 조회된 제출지를 순회하며 설문지 버전을 찾은 후 응답 형식으로 변환해준다.
        return submissions.stream().map(
                        submission -> SubmissionResponse.fromDomainWithForm(submission, form.findFormVersion(submission.getFormVersion())))
                .toList();
    }

}
