package com.wlghsp.querybox.repository

import com.wlghsp.querybox.domain.survey.Option
import com.wlghsp.querybox.domain.survey.Options
import com.wlghsp.querybox.domain.survey.Question
import com.wlghsp.querybox.domain.survey.QuestionType
import com.wlghsp.querybox.domain.survey.Questions
import com.wlghsp.querybox.domain.survey.Survey
import jakarta.persistence.EntityManager
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class SurveyRepositoryTest @Autowired constructor(
    val surveyRepository: SurveyRepository,
    val entityManager: EntityManager,
) {

    @DisplayName("설문과 항목들을 함께 조회한다.")
    @Test
    fun findSurveyWithQuestionsById_loadsQuestions() {
        val question = Question(
            name = "좋아하는 언어는?",
            description = "언어를 선택하세요",
            type = QuestionType.SINGLE_CHOICE,
            required = true,
            options = Options.of(listOf(Option("Kotlin"), Option("Java")))
        )

        val survey = Survey.of(
            title = "개발자 설문",
            description = "개발자에게 묻습니다.",
            questions = Questions.of(listOf(question))
        ).apply {
            addQuestion(question)
        }

        surveyRepository.save(survey)
        entityManager.flush()
        entityManager.clear()

        val foundSurvey = surveyRepository.findSurveyWithQuestionsById(survey.id)

        assertThat(foundSurvey).isNotNull
        assertThat(foundSurvey!!.questions.values()).hasSize(1)
        assertThat(foundSurvey.questions.values()[0].name).isEqualTo("좋아하는 언어는?")
    }

}