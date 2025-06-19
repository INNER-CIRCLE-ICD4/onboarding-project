package survey.surveyread.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import survey.surveyread.entity.SurveyResponseQueryModel;
import survey.surveyread.repository.SurveyResponseQueryModelRepository;
import survey.surveyread.service.response.SurveyReadResponse;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SurveyReadService {
    private final SurveyResponseQueryModelRepository repository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void saveResponse(SurveyReadResponse responseDto) {
        try {
            String jsonResponse = objectMapper.writeValueAsString(responseDto);

            SurveyResponseQueryModel model = SurveyResponseQueryModel.create(
                    responseDto.surveyFormId(),
                    responseDto.surveySubmitId(),
                    jsonResponse
            );

            repository.save(model);
        } catch (Exception e) {
            throw new RuntimeException("응답 저장 중 오류가 발생했습니다", e);
        }
    }

    public List<SurveyReadResponse> getResponsesBySurveyId(Long surveyId) {
        List<SurveyResponseQueryModel> models = repository.findAllBySurveyId(surveyId);

        return models.stream()
                .map(model -> {
                    try {
                        return objectMapper.readValue(model.getSurveyResponse(), SurveyReadResponse.class);
                    } catch (Exception e) {
                        log.error("응답 파싱 중 오류: {}", e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
