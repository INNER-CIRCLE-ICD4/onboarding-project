package com.innercircle.onboarding.question.domain;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.innercircle.onboarding.answer.domain.Answer;
import com.innercircle.onboarding.common.exceptions.CommonException;
import com.innercircle.onboarding.common.response.ResponseStatus;
import com.innercircle.onboarding.common.utils.JsonUtils;
import com.innercircle.onboarding.enums.AnswerType;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

public class QuestionDto {

    @Slf4j
    @Getter
    @NoArgsConstructor
    public static class Create {

        @NotEmpty
        private String content;
        private String description;
        @NotEmpty
        private String answerType;
        private ArrayNode answerOption;
        private boolean isRequired;

        public void setAnswerType(String answerType) {
            if (!AnswerType.checkValid(answerType)) {
                throw new CommonException(ResponseStatus.VALIDATION_FAIL, "[AnswerType] is not valid: " + answerType);
            }
            this.answerType = answerType;
        }

        public Question ofEntity(Long formSeq, int orderIdx) {
            return Question.builder()
                    .formSeq(formSeq)
                    .content(content)
                    .description(description)
                    .answerType(answerType)
                    .answerOption(JsonUtils.toJsonString(answerOption))
                    .isRequired(isRequired)
                    .version(1)
                    .isDeleted(false)
                    .orderIdx(orderIdx)
                    .build();
        }

    }

    @Getter
    @NoArgsConstructor
    public static class Update {

        @NotEmpty
        private String content;
        private String description;
        @NotEmpty
        private String answerType;
        private ArrayNode answerOption;
        private boolean isRequired;

        public void setAnswerType(String answerType) {
            if (!AnswerType.checkValid(answerType)) {
                throw new CommonException(ResponseStatus.VALIDATION_FAIL, "[AnswerType] is not valid: " + answerType);
            }
            this.answerType = answerType;
        }

        public Question ofDeleted(Question origin) {
            return Question.builder()
                    .seq(origin.getSeq())
                    .formSeq(origin.getFormSeq())
                    .content(origin.getContent())
                    .description(origin.getDescription())
                    .answerType(origin.getAnswerType())
                    .answerOption(origin.getAnswerOption())
                    .isRequired(origin.isRequired())
                    .version(origin.getVersion())
                    .isDeleted(true)
                    .orderIdx(origin.getOrderIdx())
                    .build();
        }

        public Question ofEntity(Question origin) {
            return Question.builder()
                    .formSeq(origin.getFormSeq())
                    .content(content)
                    .description(description)
                    .answerType(answerType)
                    .answerOption(JsonUtils.toJsonString(answerOption))
                    .isRequired(isRequired)
                    .version(origin.getVersion() + 1)
                    .isDeleted(false)
                    .orderIdx(origin.getOrderIdx())
                    .build();
        }

    }

    @Getter
    @NoArgsConstructor
    public static class SearchOrigin {

        private Long questionSeq;
        private String originQuestion;
        private boolean isRequired;

        public Answer ofEntity(String content) {
            return Answer.builder()
                    .questionSeq(questionSeq)
                    .originQuestion(originQuestion)
                    .content(content)
                    .build();
        }

    }


}
