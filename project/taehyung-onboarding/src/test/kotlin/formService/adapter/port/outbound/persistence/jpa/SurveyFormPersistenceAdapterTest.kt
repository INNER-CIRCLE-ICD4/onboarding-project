package formService.adapter.port.outbound.persistence.jpa

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import formService.adapter.port.outbound.persistence.jpa.SurveyFormJpaRepository
import formService.adapter.port.outbound.persistence.jpa.SurveyFormPersistenceAdapter
import formService.domain.Question
import formService.domain.SurveyForm
import formService.util.getTsid
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class SurveyFormPersistenceAdapterTest(
    @Autowired
    private val surveyFormJpaRepository: SurveyFormJpaRepository,
) {
    private val persistenceAdapter: SurveyFormPersistenceAdapter = SurveyFormPersistenceAdapter(surveyFormJpaRepository)

    @Test
    @DisplayName("jpa 를 이용해서 단답형 설문지 엔티티를 삽입 할 수 있어야된다.")
    fun jpaSurveyFormSaveTest() {
        // given
        val fixtureSurveyForm = getFixtureSurveyForm(1, listOf(Question.QuestionInputType.SHORT_TEXT))

        // when
        persistenceAdapter.save(fixtureSurveyForm)

        val entity = surveyFormJpaRepository.findById(fixtureSurveyForm.id).get()

        // then
        assertThat(entity.id).isEqualTo(fixtureSurveyForm.id)
        assertThat(entity.surveyName).isEqualTo(fixtureSurveyForm.surveyName)
        assertThat(entity.description).isEqualTo(fixtureSurveyForm.description)
        assertThat(entity.questions).hasSize(1)
        assertThat(entity.questions[0].inputType).isEqualTo(Question.QuestionInputType.SHORT_TEXT)
        assertThat(entity.questions[0].options).hasSize(0)
    }

    @Test
    @DisplayName("jpa 를 이용해서 단일 리스트 설문지 엔티티를 삽입 할 수 있어야된다.")
    fun jpaQuestionOptionsSaveTest() {
        // given
        val fixtureSurveyForm = getFixtureSurveyForm(1, listOf(Question.QuestionInputType.SINGLE_CHOICE), true, 1)

        // when
        persistenceAdapter.save(fixtureSurveyForm)

        val entity = surveyFormJpaRepository.findById(fixtureSurveyForm.id).get()

        // then
        assertThat(entity.id).isEqualTo(fixtureSurveyForm.id)
        assertThat(entity.surveyName).isEqualTo(fixtureSurveyForm.surveyName)
        assertThat(entity.description).isEqualTo(fixtureSurveyForm.description)
        assertThat(entity.questions).hasSize(1)
        assertThat(entity.questions[0].inputType).isEqualTo(Question.QuestionInputType.SINGLE_CHOICE)
        assertThat(entity.questions[0].options).hasSize(1)
        assertThat(entity.questions[0].options[0].option).isEqualTo(
            fixtureSurveyForm.questions[0]
                .options
                ?.get(0)
                ?.value!!,
        )
    }

    private fun getFixtureSurveyForm(
        questionsSize: Int,
        inputTypes: List<Question.QuestionInputType>,
        isOptions: Boolean = false,
        optionSize: Int = 0,
    ): SurveyForm {
        val fixtureBuilder =
            FixtureMonkey
                .builder()
                .plugin(KotlinPlugin())
                .build()
                .giveMeKotlinBuilder<SurveyForm>()
                .set(SurveyForm::id, getTsid())
                .size("questions", questionsSize)

        (0..<questionsSize).forEach {
            fixtureBuilder.set("questions[$it].inputType", inputTypes[it % 4])
        }

        if (!isOptions) {
            fixtureBuilder.set("questions[*].options", null)
        }

        if (isOptions && optionSize > 0) {
            fixtureBuilder.size("questions[*].options", optionSize)
        }

        return fixtureBuilder.sample()
    }
}
