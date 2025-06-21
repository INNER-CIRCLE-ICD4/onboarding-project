package onboarding.survey.factory;

import onboarding.survey.domain.Choice;
import onboarding.survey.domain.Survey;
import onboarding.survey.domain.SurveyItem;
import onboarding.survey.dto.CreateSurveyRequest;
import onboarding.survey.dto.SurveyItemDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SurveyFactory {

    public Survey createSurvey(CreateSurveyRequest request) {
        List<SurveyItem> items = request.items().stream()
                .map(this::createSurveyItem)
                .toList();

        return new Survey(request.title(), request.description(), items);
    }

    private SurveyItem createSurveyItem(SurveyItemDto dto) {
        List<Choice> choices = null;
        if (dto.inputType().isSelectable()) {
            choices = dto.choices().stream()
                    .map(Choice::new)
                    .toList();
        }
        return new SurveyItem(dto.name(), dto.description(), dto.inputType(), dto.required(), choices);
    }
}