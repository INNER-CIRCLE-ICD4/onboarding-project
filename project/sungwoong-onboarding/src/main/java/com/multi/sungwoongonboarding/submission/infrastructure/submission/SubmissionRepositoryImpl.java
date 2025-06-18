package com.multi.sungwoongonboarding.submission.infrastructure.submission;

import com.multi.sungwoongonboarding.forms.application.repository.FormRepository;
import com.multi.sungwoongonboarding.forms.domain.Forms;
import com.multi.sungwoongonboarding.options.application.repository.OptionsRepository;
import com.multi.sungwoongonboarding.options.domain.Options;
import com.multi.sungwoongonboarding.questions.application.repository.QuestionRepository;
import com.multi.sungwoongonboarding.questions.domain.Questions;
import com.multi.sungwoongonboarding.submission.domain.Answers;
import com.multi.sungwoongonboarding.submission.domain.Submission;
import com.multi.sungwoongonboarding.submission.application.repository.SubmissionRepository;
import com.multi.sungwoongonboarding.submission.infrastructure.answers.AnswerJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubmissionRepositoryImpl implements SubmissionRepository {

    private final SubmissionJpaRepository submissionJpaRepository;

    private final OptionsRepository optionsRepository;

    private final FormRepository formRepository;

    private final QuestionRepository questionRepository;


    @Override
    @Transactional
    public Submission save(Submission submission) {

        Forms forms = formRepository.findById(submission.getFormId());

        SubmissionJpaEntity submissionJpaEntity = SubmissionJpaEntity.fromDomain(submission);
        submissionJpaEntity.setFormVersion(forms.getVersion());

        for (Answers answer : submission.getAnswers()) {
            AnswerJpaEntity answerJpaEntity = AnswerJpaEntity.fromDomain(answer);
            answerJpaEntity.setSubmissionJpaEntity(submissionJpaEntity);
            Questions question = questionRepository.findById(answer.getQuestionId());

            if (question.getQuestionType().isChoiceType()) {
                Options options = optionsRepository.findById(answer.getOptionId());
                answerJpaEntity.setOptions(options);
            }
        }
        submissionJpaRepository.save(submissionJpaEntity);

        return submissionJpaEntity.toDomain();
    }

    @Override
    public List<Submission> findAll() {
        return submissionJpaRepository.findAll().stream().map(SubmissionJpaEntity::toDomain).toList();
    }

    @Override
    public Submission findById(Long id) {

        Optional<SubmissionJpaEntity> byId = submissionJpaRepository.findById(id);
        return byId.map(SubmissionJpaEntity::toDomain).orElse(null);

    }
}