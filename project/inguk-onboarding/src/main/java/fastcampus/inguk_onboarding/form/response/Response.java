package fastcampus.inguk_onboarding.form.response;

import fastcampus.inguk_onboarding.form.response.application.dto.ResponseSurveyRequestDto;
import fastcampus.inguk_onboarding.form.response.domain.ResponseItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class Response {

    private final Long id;
    private final Long surveyId;
    private final Long surveyVersionId;
    private final List<ResponseItem> responseItems;

    public static Response from(Long surveyId, Long surveyVersionId, ResponseSurveyRequestDto dto) {
        List<ResponseItem> responseItems = dto.answers().stream()
                .map(ResponseItem::from)
                .toList();
                
        return Response.builder()
                .surveyId(surveyId)
                .surveyVersionId(surveyVersionId)
                .responseItems(responseItems)
                .build();
    }
}
