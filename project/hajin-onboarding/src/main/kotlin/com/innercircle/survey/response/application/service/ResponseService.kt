package com.innercircle.survey.response.application.service

import com.innercircle.survey.response.adapter.out.persistence.dto.ResponseSummaryProjection
import com.innercircle.survey.response.application.port.`in`.ResponseUseCase
import com.innercircle.survey.response.application.port.`in`.ResponseUseCase.SubmitResponseCommand
import com.innercircle.survey.response.application.port.out.ResponseRepository
import com.innercircle.survey.response.domain.Answer
import com.innercircle.survey.response.domain.Response
import com.innercircle.survey.response.domain.exception.InvalidAnswerException
import com.innercircle.survey.response.domain.exception.InvalidChoiceException
import com.innercircle.survey.response.domain.exception.ResponseNotFoundException
import com.innercircle.survey.response.domain.exception.SurveyMismatchException
import com.innercircle.survey.survey.application.port.out.SurveyRepository
import com.innercircle.survey.survey.domain.Question
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
class ResponseService(
    private val responseRepository: ResponseRepository,
    private val surveyRepository: SurveyRepository,
) : ResponseUseCase {
    override fun submitResponse(command: SubmitResponseCommand): Response {
        logger.info { "Submitting response for survey: ${command.surveyId}" }

        // 설문조사 조회
        val survey =
            surveyRepository.findById(command.surveyId)
                ?: throw SurveyNotFoundException(command.surveyId)

        // 응답 유효성 검증
        validateAnswersAgainstSurvey(command.answers, survey.questions)

        // Answer 엔티티 생성
        val answers =
            command.answers.map { answerCommand ->
                createAnswerFromCommand(answerCommand, survey.questions)
            }

        // Response 생성
        val response =
            Response.create(
                survey = survey,
                respondentId = command.respondentId,
                answers = answers,
            )

        val savedResponse = responseRepository.save(response)
        logger.info { "Response submitted successfully with id: ${savedResponse.id}" }

        return savedResponse
    }

    @Transactional(readOnly = true)
    override fun getResponseById(responseId: UUID): Response {
        logger.debug { "Loading response with id: $responseId" }

        return responseRepository.findById(responseId)
            ?: throw ResponseNotFoundException(responseId)
    }

    @Transactional(readOnly = true)
    override fun getResponsesBySurveyId(
        surveyId: UUID,
        pageable: Pageable,
    ): Page<Response> {
        logger.debug { 
            "Loading responses for survey: $surveyId, page=${pageable.pageNumber}, size=${pageable.pageSize}" 
        }

        // 설문조사 존재 여부 확인
        surveyRepository.findById(surveyId)
            ?: throw SurveyNotFoundException(surveyId)

        // 답변을 포함한 응답 조회 (N+1 문제 방지)
        return responseRepository.findBySurveyIdWithAnswers(surveyId, pageable)
    }

    @Transactional(readOnly = true)
    override fun getResponseSummariesBySurveyId(
        surveyId: UUID,
        pageable: Pageable,
    ): Page<ResponseSummaryProjection> {
        logger.debug { 
            "Loading response summaries for survey: $surveyId, page=${pageable.pageNumber}, size=${pageable.pageSize}" 
        }

        // 설문조사 존재 여부 확인
        surveyRepository.findById(surveyId)
            ?: throw SurveyNotFoundException(surveyId)

        // 프로젝션을 사용한 최적화된 요약 조회
        return responseRepository.findResponseSummariesBySurveyId(surveyId, pageable)
    }

    private fun validateAnswersAgainstSurvey(
        answerCommands: List<SubmitResponseCommand.AnswerCommand>,
        questions: List<Question>,
    ) {
        val questionMap = questions.associateBy { it.id }
        val answerQuestionIds = answerCommands.map { it.questionId }.toSet()

        // 모든 응답이 유효한 질문에 대한 것인지 확인
        val invalidQuestionIds = answerQuestionIds - questionMap.keys
        if (invalidQuestionIds.isNotEmpty()) {
            throw SurveyMismatchException("존재하지 않는 질문에 대한 응답: ${invalidQuestionIds.joinToString(", ")}")
        }

        // 중복 응답 확인
        val duplicateQuestionIds =
            answerCommands
                .groupingBy { it.questionId }
                .eachCount()
                .filter { it.value > 1 }
                .keys

        if (duplicateQuestionIds.isNotEmpty()) {
            throw InvalidAnswerException("중복된 응답이 있습니다: ${duplicateQuestionIds.joinToString(", ")}")
        }
    }

    private fun createAnswerFromCommand(
        command: SubmitResponseCommand.AnswerCommand,
        questions: List<Question>,
    ): Answer {
        val question =
            questions.find { it.id == command.questionId }
                ?: throw SurveyMismatchException("질문을 찾을 수 없습니다: ${command.questionId}")

        return when {
            question.type.isTextType() -> {
                val textValue =
                    command.textValue
                        ?: throw InvalidAnswerException("텍스트형 질문에는 텍스트 응답이 필요합니다.")

                Answer.createTextAnswer(
                    questionId = question.id,
                    questionTitle = question.title,
                    questionType = question.type,
                    textValue = textValue,
                )
            }
            question.type.isChoiceType() -> {
                val selectedChoiceIds =
                    command.selectedChoiceIds
                        ?: throw InvalidAnswerException("선택형 질문에는 선택지가 필요합니다.")

                // 유효한 선택지인지 확인
                val validChoiceIds = question.choices.map { it.id }.toSet()
                val invalidChoiceIds = selectedChoiceIds - validChoiceIds

                if (invalidChoiceIds.isNotEmpty()) {
                    throw InvalidChoiceException(invalidChoiceIds)
                }

                Answer.createChoiceAnswer(
                    questionId = question.id,
                    questionTitle = question.title,
                    questionType = question.type,
                    selectedChoiceIds = selectedChoiceIds,
                )
            }
            else -> throw InvalidAnswerException("알 수 없는 질문 타입: ${question.type}")
        }
    }
}
