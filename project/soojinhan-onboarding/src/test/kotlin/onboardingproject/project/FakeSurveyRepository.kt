package onboardingproject.project

import onboardingproject.project.domain.*
import onboardingproject.project.repository.fieldOption.FieldOptionRepository
import onboardingproject.project.repository.response.ResponseRepository
import onboardingproject.project.repository.survey.SurveyRepository
import onboardingproject.project.repository.surveyField.SurveyFieldRepository
import onboardingproject.project.repository.surveyVersion.SurveyVersionRepository

/**
 * packageName : onboardingproject.project.repository
 * fileName    : FakeSurveyRepository
 * author      : hsj
 * date        : 2025. 6. 19.
 * description :
 */

class FakeSurveyRepository : SurveyRepository {
    val saved = mutableListOf<Survey>()

    override fun save(survey: Survey): Survey {
        saved += survey
        return survey
    }

    override fun deleteAll() {
        saved.clear()
    }

    override fun findByIdOrNull(id: String): Survey? {
        return saved.find { it.id == id }
    }

    // 필요한 경우 다른 메서드도 구현
}

class FakeSurveyVersionRepository : SurveyVersionRepository {
    val saved = mutableListOf<SurveyVersion>()

    override fun save(version: SurveyVersion): SurveyVersion {
        saved += version
        return version
    }

    override fun deleteAll() {
        saved.clear()
    }

    override fun findFirstBySurveyOrderByVersionDesc(survey: Survey): SurveyVersion {
        return saved.filter { it.survey.id == survey.id }.maxBy { it.version }
    }
}

class FakeSurveyFieldRepository : SurveyFieldRepository {
    val saved = mutableListOf<SurveyField>()

    override fun save(field: SurveyField): SurveyField {
        saved += field
        return field
    }

    override fun deleteAll() {
        saved.clear()
    }
}

class FakeFieldOptionRepository : FieldOptionRepository {
    val saved = mutableListOf<FieldOption>()

    override fun save(option: FieldOption): FieldOption {
        saved += option
        return option
    }

    override fun deleteAll() {
        saved.clear()
    }
}

class FakeResponseRepository : ResponseRepository {
    val saved = mutableListOf<Response>()

    override fun save(response: Response): Response {
        saved += response
        return response
    }

    override fun deleteAll() {
        saved.clear()
    }
}