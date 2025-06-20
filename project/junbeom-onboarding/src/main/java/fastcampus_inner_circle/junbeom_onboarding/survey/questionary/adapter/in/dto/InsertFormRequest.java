package fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.in.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsertFormRequest {

    private String name;
    private String describe;
    @Size(min = 1, max = 10, message = "설문 항목은 1개 이상 10개 이하로 입력해야 합니다.")
    private List<InsertContentRequest> contents;


}
