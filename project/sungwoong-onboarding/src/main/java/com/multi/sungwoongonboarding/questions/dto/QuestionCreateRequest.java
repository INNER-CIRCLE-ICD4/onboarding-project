package com.multi.sungwoongonboarding.questions.dto;

import com.multi.sungwoongonboarding.common.valid.OptionValid;
import com.multi.sungwoongonboarding.common.valid.ValidEnum;
import com.multi.sungwoongonboarding.options.dto.OptionCreateRequest;
import com.multi.sungwoongonboarding.questions.domain.Questions;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static com.multi.sungwoongonboarding.questions.domain.Questions.QuestionType.*;

@Getter
@Builder
@NoArgsConstructor
@OptionValid
public class QuestionCreateRequest implements OptionContainer{


    @NotBlank(message = "질문 내용은 필수 입력 항목입니다.")
    private String questionText;

    @ValidEnum(enumClass = Questions.QuestionType.class, message = "유효하지 않은 질문 유형입니다. 단문, 장문, 단일 선택, 복수 선택 중 하나를 선택하세요.")
    private String questionType;

    private boolean isRequired;

    private List<OptionCreateRequest> optionCreateRequests;

    @Builder
    public QuestionCreateRequest(String questionText, String questionType, boolean isRequired, List<OptionCreateRequest> optionCreateRequests) {
        this.questionText = questionText;
        this.questionType = questionType;
        this.isRequired = isRequired;
        this.optionCreateRequests = optionCreateRequests;
    }

    @Override
    public String getType() {
        return this.questionType;
    }

    @Override
    public List<?> getOptions() {
        return this.optionCreateRequests;
    }

    public Questions toDomain() {
        Questions.QuestionsBuilder questionBuilder = Questions.builder()
                .questionText(this.questionText)
                .questionType(valueOf(this.getQuestionType().toUpperCase()))
                .isRequired(this.isRequired);

        if (this.optionCreateRequests != null) {
            questionBuilder.options(this.optionCreateRequests.stream().map(OptionCreateRequest::toDomain).collect(Collectors.toList()));
        }

        return questionBuilder.build();
    }
}
