package com.innercircle.onboarding.form.domain;

import com.innercircle.onboarding.question.domain.QuestionDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.UUID;

public class FormDto {

    @Getter
    @NoArgsConstructor
    public static class Create {

        @NotBlank
        private String title;

        private String description;

        @Size(min = 1, max = 10)
        @NotEmpty
        @Valid
        public List<QuestionDto.Create> questionList;

        @Builder
        public Create(String title, String description, List<QuestionDto.Create> questionList) {
            this.title = title;
            this.description = description;
            this.questionList = questionList;
        }

        public Form ofEntity() {
            return Form.builder()
                    .uuid(UUID.randomUUID())
                    .title(title)
                    .description(description)
                    .build();
        }

    }

    @ToString
    @Getter
    @NoArgsConstructor
    public static class SearchQuestionsDto {
        private UUID uuid;
        private String title;
        private String formDescription;
        private Long questionSeq;
        private String questionContent;
        private String questionDescription;
        private String answerType;
        private String answerOption;
        private boolean isDeleted;
        private boolean isRequired;
        private List<String> answerList = List.of();

        public void setAnswerList(List<String> answerList) {
            if (!ObjectUtils.isEmpty(answerList)) {
                this.answerList = answerList;
            }
        }

    }

    @ToString
    @Getter
    @NoArgsConstructor
    public static class SearchAnswerDto {
        private Long questionSeq;
        private String questionContent;
        private String answerContent;
        private String questionDescription;
        private String answerType;
        private String answerOption;
        private boolean isDeleted;
        private boolean isRequired;
    }

}
