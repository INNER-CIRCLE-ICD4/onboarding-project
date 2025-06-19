package fc.innercircle.sanghyukonboarding.form.interfaces.rest

import com.fasterxml.jackson.databind.ObjectMapper
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import fc.innercircle.sanghyukonboarding.configuration.MysqlTestContainerConfig
import fc.innercircle.sanghyukonboarding.form.interfaces.rest.port.dto.request.FormRequest
import fc.innercircle.sanghyukonboarding.form.domain.model.Form
import fc.innercircle.sanghyukonboarding.form.domain.model.vo.InputType
import fc.innercircle.sanghyukonboarding.form.service.port.FormQueryRepository
import fc.innercircle.sanghyukonboarding.form.service.port.FormCommandRepository
import io.kotest.core.spec.style.BehaviorSpec
import org.hamcrest.CoreMatchers.endsWith
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.TestContextManager
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

@Import(MysqlTestContainerConfig::class)
@AutoConfigureMockMvc
@SpringBootTest
class FormControllerIntegrationTest : BehaviorSpec({

    // Get dependencies from Spring context
    val applicationContext = TestContextManager(FormControllerIntegrationTest::class.java)
        .testContext.applicationContext
    val mockMvc = applicationContext.getBean(MockMvc::class.java)
    val objectMapper = applicationContext.getBean(ObjectMapper::class.java)
    val formCommandRepository = applicationContext.getBean(FormCommandRepository::class.java)
    val formQueryRepository = applicationContext.getBean(FormQueryRepository::class.java)

    context("설문 조사 작성 API 테스트") {

        // DB 초기화
        formCommandRepository.deleteAll()

        given("유효한 요청 값으로") {
            // Given
            // 필요한 데이터 준비
            val cmd = FormRequest(
                title = "티셔츠 신청",
                description = "티셔츠를 신청하려면 이름 및 사이즈를 입력하세요.",
                questions = listOf(
                    FormRequest.QuestionRequest(
                        title = "이름",
                        description = "반드시 본명을 입력해주세요.",
                        type = InputType.TEXT.name,
                        required = true
                    ),
                    FormRequest.QuestionRequest(
                        title = "티셔츠 사이즈",
                        type = InputType.RADIO.name,
                        required = false,
                        selectableOptions = listOf(
                            FormRequest.QuestionRequest.SelectableOptionRequest(text = "XS"),
                            FormRequest.QuestionRequest.SelectableOptionRequest(text = "S"),
                            FormRequest.QuestionRequest.SelectableOptionRequest(text = "M"),
                            FormRequest.QuestionRequest.SelectableOptionRequest(text = "L"),
                            FormRequest.QuestionRequest.SelectableOptionRequest(text = "XL")
                        )
                    ),
                    FormRequest.QuestionRequest(
                        title = "기타 의견",
                        type = InputType.LONG_TEXT.name,
                        required = false,
                    )
                )
            )

            `when`("설문 조사 작성 API를 호출하면") {

                val result: ResultActionsDsl = mockMvc.post("/api/v1/forms") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(cmd)
                }
                val locationHeader = result.andReturn().response.getHeader("Location")
                val formId = locationHeader?.substringAfterLast("/") ?:
                throw IllegalStateException("Location header is missing or malformed")

                then("201 CREATED 응답을 반환해야 한다.") {
                    result.andExpect {
                        status { isCreated() }
                    }
                }

                then("Location 헤더에 생성된 설문 조사 ID가 포함되어야 한다.") {
                    result.andExpect {
                        header { exists("Location") }
                        header { string("Location", endsWith("/api/v1/forms/$formId")) }
                    }
                }

                then("응답받은 설문 조사 ID로 GET API를 호출하면, 요청한 내용과 동일한 설문 조사가 반환되어야 하며, 버전은 모두 0이어야 한다.") {
                    // 작성된 설문 조사 조회
                    val result: ResultActionsDsl = mockMvc.get("/api/v1/forms/$formId") {
                        accept = MediaType.APPLICATION_JSON
                    }

                    result.andExpect {
                        status { isOk() }
                        jsonPath("$.title") { value(cmd.title) }
                        jsonPath("$.description") { value(cmd.description) }
                        jsonPath("$.questions.size()") { value(cmd.questions.size) }
                        cmd.questions.forEachIndexed { idx1, question ->
                            jsonPath("$.questions[$idx1].title") { value(question.title) }
                            jsonPath("$.questions[$idx1].description") { value(question.description) }
                            jsonPath("$.questions[$idx1].type") { value(question.type.uppercase()) }
                            jsonPath("$.questions[$idx1].required") { value(question.required) }
                            jsonPath("$.questions[$idx1].version") { value(0) }
                            jsonPath("$.questions[$idx1].selectableOptions.size()") { value(question.selectableOptions.size) }
                            question.selectableOptions.forEachIndexed { idx2, option ->
                                jsonPath("$.questions[$idx1].selectableOptions[$idx2].text") { value(option.text) }
                            }
                        }
                    }.andDo {
                        print()
                    }
                }
            }
        }

        given("빈 요청 값으로") {
            `when`("설문 조사 작성 API를 호출하면") {

                val result: ResultActionsDsl = mockMvc.post("/api/v1/forms") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString({})
                }

                then("${ErrorCode.INVALID_REQUEST.status.name} 응답을 반환해야 한다.") {
                    result.andExpect {
                        status { isBadRequest() }
                        jsonPath("$.code") { value(ErrorCode.INVALID_REQUEST.name) }
                        jsonPath("$.message") { value(ErrorCode.INVALID_REQUEST.message) }
                    }
                }
            }
        }
    }

    context("설문 조사 조회 API 테스트") {

        // DB 초기화
        formCommandRepository.deleteAll()

        given("존재하는 설문조사에 대해") {

            val cmd = FormRequest(
                title = "티셔츠 신청",
                description = "티셔츠를 신청하려면 이름 및 사이즈를 입력하세요.",
                questions = listOf(
                    FormRequest.QuestionRequest(
                        title = "이름",
                        description = "반드시 본명을 입력해주세요.",
                        type = InputType.TEXT.name,
                        required = true
                    ),
                    FormRequest.QuestionRequest(
                        title = "티셔츠 사이즈",
                        type = InputType.RADIO.name,
                        required = false,
                        selectableOptions = listOf(
                            FormRequest.QuestionRequest.SelectableOptionRequest(text = "XS"),
                            FormRequest.QuestionRequest.SelectableOptionRequest(text = "S"),
                            FormRequest.QuestionRequest.SelectableOptionRequest(text = "M"),
                            FormRequest.QuestionRequest.SelectableOptionRequest(text = "L"),
                            FormRequest.QuestionRequest.SelectableOptionRequest(text = "XL")
                        )
                    ),
                    FormRequest.QuestionRequest(
                        title = "기타 의견",
                        type = InputType.LONG_TEXT.name,
                        required = false,
                    )
                )
            )
            val formId: String = formCommandRepository.insertOrUpdate(Form.from(cmd))

            `when`("설문 조사 조회 API를 호출하면") {

                val result: ResultActionsDsl = mockMvc.get("/api/v1/forms/${formId}") {
                    accept = MediaType.APPLICATION_JSON
                }

                then("200 OK 응답을 반환해야 한다.") {
                    result.andExpect {
                        status { isOk() }
                    }
                }

                then("응답 본문에 요청한 설문 조사의 내용이 포함되어야 한다.") {
                    result.andExpect {
                        jsonPath("$.title") { value(cmd.title) }
                        jsonPath("$.description") { value(cmd.description) }
                        jsonPath("$.questions.size()") { value(cmd.questions.size) }
                        cmd.questions.forEachIndexed { idx1, question ->
                            jsonPath("$.questions[$idx1].title") { value(question.title) }
                            jsonPath("$.questions[$idx1].description") { value(question.description) }
                            jsonPath("$.questions[$idx1].type") { value(question.type.uppercase()) }
                            jsonPath("$.questions[$idx1].required") { value(question.required) }
                            jsonPath("$.questions[$idx1].version") { value(0) } // 버전은 0이어야 함
                            jsonPath("$.questions[$idx1].selectableOptions.size()") { value(question.selectableOptions.size) }
                            question.selectableOptions.forEachIndexed { idx2, option ->
                                jsonPath("$.questions[$idx1].selectableOptions[$idx2].text") { value(option.text) }
                            }
                        }
                    }.andDo {
                        print()
                    }
                }
            }
        }
    }

    context("설문 조사 수정 API 테스트") {

        formCommandRepository.deleteAll()

        given("존재하는 설문조사에 대해") {

            val cmd = FormRequest(
                title = "티셔츠 신청",
                description = "티셔츠를 신청하려면 이름 및 사이즈를 입력하세요.",
                questions = listOf(
                    FormRequest.QuestionRequest(
                        title = "이름",
                        description = "반드시 본명을 입력해주세요.",
                        type = InputType.TEXT.name,
                        required = true
                    ),
                    FormRequest.QuestionRequest(
                        title = "티셔츠 사이즈",
                        type = InputType.RADIO.name,
                        required = false,
                        selectableOptions = listOf(
                            FormRequest.QuestionRequest.SelectableOptionRequest(text = "XS"),
                            FormRequest.QuestionRequest.SelectableOptionRequest(text = "S"),
                            FormRequest.QuestionRequest.SelectableOptionRequest(text = "M"),
                            FormRequest.QuestionRequest.SelectableOptionRequest(text = "L"),
                            FormRequest.QuestionRequest.SelectableOptionRequest(text = "XL")
                        )
                    ),
                    FormRequest.QuestionRequest(
                        title = "기타 의견",
                        type = InputType.LONG_TEXT.name,
                        required = false,
                    )
                )
            )
            val formId: String = formCommandRepository.insertOrUpdate(Form.from(cmd))
            val form: Form = formQueryRepository.getById(formId)

            `when`("설문 조사 수정 API를 호출하면") {

                val updateCmd = FormRequest(
                    title = "티셔츠 신청 (수정)",
                    description = "티셔츠를 신청하려면 이름 및 사이즈를 입력하세요. (수정됨)",
                    questions = listOf(
                        FormRequest.QuestionRequest(
                            title = "(추가 문항) 귀하의 키는 몇 cm 인가요?",
                            type = InputType.CHECKBOX.name,
                            required = false,
                            selectableOptions = listOf(
                                FormRequest.QuestionRequest.SelectableOptionRequest(
                                    text = "180cm"
                                ),
                                FormRequest.QuestionRequest.SelectableOptionRequest(
                                    text = "170cm"
                                ),
                                FormRequest.QuestionRequest.SelectableOptionRequest(
                                    text = "160cm"
                                ),
                                FormRequest.QuestionRequest.SelectableOptionRequest(
                                    text = "150cm"
                                ),
                            ),
                        ),
                        FormRequest.QuestionRequest(
                            questionTemplateId = form.questionTemplates.list()[0].id,
                            title = "이름 (수정)",
                            description = "반드시 본명을 입력해주세요. (수정)",
                            type = InputType.TEXT.name,
                            required = true,
                        ),
                        FormRequest.QuestionRequest(
                            questionTemplateId = form.questionTemplates.list()[1].id,
                            title = "티셔츠 사이즈",
                            type = InputType.CHECKBOX.name,
                            required = false,
                            selectableOptions = listOf(
                                FormRequest.QuestionRequest.SelectableOptionRequest(
                                    text = "XL"
                                ),
                                FormRequest.QuestionRequest.SelectableOptionRequest(
                                    text = "L"
                                ),
                                FormRequest.QuestionRequest.SelectableOptionRequest(
                                    text = "M"
                                ),
                                FormRequest.QuestionRequest.SelectableOptionRequest(
                                    text = "S"
                                ),
                                FormRequest.QuestionRequest.SelectableOptionRequest(
                                    text = "XS"
                                ),
                            ),
                        ),
                    )
                )

                val result: ResultActionsDsl = mockMvc.put("/api/v1/forms/${formId}") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(updateCmd)
                }

                then("200 OK 응답을 반환해야 한다.") {
                    result.andExpect {
                        status { isOk() }
                    }
                }

                then("응답 본문에는 수정된 설문 조사의 내용이 포함되어야 한다.") {
                    result.andExpect {
                        jsonPath("$.title") { value(updateCmd.title) }
                        jsonPath("$.description") { value(updateCmd.description) }
                        jsonPath("$.questions.size()") { value(updateCmd.questions.size) }
                        updateCmd.questions.forEachIndexed { idx1, question ->
                            jsonPath("$.questions[$idx1].title") { value(question.title) }
                            jsonPath("$.questions[$idx1].description") { value(question.description) }
                            jsonPath("$.questions[$idx1].type") { value(question.type.uppercase()) }
                            jsonPath("$.questions[$idx1].required") { value(question.required) }
                            // 요청 값에 questionTemplateId가 있으면, 기존 질문을 업데이트 하는 것이므로 기존 버전 + 1 이어야 하고, questionTemplateId가 없으면 새 질문이므로 0이어야 함
                            jsonPath("$.questions[$idx1].version") { value(if (question.questionTemplateId.isNotBlank()) 1 else 0) }
                            jsonPath("$.questions[$idx1].selectableOptions.size()") { value(question.selectableOptions.size) }
                            question.selectableOptions.forEachIndexed { idx2, option ->
                                jsonPath("$.questions[$idx1].selectableOptions[$idx2].text") { value(option.text) }
                            }
                        }
                    }.andDo {
                        print()
                    }
                }
            }
        }
    }
})
