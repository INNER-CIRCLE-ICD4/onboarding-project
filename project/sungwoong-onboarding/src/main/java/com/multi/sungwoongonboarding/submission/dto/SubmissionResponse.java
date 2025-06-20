package com.multi.sungwoongonboarding.submission.dto;

import com.multi.sungwoongonboarding.forms.domain.Forms;
import com.multi.sungwoongonboarding.questions.domain.Questions;
import com.multi.sungwoongonboarding.submission.domain.Answers;
import com.multi.sungwoongonboarding.submission.domain.Submission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class SubmissionResponse {

    private Long id;
    private Long formId;
    private String userId;
    private String formTitle;
    private String formDescription;
    private int formVersion;
    private List<QuestionAnswerResponse> questionAnswer;

    public static SubmissionResponse fromDomain(Submission submission) {
        return getSubmissionResponseBuilder(submission)
                .build();
    }

    public static SubmissionResponse fromDomainWithForm(Submission submission, Forms form, Map<Long, Questions> questionMap) {
        return getSubmissionResponseBuilder(submission)
                .formTitle(form.getTitle())
                .formDescription(form.getDescription())
                .questionAnswer(
                        submission.getAnswers().stream().map(answer -> QuestionAnswerResponse.fromQuestionWithAnswers(questionMap.get(answer.getQuestionId()), answer)).toList()
                )
                .build();
    }


    private static SubmissionResponseBuilder getSubmissionResponseBuilder(Submission submission) {
        return SubmissionResponse.builder()
                .id(submission.getId())
                .formId(submission.getFormId())
                .userId(submission.getUserId())
                .formTitle(submission.getFormTitle())
                .formDescription(submission.getFormDescription())
                .formVersion(submission.getFormVersion());
    }
}
