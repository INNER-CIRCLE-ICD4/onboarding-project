package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.item;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class QuestionOptionItem {

    private String optionValue;
}
