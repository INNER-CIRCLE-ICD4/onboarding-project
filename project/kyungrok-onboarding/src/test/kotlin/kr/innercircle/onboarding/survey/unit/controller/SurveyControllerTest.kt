package kr.innercircle.onboarding.survey.unit.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kr.innercircle.onboarding.survey.controller.SurveyController
import kr.innercircle.onboarding.survey.domain.Survey
import kr.innercircle.onboarding.survey.domain.SurveyItemInputType
import kr.innercircle.onboarding.survey.dto.request.CreateSurveyItemOptionRequest
import kr.innercircle.onboarding.survey.dto.request.CreateSurveyItemRequest
import kr.innercircle.onboarding.survey.dto.request.CreateSurveyRequest
import kr.innercircle.onboarding.survey.service.SurveyService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.Mockito.never
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

/**
 * packageName : kr.innercircle.onboarding.survey.unit.controller
 * fileName    : SurveyControllerTest
 * author      : ckr
 * date        : 25. 6. 21.
 * description :
 */

@ExtendWith(SpringExtension::class)
@WebMvcTest(SurveyController::class)
class SurveyControllerTest {
    private val objectMapper = ObjectMapper()

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    lateinit var surveyService: SurveyService

    @Test
    fun 정상적인_설문조사_생성_요청시_201_상태코드와_성공_메시지를_반환한다() {
        // Given
        val validRequest = createValidSurveyRequest()
        val createdSurvey = Survey(
            id = 1L,
            name = "테스트 설문조사",
            description = "테스트 설명"
        )

        given(surveyService.createSurvey(validRequest)).willReturn(createdSurvey)

        // When & Then
        mockMvc.perform(
            post("/surveys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest))
        )
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("설문조사가 생성되었습니다."))
            .andExpect(jsonPath("$.data").doesNotExist())
            .andExpect(jsonPath("$.error").doesNotExist())

        verify(surveyService).createSurvey(validRequest)
    }

