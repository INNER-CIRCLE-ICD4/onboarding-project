package formService.application

import formService.application.port.inbound.CreateSurveyFormUseCase
import formService.application.port.inbound.ModifySurveyFormUseCase
import formService.application.port.outbound.SurveyFormRepository
import formService.domain.Question
import formService.domain.SurveyForm
import formService.fixture.getFixtureSurveyForm
import jakarta.persistence.EntityNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class SurveyFormServiceTest {
    private val repository = SurveyFormInMemoryRepository()

    @AfterEach
    fun tearDown() {
        repository.removeAll()
    }

    @Test
    @DisplayName("사용자는 설문 조사 생성 할 수 있어야된다.")
    fun createSurveyFormTest() {
        // given
        val surveyName = "surveyName"
        val surveyDescription = "description"
        val questionName = "name"
        val questionDescription = "description"

        val command =
            CreateSurveyFormUseCase.CreateSurveyFormCommand(
                surveyName = surveyName,
                description = surveyDescription,
                questions =
                    listOf(
                        CreateSurveyFormUseCase.CreateSurveyFormQuestion(
                            name = questionName,
                            description = questionDescription,
                            required = true,
                            inputType = Question.QuestionInputType.SHORT_TEXT,
                            options = listOf(),
                        ),
                    ),
            )

        val surveyFormService = SurveyFormService(repository)

        // when
        surveyFormService.createSurveyForm(command)
        val surveyForm = repository.store[0]

        // then
        assertThat(surveyForm.id).isNotEmpty()
        assertThat(surveyForm.surveyName).isEqualTo(surveyName)
        assertThat(surveyForm.description).isEqualTo(surveyDescription)
        assertThat(surveyForm.questions).hasSize(1)
        assertThat(surveyForm.questions[0].name).isEqualTo(questionName)
        assertThat(surveyForm.questions[0].description).isEqualTo(questionDescription)
        assertThat(surveyForm.questions[0].required).isTrue()
        assertThat(surveyForm.questions[0].inputType).isEqualTo(Question.QuestionInputType.SHORT_TEXT)
        assertThat(surveyForm.questions[0].options).hasSize(0)
    }

    @Test
    @DisplayName("사용자는 설문 조사 조회 시 id 기반으로 조회 할 수 있어야된다.")
    fun readOneSurveyFormTest() {
        // given
        val surveyForm = getFixtureSurveyForm(listOf(Question.QuestionInputType.SHORT_TEXT))

        val surveyFormService = SurveyFormService(repository)
        repository.save(surveyForm)

        // when
        val retrieveSurveyForm = surveyFormService.retrieveSurveyForm(surveyForm.id)

        // then
        assertThat(retrieveSurveyForm.id).isEqualTo(surveyForm.id)
    }

    @Test
    @DisplayName("사용자는 설문 조사를 수정 할 수 있어야된다. 수정 시 설문 항목 삭제도 포함한다.")
    fun modifySurveyFormTest() {
        // given
        val surveyForm = getFixtureSurveyForm(listOf(Question.QuestionInputType.SHORT_TEXT), true, 1, true)

        val surveyFormService = SurveyFormService(repository)
        repository.save(surveyForm)

        // when
        val command =
            ModifySurveyFormUseCase.ModifySurveyFormCommand(
                id = surveyForm.id,
                surveyName = surveyForm.surveyName,
                description = surveyForm.description,
                questions =
                    surveyForm.questions.map {
                        ModifySurveyFormUseCase.ModifySurveyFormQuestion(
                            id = it.id!!,
                            name = it.name,
                            description = it.description,
                            inputType = it.inputType,
                            required = it.required,
                            isRemoved = it.isRemoved,
                            options =
                                it.options?.map { qo ->
                                    ModifySurveyFormUseCase.ModifySurveyFormQuestionOption(
                                        id = qo.id!!,
                                        value = qo.value,
                                    )
                                },
                        )
                    },
            )
        surveyFormService.modifySurveyForm(command)

        // then
        assertThat(surveyForm.surveyName).isEqualTo(command.surveyName)
        assertThat(surveyForm.questions[0].isRemoved).isTrue()
    }

    @Test
    @DisplayName("사용자는 설문 조사 조회 시 id 가 없으면 예외가 발생되어야된다.")
    fun readOneNotFound() {
        // given
        val surveyForm = getFixtureSurveyForm(listOf(Question.QuestionInputType.SHORT_TEXT))

        val surveyFormService = SurveyFormService(repository)
        repository.save(surveyForm)

        // when
        val retrieveSurveyForm = surveyFormService.retrieveSurveyForm(surveyForm.id)

        // then
        assertThat(retrieveSurveyForm.id).isEqualTo(surveyForm.id)
    }

    class SurveyFormInMemoryRepository : SurveyFormRepository {
        val store = arrayListOf<SurveyForm>()

        override fun save(survey: SurveyForm) {
            store.add(survey)
        }

        override fun getOneBy(id: String): SurveyForm = store.find { it.id == id } ?: throw EntityNotFoundException()

        override fun update(survey: SurveyForm) {
            val oneBy = getOneBy(survey.id)

            oneBy.modify(survey.surveyName, survey.description)
            oneBy.modifyQuestion(survey.questions)
        }

        fun removeAll() {
            store.removeAll { it.id.isNotEmpty() }
        }
    }
}
