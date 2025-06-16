package fc.icd.baulonboarding.survey.repository.reader;

import fc.icd.baulonboarding.survey.model.entity.Survey;
import fc.icd.baulonboarding.survey.repository.entity.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SurveyReaderImpl implements SurveyReader{

    private final SurveyRepository surveyRepository;
    @Override
    public Survey getSurveyBy(Long id) {
        return surveyRepository.findById(id)
                .orElseThrow(RuntimeException::new);
    }
}
