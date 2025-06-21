package onboarding.survey.factory;

import java.util.List;
import java.util.stream.Collectors;

import onboarding.survey.exception.BadRequestException;
import org.springframework.stereotype.Component;
import onboarding.survey.domain.Choice;
import onboarding.survey.domain.Survey;
import onboarding.survey.domain.SurveyItem;
import onboarding.survey.dto.UpdateSurveyRequest;
import onboarding.survey.dto.UpdateSurveyItemDto;

@Component
public class SurveyUpdater {

    public void applyUpdates(Survey survey, UpdateSurveyRequest req) {
        List<SurveyItem> items = req.items().stream()
                .map(dto -> toSurveyItem(survey, dto))
                .collect(Collectors.toList());

        survey.update(req.title(), req.description(), items);
    }

    private SurveyItem toSurveyItem(Survey survey, UpdateSurveyItemDto dto) {
        // 기존 항목
        if (dto.id() != null) {
            return survey.getItems().stream()
                    .filter(i -> dto.id().equals(i.getId()))
                    .findFirst()
                    .map(item -> {
                        List<Choice> choices = toChoices(dto);
                        item.update(dto.name(), dto.description(), dto.inputType(), dto.required(), choices);
                        return item;
                    })
                    .orElseThrow(() -> new BadRequestException(
                            "해당 설문 항목을 찾을 수 없습니다. (id=" + dto.id() + ")"
                    ));
        }

        // 새 항목
        List<Choice> choices = toChoices(dto);
        return new SurveyItem(dto.name(), dto.description(), dto.inputType(), dto.required(), choices);
    }

    private List<Choice> toChoices(UpdateSurveyItemDto dto) {
        if (!dto.inputType().isSelectable()) {
            return null;
        }

        return dto.choices().stream()
                .map(Choice::new)
                .collect(Collectors.toList());
    }
}