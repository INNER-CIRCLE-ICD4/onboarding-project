package com.multi.sungwoongonboarding.forms.dto;

import com.multi.sungwoongonboarding.forms.domain.Forms;
import com.multi.sungwoongonboarding.questions.dto.QuestionUpdateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class FormUpdateRequest {

    private final Long id;

    @NotBlank(message = "제목은 필수 입력 항목입니다.")
    private final String title;

    @NotBlank(message = "설명은 필수 입력 항목입니다.")
    private final String description;

    @Valid
    @NotNull(message = "질문 목록은 필수 입력 항목입니다.")
    @Size(min = 1, max = 10, message = "질문은 최소 1개에서 최대 10개까지 입력할 수 있습니다.")
    private final List<QuestionUpdateRequest> questions;

    public Forms toDomain() {

        return Forms.builder()
                .id(this.id)
                .title(this.title)
                .description(this.description)
                .build();
    }
}
