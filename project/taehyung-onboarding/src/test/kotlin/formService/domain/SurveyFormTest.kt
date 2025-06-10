package formService.domain

import formService.util.getTsid
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class SurveyFormTest {
    @Test
    @DisplayName("설문조사 생성 시 이름, 설명은 필수여야되고, 설문 받을 항목은 1개 이상여야된다.")
    fun createTest() {
        // given
        val surveyName = "surveyName"
        val description = "description"
        val questionDescription = "description0"
        val questions = getSurveyFormQuestions(1, listOf(Question.QuestionInputType.SHORT_TEXT))
        val questionName = "question0"

        // when
        val surveyForm =
            SurveyForm(
                id = getTsid(),
                surveyName = surveyName,
                description = description,
                questions = questions,
            )

        // then
        assertThat(surveyForm.surveyName).isEqualTo(surveyName)
        assertThat(surveyForm.description).isEqualTo(description)
        assertThat(surveyForm.questions).hasSize(1)
        assertThat(surveyForm.questions[0].description).isEqualTo(questionDescription)
        assertThat(surveyForm.questions[0].inputType).isEqualTo(Question.QuestionInputType.SHORT_TEXT)
        assertThat(surveyForm.questions[0].required).isTrue()
        assertThat(surveyForm.questions[0].name).isEqualTo(questionName)
    }

    @Test
    @DisplayName("설문조사 설문 받은 항목이 SINGLE_CHOICE 포함된 경우에는 option 값이 반드시 존재해야된다.")
    fun createSingleChoiceTest() {
        // given
        val surveyName = "surveyName"
        val description = "description"
        val questions =
            getSurveyFormQuestions(
                3,
                listOf(
                    Question.QuestionInputType.SHORT_TEXT,
                    Question.QuestionInputType.LONG_TEXT,
                    Question.QuestionInputType.SINGLE_CHOICE,
                ),
                listOf(
                    QuestionOption(value = "option1"),
                    QuestionOption(value = "option2"),
                    QuestionOption(value = "option3"),
                ),
            )

        // when
        val surveyForm =
            SurveyForm(
                id = getTsid(),
                surveyName = surveyName,
                description = description,
                questions = questions,
            )

        // then
        assertThat(surveyForm.questions[2].options).hasSize(3)
        assertThat(surveyForm.questions[2].options!![0].value).isEqualTo("option1")
        assertThat(surveyForm.questions[2].options!![1].value).isEqualTo("option2")
        assertThat(surveyForm.questions[2].options!![2].value).isEqualTo("option3")
    }

    @Test
    @DisplayName("설문 받을 항목은 10개 초과이면 예외를 발생해야된다.")
    fun validateQuestionTest_question_greater_than_10() {
        // given
        val surveyName = "surveyName"
        val description = "description"
        val questions = getSurveyFormQuestions(11)

        // when
        // then
        assertThrows(IllegalArgumentException::class.java) {
            SurveyForm(
                id = getTsid(),
                surveyName = surveyName,
                description = description,
                questions = questions,
            )
        }
    }

    private fun getSurveyFormQuestions(
        size: Int,
        inputType: List<Question.QuestionInputType>? = null,
        options: List<QuestionOption>? = null,
    ): List<Question> =
        (0..<size).toList().map {
            val question =
                Question(
                    description = "description$it",
                    inputType = if (inputType == null) Question.QuestionInputType.entries[it % 4] else inputType[it],
                    required = true,
                    name = "question$it",
                )

            if (inputType != null &&
                (
                    inputType[it] == Question.QuestionInputType.SINGLE_CHOICE ||
                        inputType[it] == Question.QuestionInputType.MULTI_CHOICE
                )
            ) {
                question.options = options
            }

            question
        }
}
