package formService.fixture

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import formService.domain.InputType
import formService.domain.Question
import formService.domain.SurveyForm
import formService.util.getTsid

fun getFixtureSurveyForm(
    inputTypes: List<InputType>,
    isOptions: Boolean = false,
    optionSize: Int = 0,
    isRemoved: Boolean = false,
): SurveyForm {
    val fixtureBuilder =
        FixtureMonkey
            .builder()
            .plugin(KotlinPlugin())
            .build()
            .giveMeKotlinBuilder<SurveyForm>()
            .set(SurveyForm::id, getTsid())
            .size("questions", inputTypes.size)

    inputTypes.mapIndexed { it, inputType ->
        fixtureBuilder.set("questions[$it].id", (it + 1).toLong())
        fixtureBuilder.set("questions[$it].inputType", inputType)

        if (isOptions && optionSize > 0) {
            fixtureBuilder.size("questions[*].options", optionSize)

            (0..<optionSize).forEach { opIndex ->
                fixtureBuilder.set("questions[$it].options[$opIndex].id", (opIndex + 1).toLong())
            }
        }
    }

    if (!isOptions) {
        fixtureBuilder.set("questions[*].options", null)
    }

    val sample = fixtureBuilder.sample()

    sample.questions.forEach {
        it.isRemoved = isRemoved
    }

    return sample
}

fun getFixtureOnlyQuestion(
    inputTypes: List<InputType>,
    isRemoved: Boolean = false,
    optionSize: Int? = 0,
): List<Question> {
    val mapIndexed =
        inputTypes.mapIndexed { index, inputType ->
            FixtureMonkey
                .builder()
                .plugin(KotlinPlugin())
                .build()
                .giveMeKotlinBuilder<Question>()
                .set("id", (index + 1).toLong())
                .set("inputType", inputType)
        }

    if (optionSize != null && optionSize > 0) {
        mapIndexed.forEach {
            it.size("options", optionSize)

            (0..<optionSize).forEach { index ->
                it.set("options[$index].id", (index + 1).toLong())
            }
        }
    }

    return mapIndexed.map { it.sample() }.map {
        it.isRemoved = isRemoved
        it
    }
}
