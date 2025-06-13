package formService.application

import formService.application.port.inbound.CreateSurveyFormUseCase
import formService.application.port.outbound.SurveyFormRepository
import formService.domain.Question
import formService.domain.SurveyForm
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

    class SurveyFormInMemoryRepository : SurveyFormRepository {
        val store = arrayListOf<SurveyForm>()

        override fun save(survey: SurveyForm) {
            store.add(survey)
        }

        fun removeAll() {
            store.removeAll { it.id.isNotEmpty() }
        }
    }
}
