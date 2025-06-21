package com.multi.sungwoongonboarding.questions.dto;

import com.multi.sungwoongonboarding.common.valid.QuestionOptionValid;
import com.multi.sungwoongonboarding.common.valid.EnumValid;
import com.multi.sungwoongonboarding.options.dto.OptionUpdateRequest;
import com.multi.sungwoongonboarding.questions.domain.Questions;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.multi.sungwoongonboarding.questions.domain.Questions.QuestionType.valueOf;

@Getter
@QuestionOptionValid
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class QuestionUpdateRequest implements OptionContainer {

    @NotBlank(message = "질문 내용은 필수 입력 항목입니다.")
    private String questionText;

    @EnumValid(enumClass = Questions.QuestionType.class, message = "유효하지 않은 질문 유형입니다. 단문, 장문, 단일 선택, 복수 선택 중 하나를 선택하세요.")
    private String questionType;

    @NotNull(message = "삭제여부를 입력해주세요.")
    private Boolean deleted;

    @NotNull(message = "필수 여부를 입력해주세요.")
    private Boolean isRequired;

    private List<OptionUpdateRequest> options;

    @Override
    public String getType() {
        return this.questionType;
    }

    @Override
    public List<?> getOptions() {
        return this.options;
    }

    public Questions toDomain() {

        Questions.QuestionsBuilder questionBuilder = Questions.builder()
                .questionText(this.questionText)
                .questionType(valueOf(this.getQuestionType().toUpperCase()))
                .isRequired(this.isRequired);

        if (this.options != null) {
            questionBuilder.options(this.options.stream().map(OptionUpdateRequest::toDomain).collect(Collectors.toList()));
        }

        return questionBuilder.build();

    }
}
