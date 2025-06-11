package com.innercircle.survey.survey.application.service

import com.innercircle.survey.common.utils.toEnumOrNull
import com.innercircle.survey.survey.application.port.`in`.SurveyUseCase
import com.innercircle.survey.survey.application.port.`in`.SurveyUseCase.CreateSurveyCommand
import com.innercircle.survey.survey.application.port.`in`.SurveyUseCase.UpdateSurveyCommand
import com.innercircle.survey.survey.application.port.out.SurveyRepository
import com.innercircle.survey.survey.domain.Question
import com.innercircle.survey.survey.domain.QuestionType
import com.innercircle.survey.survey.domain.Survey
import com.innercircle.survey.survey.domain.exception.InvalidQuestionTypeException
import com.innercircle.survey.survey.domain.exception.SurveyNotFoundException
import mu.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

private val logger = KotlinLogging.logger {}

@Service
@Transactional
class SurveyService(
    private val surveyRepository: SurveyRepository,
) : SurveyUseCase {
    override fun createSurvey(command: CreateSurveyCommand): Survey {
        logger.info { "Creating survey with title: ${command.title}" }

        val questions =
            command.questions.map { questionCommand ->
                createQuestionFromCommand(questionCommand)
            }

        val survey =
            Survey.create(
                title = command.title,
                description = command.description,
                questions = questions,
            )

        val savedSurvey = surveyRepository.save(survey)
        logger.info { "Survey created successfully with id: ${savedSurvey.id}" }

        return savedSurvey
    }

    override fun updateSurvey(command: UpdateSurveyCommand): Survey {
        logger.info { "Updating survey with id: ${command.surveyId}" }

        val survey = getSurveyById(command.surveyId)

        // 설문 기본 정보 업데이트
        survey.update(
            title = command.title,
            description = command.description,
        )

        // 질문 업데이트 (기존 질문 soft delete 후 새로운 질문 추가)
        val newQuestions =
            command.questions.map { questionCommand ->
                createQuestionFromUpdateCommand(questionCommand)
            }
        survey.updateQuestions(newQuestions)

        val savedSurvey = surveyRepository.save(survey)
        logger.info { "Survey updated successfully with id: ${savedSurvey.id}, version: ${savedSurvey.version}" }

        return savedSurvey
    }

    @Transactional(readOnly = true)
    override fun getSurveyById(surveyId: UUID): Survey {
        logger.debug { "Loading survey with id: $surveyId" }

        return surveyRepository.findById(surveyId)
            ?: throw SurveyNotFoundException(surveyId)
    }

    @Transactional(readOnly = true)
    override fun getSurveys(pageable: Pageable): Page<Survey> {
        logger.debug { "Loading surveys with pagination: page=${pageable.pageNumber}, size=${pageable.pageSize}" }

        return surveyRepository.findAll(pageable)
    }

    private fun createQuestionFromCommand(command: CreateSurveyCommand.QuestionCommand): Question {
        val questionType = parseQuestionType(command.type)

        return Question.create(
            title = command.title,
            description = command.description,
            type = questionType,
            required = command.required,
            choices = command.choices,
        )
    }

    private fun createQuestionFromUpdateCommand(command: UpdateSurveyCommand.QuestionCommand): Question {
        val questionType = parseQuestionType(command.type)

        return Question.create(
            title = command.title,
            description = command.description,
            type = questionType,
            required = command.required,
            choices = command.choices,
        )
    }

    private fun parseQuestionType(type: String): QuestionType {
        return type.toEnumOrNull<QuestionType>()
            ?: throw InvalidQuestionTypeException(type)
    }
}
