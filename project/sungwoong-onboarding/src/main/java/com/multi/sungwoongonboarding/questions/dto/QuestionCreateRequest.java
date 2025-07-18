package com.multi.sungwoongonboarding.questions.dto;

import com.multi.sungwoongonboarding.common.valid.OptionValid;
import com.multi.sungwoongonboarding.common.valid.ValidEnum;
import com.multi.sungwoongonboarding.options.dto.OptionCreateRequest;
import com.multi.sungwoongonboarding.questions.domain.Questions;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
@OptionValid
public class QuestionCreateRequest {


    @NotBlank(message = "질문 내용은 필수 입력 항목입니다.")
    private final String questionText;

    @ValidEnum(enumClass = Questions.QuestionType.class, message = "유효하지 않은 질문 유형입니다. 단문, 장문, 단일 선택, 복수 선택 중 하나를 선택하세요.")
    private final String questionType;
    private final int order;
    private final boolean isRequired;

    private final List<OptionCreateRequest> optionCreateRequests;
}
