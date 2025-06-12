package fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.in.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InsertContentRequest {

    private String name;
    private String describe;
    private String type;
    @JsonProperty("isRequired")
    private boolean isRequired;
    private List<String> options;

}
