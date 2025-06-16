package com.innercircle.onboarding.form.domain;

import com.innercircle.onboarding.question.domain.QuestionDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

public class FormDto {

    @ToString
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

}
