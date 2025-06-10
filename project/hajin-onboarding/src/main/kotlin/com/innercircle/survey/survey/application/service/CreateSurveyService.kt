package com.innercircle.survey.survey.application.service

import com.innercircle.survey.survey.application.port.`in`.CreateSurveyUseCase
import com.innercircle.survey.survey.application.port.`in`.CreateSurveyUseCase.CreateSurveyCommand
import com.innercircle.survey.survey.application.port.out.SaveSurveyPort
import com.innercircle.survey.survey.domain.Question
import com.innercircle.survey.survey.domain.QuestionType
import com.innercircle.survey.survey.domain.Survey
import com.innercircle.survey.survey.domain.exception.InvalidQuestionTypeException
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val logger = KotlinLogging.logger {}

@Service
@Transactional
class CreateSurveyService(
    private val saveSurveyPort: SaveSurveyPort,
) : CreateSurveyUseCase {
    override fun createSurvey(command: CreateSurveyCommand): Survey {
        logger.info { "Creating survey with title: ${command.title}" }

        val questions =
            command.questions.map { questionCommand ->
                val questionType = parseQuestionType(questionCommand.type)
                Question.create(
                    title = questionCommand.title,
                    description = questionCommand.description,
                    type = questionType,
                    required = questionCommand.required,
                    choices = questionCommand.choices,
                )
            }

        val survey =
            Survey.create(
                title = command.title,
                description = command.description,
                questions = questions,
            )

        val savedSurvey = saveSurveyPort.save(survey)

        logger.info { "Survey created successfully with id: ${savedSurvey.id}" }

        return savedSurvey
    }

    private fun parseQuestionType(type: String): QuestionType {
        return try {
            QuestionType.valueOf(type.uppercase())
        } catch (e: IllegalArgumentException) {
            throw InvalidQuestionTypeException(type)
        }
    }
}
