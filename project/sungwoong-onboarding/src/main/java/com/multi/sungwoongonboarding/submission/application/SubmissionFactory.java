package com.multi.sungwoongonboarding.submission.application;

import com.multi.sungwoongonboarding.forms.application.repository.FormRepository;
import com.multi.sungwoongonboarding.forms.domain.Forms;
import com.multi.sungwoongonboarding.options.domain.Options;
import com.multi.sungwoongonboarding.questions.application.repository.QuestionRepository;
import com.multi.sungwoongonboarding.questions.domain.Questions;
import com.multi.sungwoongonboarding.submission.application.repository.SubmissionRepository;
import com.multi.sungwoongonboarding.submission.domain.Answers;
import com.multi.sungwoongonboarding.submission.domain.SelectedOption;
import com.multi.sungwoongonboarding.submission.domain.Submission;
import com.multi.sungwoongonboarding.submission.dto.AnswerCreateRequest;
import com.multi.sungwoongonboarding.submission.dto.SubmissionCreateRequest;
import com.multi.sungwoongonboarding.submission.dto.SubmissionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SubmissionFactory {

    private final QuestionRepository questionRepository;
    private final SubmissionRepository submissionRepository;
    private final FormRepository formRepository;

    /**
     * Submission : CreateRequest -> Domain 변환
     * @param submissionCreateRequest
     * @return
     */
    public Submission createSubmission(SubmissionCreateRequest submissionCreateRequest) {

        Map<Long, Questions> questionMap = questionRepository.getQuestionMapByFormId(submissionCreateRequest.getFormId(), null);

        validateSubmissionRequest(submissionCreateRequest, questionMap);

        List<Answers> answers = createAnswersFromRequests(submissionCreateRequest.getAnswerCreateRequests(), questionMap);

        return submissionCreateRequest.toDomainForSave(answers);
    }


    /**
     * Submission 조회 기능
     * @param formId
     * @param questionText
     * @param answerText
     * @return
     */
    public List<SubmissionResponse> retrieveSubmission(Long formId, String questionText, String answerText) {
        Forms form = formRepository.findById(formId);
        Map<Long, Questions> questionMapByFormId = questionRepository.getQuestionMapByFormId(formId, null);
        List<Submission> submissions = submissionRepository.findByFormId(formId, questionText, answerText);

        return submissions.stream().map(
                        submission -> SubmissionResponse.fromDomainWithForm(submission, form.findFormVersion(submission.getFormVersion()), questionMapByFormId))
                .toList();
    }



    /**
     * AnswerList : CreateRequest -> Domain 변환
     * @param answerCreateRequests
     * @param questionMap
     * @return
     */
    private List<Answers> createAnswersFromRequests(List<AnswerCreateRequest> answerCreateRequests, Map<Long, Questions> questionMap) {

        return answerCreateRequests.stream()
                .map(answerCreateRequest -> createSingleAnswer(answerCreateRequest, questionMap))
                .toList();
    }


    /**
     * Answer : CreateRequest -> Domain 변환
     * @param answerCreateRequest
     * @param questionMap
     * @return
     */
    private Answers createSingleAnswer(AnswerCreateRequest answerCreateRequest, Map<Long, Questions> questionMap) {
        Questions question = questionMap.get(answerCreateRequest.getQuestionId());

        if (question == null) throw new IllegalArgumentException("Question not found");

        // 질문 유형에 따라 답변 객체를 만들어 준다.
        if (question.getQuestionType().isChoiceType()) {
            return createChoiceAnswer(answerCreateRequest);
        } else {
            return createTextAnswer(answerCreateRequest);
        }
    }


    // 선택 질문에 대한 답변
    private Answers createChoiceAnswer(AnswerCreateRequest answerCreateRequest) {
        return answerCreateRequest.toDomainForSave(
                answerCreateRequest.getOptionIds().stream().map(SelectedOption::ofOptionId).toList()
        );
    }

    // 텍스트 질문에 대한 답변
    private Answers createTextAnswer(AnswerCreateRequest answerCreateRequest) {
        return answerCreateRequest.toDomainForSave(null);
    }

    /**
     * 전체 제출 검증
     * @param submissionCreateRequest
     * @param questionMap
     */
    private void validateSubmissionRequest(SubmissionCreateRequest submissionCreateRequest, Map<Long, Questions> questionMap) {

        // 1) 답변의 질문이 설문지에 포함됐는가?
        validateQuestionExistInForm(submissionCreateRequest, questionMap);

        // 2) 중복 질문 검증
        validateNoDuplicateQuestions(submissionCreateRequest);

        // 3) 중복 옵션 선택 검증
        validateNoDuplicateOptions(submissionCreateRequest);

        // 4) 질문 타입별 답변 검증
        validateAnswerIntegrity(submissionCreateRequest, questionMap);

        // 5) 필수 질문에 대한 답변이 왔는가?
        validateRequiredQuestions(submissionCreateRequest, questionMap);

    }

    /**
     * 1) 답변의 질문이 설문지에 포함됐는가?
     * @param submissionCreateRequest
     * @param questionMap
     */
    private void validateQuestionExistInForm(SubmissionCreateRequest submissionCreateRequest, Map<Long, Questions> questionMap) {

        Set<Long> submissionQuestionIdSet = submissionCreateRequest.getAnswerCreateRequests().stream()
                .map(AnswerCreateRequest::getQuestionId)
                .collect(Collectors.toSet());


        Set<Long> notDeleteQuestions = questionMap.keySet().stream().filter(questionId -> !questionMap.get(questionId).isDeleted()).collect(Collectors.toSet());

        Set<Long> invalidQuestionIds = submissionQuestionIdSet.stream()
                .filter(questionId -> !notDeleteQuestions.contains(questionId))
                .collect(Collectors.toSet());

        if (!invalidQuestionIds.isEmpty()) {
            throw new IllegalArgumentException("해당 설문지에 없는 질문들입니다 : " + invalidQuestionIds);
        }
    }

    /**
     * 2) 중복 답변 검증
     * @param submissionCreateRequest
     */
    private void validateNoDuplicateQuestions(SubmissionCreateRequest submissionCreateRequest) {
        List<Long> questionIds = submissionCreateRequest.getAnswerCreateRequests().stream()
                .map(AnswerCreateRequest::getQuestionId)
                .toList();

        HashSet<Long> uniqueQuestionIds = new HashSet<>(questionIds);

        if (questionIds.size() != uniqueQuestionIds.size()) {
            throw new IllegalArgumentException("같은 질문에 여러번 답할 수 없습니다.");
        }
    }

    /**
     * 3) 중복 옵션 검증
     * @param submissionCreateRequest
     */
    private void validateNoDuplicateOptions(SubmissionCreateRequest submissionCreateRequest) {
        for (AnswerCreateRequest answerCreateRequest : submissionCreateRequest.getAnswerCreateRequests()) {
            if (answerCreateRequest.hasDuplicateOptions()) {
                Set<Long> duplicateOptionIds = answerCreateRequest.getDuplicateOptionIds();
                throw new IllegalArgumentException(
                        String.format("질문 %d에서 중복된 옵션: %s", answerCreateRequest.getQuestionId(), duplicateOptionIds)
                );
            }
        }
    }


    /**
     * 4) 전체 제출 무결성 검증
     * @param submissionCreateRequest
     * @param questionMap
     */
    private void validateAnswerIntegrity(SubmissionCreateRequest submissionCreateRequest, Map<Long, Questions> questionMap) {
        for (AnswerCreateRequest answerCreateRequest : submissionCreateRequest.getAnswerCreateRequests()) {

            Questions question = questionMap.get(answerCreateRequest.getQuestionId());

            // 4-1) 질문 타입별 데이터 검증
            validateAnswerDataConsistency(answerCreateRequest, question);

            if (question.getQuestionType().isChoiceType()) {
                // 4-2)선택 유형의 답변이 질문의 유형에 포함되는 답변인가?
                validateChoiceAnswerOptions(answerCreateRequest, question);
                // 4-3) 단일/복수 선택 규칙
                validateChoiceSelectionRules(answerCreateRequest, question);
            }
        }
    }

    /**
     * 4-1) 질문 타입별 데이터 검증
     * @param answerCreateRequest
     * @param question
     */
    private void validateAnswerDataConsistency(AnswerCreateRequest answerCreateRequest, Questions question) {

        boolean hasOptions = answerCreateRequest.getUniqueOptionIds() != null && !answerCreateRequest.getUniqueOptionIds().isEmpty();

        boolean hasText = answerCreateRequest.getAnswerText() != null && !answerCreateRequest.getAnswerText().isBlank();

        if (question.getQuestionType().isChoiceType()) {
            //선택형 질문 : 옵션만 있어야 한다.
            if (hasText) throw new IllegalArgumentException("선택형 질문에는 텍스트 답변을 할 수 없습니다.");
            if (!hasOptions && question.isRequired()) throw new IllegalArgumentException("필수 선택형 질문은 옵션을 선택해야 합니다.");
        } else {
            //텍스트형 질문: 텍스트만 있어야 한다.
            if (hasOptions) throw new IllegalArgumentException("텍스트형 질문에는 옵션을 선택할 수 없습니다.");
            if (!hasText && question.isRequired()) throw new IllegalArgumentException("필수 텍스트 질문은 답변을 입력해야 합니다.");
        }
    }

    /**
     * 4-2) 선택 유형의 답변이 질문의 유형에 포함되는 답변인가?
     * @param answerCreateRequest
     * @param question
     */
    private void validateChoiceAnswerOptions(AnswerCreateRequest answerCreateRequest, Questions question) {
        Set<Long> submittedOptionIds = answerCreateRequest.getUniqueOptionIds();

        if (submittedOptionIds.isEmpty()) {
            return;
        }

        Set<Long> validOptionIds = question.getOptions().stream()
                .map(Options::getId)
                .collect(Collectors.toSet());

        Set<Long> invalidOptionIds = submittedOptionIds.stream()
                .filter(optionId -> !validOptionIds.contains(optionId))
                .collect(Collectors.toSet());

        if (!invalidOptionIds.isEmpty()) {
            throw new IllegalArgumentException(
                    String.format(
                            "질문 %d에 없는 옵션들입니다: %s",
                            question.getId(), invalidOptionIds
                    )
            );
        }
    }

    /**
     * 4-3) 단일/복수 선택 규칙
     * @param answerCreateRequest
     * @param question
     */
    private void validateChoiceSelectionRules(AnswerCreateRequest answerCreateRequest, Questions question) {
        Set<Long> uniqueOptionIds = answerCreateRequest.getUniqueOptionIds();

        if (uniqueOptionIds.isEmpty()) {
            return;
        }

        if (question.getQuestionType().isSingleChoice()) {
            if (uniqueOptionIds.size() != 1) {
                throw new IllegalArgumentException("단일 선택 질문에는 1개의 옵션만 선택해야 합니다.");
            }
        }
    }

    /**
     * 5) 필수 질문에 대한 답변이 왔는가?
     * @param submissionCreateRequest
     * @param questionMap
     */
    private void validateRequiredQuestions(SubmissionCreateRequest submissionCreateRequest, Map<Long, Questions> questionMap) {

        Set<Long> requiredQuestionIds = questionMap.values().stream()
                .filter(Questions::isRequired)
                .filter(question -> !question.isDeleted())
                .map(Questions::getId)
                .collect(Collectors.toSet());

        Set<Long> answerQuestionIds = submissionCreateRequest.getAnswerCreateRequests().stream()
                .map(AnswerCreateRequest::getQuestionId)
                .collect(Collectors.toSet());

        Set<Long> missingRequiredQuestions = requiredQuestionIds.stream()
                .filter(questionId -> !answerQuestionIds.contains(questionId))
                .collect(Collectors.toSet());

        if (!missingRequiredQuestions.isEmpty()) {
            throw new IllegalArgumentException("필수 질문에 답변하지 않았습니다: " + missingRequiredQuestions);
        }
    }
}
