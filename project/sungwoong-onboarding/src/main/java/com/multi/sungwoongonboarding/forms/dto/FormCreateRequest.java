package com.multi.sungwoongonboarding.forms.dto;

import com.multi.sungwoongonboarding.forms.domain.Forms;
import com.multi.sungwoongonboarding.questions.domain.Questions;
import com.multi.sungwoongonboarding.questions.dto.QuestionCreateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class FormCreateRequest {

    @NotBlank(message = "제목은 필수 입력 항목입니다.")
    private final String title;

    private final String description;

    @Valid
    @Size(min = 1, max = 10, message = "질문은 최소 1개에서 최대 10개까지 입력할 수 있습니다.")
    private final List<QuestionCreateRequest> questionCreateRequests;

    public Forms toDomain() {
        Forms.FormsBuilder formBuilder = Forms.builder()
                .title(this.title)
                .description(this.description);

        if (this.questionCreateRequests != null) {
            formBuilder.questions(this.questionCreateRequests.stream().map(QuestionCreateRequest::toDomain).toList());
        }

        return formBuilder.build();
    }

}
