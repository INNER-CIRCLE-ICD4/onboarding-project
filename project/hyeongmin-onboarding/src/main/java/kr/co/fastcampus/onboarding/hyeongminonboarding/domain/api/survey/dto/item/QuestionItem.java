package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.item;


import jakarta.annotation.Nullable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.enums.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class QuestionItem {
    @NotEmpty
    private String title;

    @NotEmpty
    private String detail;

    @NotNull
    @Enumerated(EnumType.STRING)
    private QuestionType type;

    private boolean required;

    @Size(min = 0, max = 20)
    private List<QuestionOptionItem> questionOptionItems;
}
