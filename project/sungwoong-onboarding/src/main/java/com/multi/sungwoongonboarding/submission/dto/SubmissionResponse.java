package com.multi.sungwoongonboarding.submission.dto;

import com.multi.sungwoongonboarding.forms.domain.Forms;
import com.multi.sungwoongonboarding.submission.domain.Answers;
import com.multi.sungwoongonboarding.submission.domain.Submission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

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

    public static SubmissionResponse fromDomainWithForm(Submission submission, Forms form) {
        return getSubmissionResponseBuilder(submission)
                .formTitle(form.getTitle())
                .formDescription(form.getDescription())
                .questionAnswer(
                        form.getQuestions().stream().map(questions -> {
                            List<Answers> answersByQuestionId = submission.getAnswersByQuestionId(questions.getId());
                            return QuestionAnswerResponse.fromQuestionWithAnswers(questions, answersByQuestionId);
                        }).toList()
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
