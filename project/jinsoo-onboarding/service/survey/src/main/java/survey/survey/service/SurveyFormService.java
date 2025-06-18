package survey.survey.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import survey.survey.config.ApplicationException;
import survey.survey.controller.request.survey.create.SurveyFormCreateRequest;
import survey.survey.controller.request.survey.update.SurveyFormUpdateRequest;
import survey.survey.entity.surveyform.SurveyForm;
import survey.survey.repository.SurveyFormRepository;

import java.util.Objects;

import static survey.survey.config.ErrorType.SURVEY_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SurveyFormService {
    private final SurveyFormRepository surveyFormRepository;

    @Transactional
    public SurveyForm create(Long surveyId, Long surveyFormVersion,
                             SurveyFormCreateRequest request) {
        return surveyFormRepository.save(createSurveyForm(surveyId, surveyFormVersion, request));
    }

    @Transactional
    public SurveyForm update(Long surveyId, SurveyFormUpdateRequest request) {
        SurveyForm surveyForm = findById(request.surveyFormId());

        boolean formInfoChanged = isFormInfoChanged(surveyForm, surveyId, request.title(), request.description());

        if (formInfoChanged) {
            SurveyForm updatedSurveyForm = SurveyForm.update(
                    surveyForm.getVersion() + 1L,
                    request.title(),
                    request.description(),
                    surveyId
            );
            return surveyFormRepository.save(updatedSurveyForm);
        }

        return surveyForm;
    }

    private SurveyForm createSurveyForm(Long surveyId,
                                        Long surveyFormVersion, SurveyFormCreateRequest request) {
        return SurveyForm.create(
                surveyFormVersion,
                request.title(),
                request.description(),
                surveyId
        );
    }

    public SurveyForm findById(Long surveyFormId) {
        return surveyFormRepository.findById(surveyFormId)
                .orElseThrow(() -> new ApplicationException(SURVEY_NOT_FOUND));
    }

    public boolean isFormContentChanged(Long surveyFormId, String newTitle, String newDescription) {
        SurveyForm form = findById(surveyFormId);
        return !Objects.equals(form.getTitle(), newTitle) ||
                !Objects.equals(form.getDescription(), newDescription);
    }

    private boolean isFormInfoChanged(SurveyForm form, Long surveyId, String newTitle, String newDescription) {
        return !Objects.equals(form.getTitle(), newTitle) ||
                !Objects.equals(form.getDescription(), newDescription) ||
                !Objects.equals(form.getSurveyId(), surveyId);
    }

    @Transactional
    public SurveyForm createNewVersion(Long surveyId, Long oldSurveyFormId) {
        SurveyForm oldForm = findById(oldSurveyFormId);

        SurveyForm newForm = SurveyForm.create(
                oldForm.getVersion() + 1,
                oldForm.getTitle(),
                oldForm.getDescription(),
                surveyId
        );
        return surveyFormRepository.save(newForm);
    }
}
