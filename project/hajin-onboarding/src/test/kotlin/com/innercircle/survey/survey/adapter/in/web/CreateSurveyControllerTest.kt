package com.innercircle.survey.survey.adapter.`in`.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.innercircle.survey.survey.adapter.`in`.web.dto.CreateSurveyRequest
import com.innercircle.survey.survey.application.port.`in`.CreateSurveyUseCase
import com.innercircle.survey.survey.domain.Question
import com.innercircle.survey.survey.domain.QuestionType
import com.innercircle.survey.survey.domain.Survey
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(CreateSurveyController::class)
class CreateSurveyControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper,
    @MockkBean private val createSurveyUseCase: CreateSurveyUseCase,
) : DescribeSpec({
        describe("POST /api/v1/surveys") {
            context("유효한 설문조사 생성 요청") {
                val request =
                    CreateSurveyRequest(
                        title = "고객 만족도 조사",
                        description = "서비스 개선을 위한 설문조사입니다.",
                        questions =
                            listOf(
                                CreateSurveyRequest.QuestionRequest(
                                    title = "서비스 만족도",
                                    description = "전반적인 서비스 만족도를 평가해주세요.",
                                    type = "SINGLE_CHOICE",
                                    required = true,
                                    choices = listOf("매우 만족", "만족", "보통", "불만족", "매우 불만족"),
                                ),
                                CreateSurveyRequest.QuestionRequest(
                                    title = "개선 사항",
                                    description = "서비스 개선이 필요한 부분을 자유롭게 작성해주세요.",
                                    type = "LONG_TEXT",
                                    required = false,
                                ),
                            ),
                    )

                it("201 Created와 설문조사 정보를 반환해야 한다") {
                    val survey =
                        Survey.create(
                            title = request.title,
                            description = request.description,
                            questions =
                                listOf(
                                    Question.create(
                                        title = "서비스 만족도",
                                        description = "전반적인 서비스 만족도를 평가해주세요.",
                                        type = QuestionType.SINGLE_CHOICE,
                                        required = true,
                                        choices = listOf("매우 만족", "만족", "보통", "불만족", "매우 불만족"),
                                    ),
                                    Question.create(
                                        title = "개선 사항",
                                        description = "서비스 개선이 필요한 부분을 자유롭게 작성해주세요.",
                                        type = QuestionType.LONG_TEXT,
                                        required = false,
                                    ),
                                ),
                        )

                    every { createSurveyUseCase.createSurvey(any()) } returns survey

                    mockMvc.perform(
                        post("/api/v1/surveys")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)),
                    )
                        .andExpect(status().isCreated)
                        .andExpect(jsonPath("$.success").value(true))
                        .andExpect(jsonPath("$.data.title").value(request.title))
                        .andExpect(jsonPath("$.data.description").value(request.description))
                        .andExpect(jsonPath("$.data.questions").isArray)
                        .andExpect(jsonPath("$.data.questions.length()").value(2))
                        .andExpect(jsonPath("$.message").value("설문조사가 성공적으로 생성되었습니다."))
                }
            }

            context("제목이 없는 요청") {
                val request =
                    mapOf(
                        "description" to "설명",
                        "questions" to
                            listOf(
                                mapOf(
                                    "title" to "질문1",
                                    "description" to "설명1",
                                    "type" to "SHORT_TEXT",
                                    "required" to true,
                                ),
                            ),
                    )

                it("400 Bad Request를 반환해야 한다") {
                    mockMvc.perform(
                        post("/api/v1/surveys")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)),
                    )
                        .andExpect(status().isBadRequest)
                        .andExpect(jsonPath("$.errorCode").value("COMMON_001"))
                }
            }

            context("질문이 없는 요청") {
                val request =
                    CreateSurveyRequest(
                        title = "설문조사",
                        description = "설명",
                        questions = emptyList(),
                    )

                it("400 Bad Request를 반환해야 한다") {
                    mockMvc.perform(
                        post("/api/v1/surveys")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)),
                    )
                        .andExpect(status().isBadRequest)
                        .andExpect(jsonPath("$.errorCode").value("COMMON_001"))
                }
            }

            context("질문이 10개를 초과하는 요청") {
                val questions =
                    (1..11).map { index ->
                        CreateSurveyRequest.QuestionRequest(
                            title = "질문 $index",
                            description = "설명 $index",
                            type = "SHORT_TEXT",
                            required = false,
                        )
                    }

                val request =
                    CreateSurveyRequest(
                        title = "너무 많은 질문",
                        description = "11개의 질문",
                        questions = questions,
                    )

                it("400 Bad Request를 반환해야 한다") {
                    mockMvc.perform(
                        post("/api/v1/surveys")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)),
                    )
                        .andExpect(status().isBadRequest)
                        .andExpect(jsonPath("$.errorCode").value("COMMON_001"))
                }
            }

            context("제목이 200자를 초과하는 요청") {
                val longTitle = "a".repeat(201)
                val request =
                    CreateSurveyRequest(
                        title = longTitle,
                        description = "설명",
                        questions =
                            listOf(
                                CreateSurveyRequest.QuestionRequest(
                                    title = "질문",
                                    description = "설명",
                                    type = "SHORT_TEXT",
                                    required = false,
                                ),
                            ),
                    )

                it("400 Bad Request를 반환해야 한다") {
                    mockMvc.perform(
                        post("/api/v1/surveys")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)),
                    )
                        .andExpect(status().isBadRequest)
                        .andExpect(jsonPath("$.errorCode").value("COMMON_001"))
                }
            }

            context("선택지가 20개를 초과하는 요청") {
                val choices = (1..21).map { "선택지 $it" }
                val request =
                    CreateSurveyRequest(
                        title = "설문조사",
                        description = "설명",
                        questions =
                            listOf(
                                CreateSurveyRequest.QuestionRequest(
                                    title = "너무 많은 선택지",
                                    description = "21개의 선택지",
                                    type = "MULTIPLE_CHOICE",
                                    required = true,
                                    choices = choices,
                                ),
                            ),
                    )

                it("400 Bad Request를 반환해야 한다") {
                    mockMvc.perform(
                        post("/api/v1/surveys")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)),
                    )
                        .andExpect(status().isBadRequest)
                        .andExpect(jsonPath("$.errorCode").value("COMMON_001"))
                }
            }
        }
    })
