package fc.icd.baulonboarding.survey.repository.store;

import fc.icd.baulonboarding.survey.model.entity.Survey;
import fc.icd.baulonboarding.survey.repository.entity.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class SurveyStoreImpl implements SurveyStore{

    private final SurveyRepository surveyRepository;

    @Override
    public Survey store(Survey survey) {
        return surveyRepository.save(survey);
    }


}
