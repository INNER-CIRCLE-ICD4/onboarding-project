package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.request;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.item.QuestionItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SurveyCreateRequest {

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @Size(min = 1, max = 100)
    private List<QuestionItem> questionItems;

}
