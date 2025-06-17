package survey.survey.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import survey.survey.controller.request.survey.create.SurveyFormCreateRequest;
import survey.survey.entity.surveyform.SurveyForm;
import survey.survey.repository.SurveyFormRepository;

@Service
@RequiredArgsConstructor
public class SurveyFormService {
    private final SurveyFormRepository surveyFormRepository;

    @Transactional
    public SurveyForm create(Long surveyId, Long surveyFormVersion,
                             SurveyFormCreateRequest request) {
        return surveyFormRepository.save(createSurveyForm(surveyId, surveyFormVersion, request));

    }

//    @Transactional
//    public SurveyForm update(Long surveyFormId, SurveyFormCreateRequest request) {
//
//        surveyForm.update(request.title(), request.description());
//        return surveyFormRepository.save(surveyForm);
//    }

    private SurveyForm createSurveyForm(Long surveyId,
                                        Long surveyFormVersion, SurveyFormCreateRequest request) {
        return SurveyForm.create(
                surveyFormVersion,
                request.title(),
                request.description(),
                surveyId
        );
    }
}
