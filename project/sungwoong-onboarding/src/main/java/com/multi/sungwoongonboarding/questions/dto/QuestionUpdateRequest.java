package com.multi.sungwoongonboarding.questions.dto;

import com.multi.sungwoongonboarding.common.valid.QuestionOptionValid;
import com.multi.sungwoongonboarding.common.valid.ValidEnum;
import com.multi.sungwoongonboarding.options.dto.OptionUpdateRequest;
import com.multi.sungwoongonboarding.questions.domain.Questions;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@QuestionOptionValid
@RequiredArgsConstructor
public class QuestionUpdateRequest implements OptionContainer {

    private final Long id;

    @NotBlank(message = "질문 내용은 필수 입력 항목입니다.")
    private final String questionText;

    @ValidEnum(enumClass = Questions.QuestionType.class, message = "유효하지 않은 질문 유형입니다. 단문, 장문, 단일 선택, 복수 선택 중 하나를 선택하세요.")
    private final String questionType;

    @NotNull(message = "삭제여부를 입력해주세요.")
    private final Boolean deleted;

    @NotNull(message = "필수 여부를 입력해주세요.")
    private final Boolean isRequired;

    private final List<OptionUpdateRequest> options;

    @Override
    public String getType() {
        return this.questionType;
    }

    @Override
    public List<?> getOptions() {
        return this.options;
    }
}
