package formService.application.port.outbound

import formService.domain.SurveyForm

interface SurveyFormRepository {
    fun save(survey: SurveyForm)

    fun getOneBy(id: String): SurveyForm

    fun update(survey: SurveyForm)
}
