package formService.fixture

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import formService.domain.Question
import formService.domain.SurveyForm
import formService.util.getTsid

fun getFixtureSurveyForm(
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
