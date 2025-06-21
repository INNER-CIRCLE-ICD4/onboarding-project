package com.multi.sungwoongonboarding.submission.infrastructure.submission;

import com.multi.sungwoongonboarding.common.entity.BaseEntity;
import com.multi.sungwoongonboarding.options.domain.Options;
import com.multi.sungwoongonboarding.submission.domain.Answers;
import com.multi.sungwoongonboarding.submission.domain.SelectedOption;
import com.multi.sungwoongonboarding.submission.domain.Submission;
import com.multi.sungwoongonboarding.submission.infrastructure.answers.AnswerJpaEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "submissions")
public class SubmissionJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submission_id")
    private Long id;

    @Column(name = "form_id", nullable = false)
    private Long formId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "form_version", nullable = false)
    private int formVersion;

    @OneToMany(mappedBy = "submissionJpaEntity", cascade = {CascadeType.PERSIST})
    private List<AnswerJpaEntity> answers = new ArrayList<>();

    public static SubmissionJpaEntity fromDomain(Submission submission) {
        SubmissionJpaEntity submissionJpaEntity = new SubmissionJpaEntity();
        submissionJpaEntity.id = submission.getId();
        submissionJpaEntity.formVersion = submission.getFormVersion();
        submissionJpaEntity.formId = submission.getFormId();
        submissionJpaEntity.userId = submission.getUserId();
        return submissionJpaEntity;
    }

    public static SubmissionJpaEntity fromDomainWithFormVersion(Submission submission,  int formVersion) {

        SubmissionJpaEntity submissionJpaEntity = fromDomain(submission);
        submissionJpaEntity.formVersion = formVersion;

        for (Answers answer : submission.getAnswers()) {
            AnswerJpaEntity.fromDomainsAndMapping(answer, submissionJpaEntity);
        }

        return submissionJpaEntity;
    }

    public Submission toDomain() {
        return Submission.builder()
                .id(this.id)
                .formId(this.formId)
                .userId(this.userId)
                .answers(this.answers.stream().map(AnswerJpaEntity::toDomain).toList())
                .createdAt(this.getCreatedAt())
                .build();
    }

    public Submission toDomainWithOptionMap(Map<Long, Options> optionsMap) {


        Map<Long, List<AnswerJpaEntity>> groupByQuestionId = this.answers.stream().collect(Collectors.groupingBy(AnswerJpaEntity::getQuestionId));


        List<Answers> answersList = groupByQuestionId.keySet().stream().map(questionId -> {
            List<AnswerJpaEntity> answers = groupByQuestionId.get(questionId);

            if (answers.size() == 1 && answers.get(0).getAnswerText() != null && !answers.get(0).getAnswerText().isBlank()) {
                return Answers.builder()
                        .submissionId(this.id)
                        .questionId(questionId)
                        .answerText(answers.get(0).getAnswerText())
                        .build();
            } else {
                return Answers.builder()
                        .submissionId(this.id)
                        .questionId(questionId)
                        .selectedOptions(answers.stream().map(a -> SelectedOption.builder().optionId(a.getOptionId()).optionText(optionsMap.get(a.getOptionId()).getOptionText()).build()).toList())
                        .build();
            }

        }).toList();



        return Submission.builder()
                .id(this.id)
                .formId(this.formId)
                .userId(this.userId)
                .answers(answersList)
                .createdAt(this.getCreatedAt())
                .build();
    }
}