    @Test
    fun 설문조사_이름이_비어있으면_400_상태코드를_반환한다() {
        // Given
        val invalidRequest = createValidSurveyRequest().copy(name = "")

        // When & Then
        mockMvc.perform(
            post("/surveys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest))
        )
            .andExpect(status().isBadRequest)

        verify(surveyService, never()).createSurvey(invalidRequest)
    }

    @Test
    @DisplayName("설문조사 이름이 null이면 400 상태코드를 반환한다")
    fun `should return 400 when survey name is null`() {
        // Given
        val invalidRequestJson = """
            {
                "name": null,
                "description": "테스트 설명",
                "surveyItems": [
                    {
                        "name": "질문1",
                        "description": "질문1 설명",
                        "inputType": "SHORT_TEXT",
                        "options": [{"option": "옵션1"}],
                        "isRequired": true
                    }
                ]
            }
        """.trimIndent()

        // When & Then
        mockMvc.perform(
            post("/surveys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequestJson)
        )
            .andExpect(status().isBadRequest)

        verify(surveyService, never()).createSurvey(any())
    }

    @Test
    @DisplayName("설문조사 항목이 비어있으면 400 상태코드를 반환한다")
    fun `should return 400 when survey items is empty`() {
        // Given
        val invalidRequest = createValidSurveyRequest().copy(surveyItems = emptyList())

        // When & Then
        mockMvc.perform(
            post("/surveys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest))
        )
            .andExpect(status().isBadRequest)

        verify(surveyService, never()).createSurvey(invalidRequest)
    }

    @Test
    @DisplayName("설문조사 항목이 10개를 초과하면 400 상태코드를 반환한다")
    fun `should return 400 when survey items exceed maximum size`() {
        // Given
        val tooManyItems = (1..11).map { createValidSurveyItemRequest("질문$it") }
        val invalidRequest = createValidSurveyRequest().copy(surveyItems = tooManyItems)

        // When & Then
        mockMvc.perform(
            post("/surveys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest))
        )
            .andExpect(status().isBadRequest)

        verify(surveyService, never()).createSurvey(invalidRequest)
    }

    @Test
    @DisplayName("설문조사 항목이 null이면 400 상태코드를 반환한다")
    fun `should return 400 when survey items is null`() {
        // Given
        val invalidRequestJson = """
            {
                "name": "테스트 설문조사",
                "description": "테스트 설명",
                "surveyItems": null
            }
        """.trimIndent()

        // When & Then
        mockMvc.perform(
            post("/surveys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequestJson)
        )
            .andExpect(status().isBadRequest)

        verify(surveyService, never()).createSurvey(any())
    }

    @Test
    @DisplayName("설문조사 항목 이름이 비어있으면 400 상태코드를 반환한다")
    fun `should return 400 when survey item name is blank`() {
        // Given
        val invalidSurveyItem = createValidSurveyItemRequest("").copy(name = "")
        val invalidRequest = createValidSurveyRequest().copy(surveyItems = listOf(invalidSurveyItem))

        // When & Then
        mockMvc.perform(
            post("/surveys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest))
        )
            .andExpect(status().isBadRequest)

        verify(surveyService, never()).createSurvey(invalidRequest)
    }

    @Test
    @DisplayName("설문조사 항목의 입력 타입이 null이면 400 상태코드를 반환한다")
    fun `should return 400 when survey item input type is null`() {
        // Given
        val invalidRequestJson = """
            {
                "name": "테스트 설문조사",
                "description": "테스트 설명",
                "surveyItems": [
                    {
                        "name": "질문1",
                        "description": "질문1 설명",
                        "inputType": null,
                        "options": [{"option": "옵션1"}],
                        "isRequired": true
                    }
                ]
            }
        """.trimIndent()

        // When & Then
        mockMvc.perform(
            post("/surveys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequestJson)
        )
            .andExpect(status().isBadRequest)

        verify(surveyService, never()).createSurvey(any())
    }

    @Test
    @DisplayName("설문조사 항목의 옵션이 null이면 400 상태코드를 반환한다")
    fun `should return 400 when survey item options is null`() {
        // Given
        val invalidRequestJson = """
            {
                "name": "테스트 설문조사",
                "description": "테스트 설명",
                "surveyItems": [
                    {
                        "name": "질문1",
                        "description": "질문1 설명",
                        "inputType": "SHORT_TEXT",
                        "options": null,
                        "isRequired": true
                    }
                ]
            }
        """.trimIndent()

        // When & Then
        mockMvc.perform(
            post("/surveys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequestJson)
        )
            .andExpect(status().isBadRequest)

        verify(surveyService, never()).createSurvey(any())
    }

    @Test
    @DisplayName("설문조사 항목 옵션이 비어있으면 400 상태코드를 반환한다")
    fun `should return 400 when survey item option is blank`() {
        // Given
        val invalidOption = CreateSurveyItemOptionRequest("")
        val invalidSurveyItem = createValidSurveyItemRequest("질문1").copy(options = listOf(invalidOption))
        val invalidRequest = createValidSurveyRequest().copy(surveyItems = listOf(invalidSurveyItem))

        // When & Then
        mockMvc.perform(
            post("/surveys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest))
        )
            .andExpect(status().isBadRequest)

        verify(surveyService, never()).createSurvey(invalidRequest)
    }

    @Test
    @DisplayName("설문조사 항목의 필수 여부가 null이면 400 상태코드를 반환한다")
    fun `should return 400 when survey item isRequired is null`() {
        // Given
        val invalidRequestJson = """
            {
                "name": "테스트 설문조사",
                "description": "테스트 설명",
                "surveyItems": [
                    {
                        "name": "질문1",
                        "description": "질문1 설명",
                        "inputType": "SHORT_TEXT",
                        "options": [{"option": "옵션1"}],
                        "isRequired": null
                    }
                ]
            }
        """.trimIndent()

        // When & Then
        mockMvc.perform(
            post("/surveys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequestJson)
        )
            .andExpect(status().isBadRequest)

        verify(surveyService, never()).createSurvey(any())
    }

    @Test
    @DisplayName("잘못된 JSON 형식이면 400 상태코드를 반환한다")
    fun `should return 400 when request body has invalid JSON format`() {
        // Given
        val invalidJson = """
            {
                "name": "테스트 설문조사",
                "description": "테스트 설명",
                "surveyItems": [
                    {
                        "name": "질문1",
                        "description": "질문1 설명",
                        "inputType": "SHORT_TEXT",
                        "options": [{"option": "옵션1"}],
                        "isRequired": true
                    }
                // 누락된 닫는 괄호
            }
        """.trimIndent()

        // When & Then
        mockMvc.perform(
            post("/surveys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson)
        )
            .andExpect(status().isBadRequest)

        verify(surveyService, never()).createSurvey(any())
    }

    @Test
    @DisplayName("Content-Type이 application/json이 아니면 415 상태코드를 반환한다")
    fun `should return 415 when content type is not application json`() {
        // Given
        val validRequest = createValidSurveyRequest()

        // When & Then
        mockMvc.perform(
            post("/surveys")
                .contentType(MediaType.APPLICATION_XML)
                .content(objectMapper.writeValueAsString(validRequest))
        )
            .andExpect(status().isUnsupportedMediaType)

        verify(surveyService, never()).createSurvey(any())
    }

    @Test
    @DisplayName("요청 본문이 비어있으면 400 상태코드를 반환한다")
    fun `should return 400 when request body is empty`() {
        // When & Then
        mockMvc.perform(
            post("/surveys")
                .contentType(MediaType.APPLICATION_JSON)
                .content("")
        )
            .andExpect(status().isBadRequest)

        verify(surveyService, never()).createSurvey(any())
    }

    @Test
    @DisplayName("서비스에서 예외가 발생하면 예외가 전파된다")
    fun `should propagate exception when service throws exception`() {
        // Given
        val validRequest = createValidSurveyRequest()
        given(surveyService.createSurvey(validRequest))
            .willThrow(RuntimeException("서비스 오류"))

        // When & Then
        try {
            mockMvc.perform(
                post("/surveys")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validRequest))
            )
        } catch (e: Exception) {
            // 예외가 발생하는 것이 정상
        }

        verify(surveyService).createSurvey(validRequest)
    }

    @Test
    @DisplayName("모든 입력 타입에 대해 정상적으로 처리된다")
    fun `should handle all input types successfully`() {
        // Given
        val inputTypes = SurveyItemInputType.entries
        inputTypes.forEach { inputType ->
            val request = createValidSurveyRequest().copy(
                surveyItems = listOf(
                    createValidSurveyItemRequest("질문").copy(inputType = inputType)
                )
            )
            val createdSurvey = Survey(
                id = 1L,
                name = "테스트 설문조사",
                description = "테스트 설명"
            )

            given(surveyService.createSurvey(request)).willReturn(createdSurvey)

            // When & Then
            mockMvc.perform(
                post("/surveys")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("설문조사가 생성되었습니다."))

            verify(surveyService).createSurvey(request)
        }
    }

    @Test
    @DisplayName("description이 null이어도 정상적으로 처리된다")
    fun `should handle null description successfully`() {
        // Given
        val requestWithNullDescription = createValidSurveyRequest().copy(description = null)
        val createdSurvey = Survey(
            id = 1L,
            name = "테스트 설문조사",
            description = null
        )

        given(surveyService.createSurvey(requestWithNullDescription)).willReturn(createdSurvey)

        // When & Then
        mockMvc.perform(
            post("/surveys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestWithNullDescription))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("설문조사가 생성되었습니다."))

        verify(surveyService).createSurvey(requestWithNullDescription)
    }

    private fun createValidSurveyRequest(): CreateSurveyRequest {
        return CreateSurveyRequest(
            name = "테스트 설문조사",
            description = "테스트 설명",
            surveyItems = listOf(createValidSurveyItemRequest("질문1"))
        )
    }

    private fun createValidSurveyItemRequest(name: String): CreateSurveyItemRequest {
        return CreateSurveyItemRequest(
            name = name,
            description = "질문 설명",
            inputType = SurveyItemInputType.SHORT_TEXT,
            options = listOf(CreateSurveyItemOptionRequest("옵션1")),
            isRequired = true
        )
    }
}

