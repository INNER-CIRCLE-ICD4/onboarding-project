package formService.adapter.port.outbound.persistence.jpa

import formService.domain.InputType
import formService.fixture.getFixtureSurveyForm
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class SurveyFormPersistenceAdapterTest(
    @Autowired
    private val surveyFormJpaRepository: SurveyFormJpaRepository,
) {
    private val persistenceAdapter: SurveyFormPersistenceAdapter = SurveyFormPersistenceAdapter(surveyFormJpaRepository)

    @Test
    @DisplayName("jpa 를 이용해서 단답형 설문지 엔티티를 삽입 할 수 있어야된다.")
    fun jpaSurveyFormSaveTest() {
        // given
        val fixtureSurveyForm = getFixtureSurveyForm(listOf(InputType.SHORT_TEXT))

        // when
        persistenceAdapter.save(fixtureSurveyForm)

        val entity = surveyFormJpaRepository.findById(fixtureSurveyForm.id).get()

        // then
        assertThat(entity.id).isEqualTo(fixtureSurveyForm.id)
        assertThat(entity.surveyName).isEqualTo(fixtureSurveyForm.surveyName)
        assertThat(entity.description).isEqualTo(fixtureSurveyForm.description)
        assertThat(entity.questions).hasSize(1)
        assertThat(entity.questions[0].inputType).isEqualTo(InputType.SHORT_TEXT)
        assertThat(entity.questions[0].options).hasSize(0)
    }

    @Test
    @DisplayName("jpa 를 이용해서 단일 리스트 설문지 엔티티를 삽입 할 수 있어야된다.")
    fun jpaQuestionOptionsSaveTest() {
        // given
        val fixtureSurveyForm = getFixtureSurveyForm(listOf(InputType.SINGLE_CHOICE), true, 1)

        // when
        persistenceAdapter.save(fixtureSurveyForm)

        val entity = surveyFormJpaRepository.findById(fixtureSurveyForm.id).get()

        // then
        assertThat(entity.id).isEqualTo(fixtureSurveyForm.id)
        assertThat(entity.surveyName).isEqualTo(fixtureSurveyForm.surveyName)
        assertThat(entity.description).isEqualTo(fixtureSurveyForm.description)
        assertThat(entity.questions).hasSize(1)
        assertThat(entity.questions[0].inputType).isEqualTo(InputType.SINGLE_CHOICE)
        assertThat(entity.questions[0].options).hasSize(1)
        assertThat(entity.questions[0].options[0].option).isEqualTo(
            fixtureSurveyForm.questions[0]
                .options
                ?.get(0)
                ?.value!!,
        )
    }

    @Test
    @DisplayName("jpa 를 이용해서 설문지 엔티티를 상태 변경이 이뤄져야된다.")
    fun jpaQuestionOptionsUpdateTest() {
        // given
        val fixtureSurveyForm = getFixtureSurveyForm(listOf(InputType.SINGLE_CHOICE), true, 1)
        persistenceAdapter.save(fixtureSurveyForm)

        val updateSurveyForm = getFixtureSurveyForm(listOf(InputType.SINGLE_CHOICE), true, 1)

        val entity = surveyFormJpaRepository.findById(fixtureSurveyForm.id).get()

        // when
        entity.update(updateSurveyForm)

        surveyFormJpaRepository.saveAndFlush(entity)

        // then
        assertThat(entity.surveyName).isEqualTo(updateSurveyForm.surveyName)
        assertThat(entity.description).isEqualTo(updateSurveyForm.description)
    }
}
