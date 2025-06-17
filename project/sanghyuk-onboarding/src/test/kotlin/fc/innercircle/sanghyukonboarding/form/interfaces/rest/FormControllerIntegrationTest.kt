package fc.innercircle.sanghyukonboarding.form.interfaces.rest

import com.fasterxml.jackson.databind.ObjectMapper
import fc.innercircle.sanghyukonboarding.common.domain.exception.ErrorCode
import fc.innercircle.sanghyukonboarding.configuration.MysqlTestContainerConfig
import fc.innercircle.sanghyukonboarding.form.domain.dto.command.FormCommand
import fc.innercircle.sanghyukonboarding.form.domain.model.Form
import fc.innercircle.sanghyukonboarding.form.domain.model.vo.InputType
import fc.innercircle.sanghyukonboarding.form.service.port.FormReader
import fc.innercircle.sanghyukonboarding.form.service.port.FormWriter
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


@Import(MysqlTestContainerConfig::class)
@AutoConfigureMockMvc
@SpringBootTest
class FormControllerIntegrationTest : BehaviorSpec({

    // Get dependencies from Spring context
    val applicationContext = TestContextManager(FormControllerIntegrationTest::class.java)
        .testContext.applicationContext
    val mockMvc = applicationContext.getBean(MockMvc::class.java)
    val objectMapper = applicationContext.getBean(ObjectMapper::class.java)
    val formWriter = applicationContext.getBean(FormWriter::class.java)
    val formReader = applicationContext.getBean(FormReader::class.java)

    context("설문 조사 작성 API 테스트") {

        // DB 초기화
        formWriter.deleteAll()

        given("유효한 요청 값으로") {
            // Given
            // 필요한 데이터 준비
            val cmd = FormCreateCommand(
                title = "티셔츠 신청",
                description = "티셔츠를 신청하려면 이름 및 사이즈를 입력하세요.",
                questions = listOf(
                    FormCreateCommand.Question(
                        title = "이름",
                        description = "반드시 본명을 입력해주세요.",
                        type = InputType.TEXT.name,
                        required = true
                    ),
                    FormCreateCommand.Question(
                        title = "티셔츠 사이즈",
                        type = InputType.RADIO.name,
                        required = false,
                        selectableOptions = listOf(
                            FormCreateCommand.Question.SelectableOption(text = "XS"),
                            FormCreateCommand.Question.SelectableOption(text = "S"),
                            FormCreateCommand.Question.SelectableOption(text = "M"),
                            FormCreateCommand.Question.SelectableOption(text = "L"),
                            FormCreateCommand.Question.SelectableOption(text = "XL")
                        )
                    ),
                    FormCreateCommand.Question(
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
        formWriter.deleteAll()

        given("존재하는 설문조사에 대해") {

            val cmd = FormCreateCommand(
                title = "티셔츠 신청",
                description = "티셔츠를 신청하려면 이름 및 사이즈를 입력하세요.",
                questions = listOf(
                    FormCreateCommand.Question(
                        title = "이름",
                        description = "반드시 본명을 입력해주세요.",
                        type = InputType.TEXT.name,
                        required = true
                    ),
                    FormCreateCommand.Question(
                        title = "티셔츠 사이즈",
                        type = InputType.RADIO.name,
                        required = false,
                        selectableOptions = listOf(
                            FormCreateCommand.Question.SelectableOption(text = "XS"),
                            FormCreateCommand.Question.SelectableOption(text = "S"),
                            FormCreateCommand.Question.SelectableOption(text = "M"),
                            FormCreateCommand.Question.SelectableOption(text = "L"),
                            FormCreateCommand.Question.SelectableOption(text = "XL")
                        )
                    ),
                    FormCreateCommand.Question(
                        title = "기타 의견",
                        type = InputType.LONG_TEXT.name,
                        required = false,
                    )
                )
            )
            val formId: String = formWriter.insertOrUpdate(Form.from(cmd))

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

    context("설문 조사 수정 API 테슽") {

        formWriter.deleteAll()

        given("존재하는 설문조사에 대해") {

            val cmd = FormCreateCommand(
                title = "티셔츠 신청",
                description = "티셔츠를 신청하려면 이름 및 사이즈를 입력하세요.",
                questions = listOf(
                    FormCreateCommand.Question(
                        title = "이름",
                        description = "반드시 본명을 입력해주세요.",
                        type = InputType.TEXT.name,
                        required = true
                    ),
                    FormCreateCommand.Question(
                        title = "티셔츠 사이즈",
                        type = InputType.RADIO.name,
                        required = false,
                        selectableOptions = listOf(
                            FormCreateCommand.Question.SelectableOption(text = "XS"),
                            FormCreateCommand.Question.SelectableOption(text = "S"),
                            FormCreateCommand.Question.SelectableOption(text = "M"),
                            FormCreateCommand.Question.SelectableOption(text = "L"),
                            FormCreateCommand.Question.SelectableOption(text = "XL")
                        )
                    ),
                    FormCreateCommand.Question(
                        title = "기타 의견",
                        type = InputType.LONG_TEXT.name,
                        required = false,
                    )
                )
            )
            val formId: String = formWriter.insertOrUpdate(Form.from(cmd))
            val form: Form = formReader.getById(formId)

            `when`("설문 조사 수정 API를 호출하면") {

                val updateCmd = FormCommand(
                    title = "수정된 티셔츠 신청",
                    description = "수정된 티셔츠를 신청하려면 이름 및 사이즈를 입력하세요.",
                    questions = listOf(
                        FormCommand.Question(
                            questionTemplateId = form.questionTemplates.list()[0].id,
                            title = "이름",
                            description = "반드시 본명을 입력해주세요.",
                            type = InputType.TEXT.name,
                            required = true,
                            version = form.questionTemplates.list()[0].version,
                        ),
                        FormCommand.Question(
                            questionTemplateId = form.questionTemplates.list()[1].id,
                            title = "티셔츠 사이즈",
                            type = InputType.RADIO.name,
                            required = false,
                            selectableOptions = listOf(
                                FormCommand.Question.SelectableOption(
                                    selectableOptionId = form.questionTemplates.list()[1].snapshots.list()[0].selectableOptions.list()[0].id,
                                    text = "XS"
                                ),
                                FormCommand.Question.SelectableOption(
                                    selectableOptionId = form.questionTemplates.list()[1].snapshots.list()[0].selectableOptions.list()[1].id,
                                    text = "S"
                                ),
                                FormCommand.Question.SelectableOption(
                                    selectableOptionId = form.questionTemplates.list()[1].snapshots.list()[0].selectableOptions.list()[2].id,
                                    text = "M"
                                ),
                                FormCommand.Question.SelectableOption(
                                    selectableOptionId = form.questionTemplates.list()[1].snapshots.list()[0].selectableOptions.list()[3].id,
                                    text = "L"
                                ),
                                FormCommand.Question.SelectableOption(
                                    selectableOptionId = form.questionTemplates.list()[1].snapshots.list()[0].selectableOptions.list()[4].id,
                                    text = "XL"
                                ),
                            ),
                            version = form.questionTemplates.list()[1].version,
                        ),
                        FormCommand.Question(
                            questionTemplateId = form.questionTemplates.list()[2].id,
                            title = "기타 의견",
                            type = InputType.LONG_TEXT.name,
                            required = false,
                            version = form.questionTemplates.list()[2].version,
                        )
                    )
                )

                val result: ResultActionsDsl = mockMvc.post("/api/v1/forms/${formId}") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(updateCmd)
                }

                then("200 OK 응답을 반환해야 한다.") {
                    result.andExpect {
                        status { isOk() }
                    }
                }

                then("응답 본문에 수정된 설문 조사의 내용이 포함되어야 한다.") {
                    result.andExpect {
                        jsonPath("$.title") { value(updateCmd.title) }
                        jsonPath("$.description") { value(updateCmd.description) }
                        jsonPath("$.questions.size()") { value(updateCmd.questions.size) }
                        updateCmd.questions.forEachIndexed { idx1, question ->
                            jsonPath("$.questions[$idx1].title") { value(question.title) }
                            jsonPath("$.questions[$idx1].description") { value(question.description) }
                            jsonPath("$.questions[$idx1].type") { value(question.type.uppercase()) }
                            jsonPath("$.questions[$idx1].required") { value(question.required) }
                            jsonPath("$.questions[$idx1].version") { value(0) } // 버전은 0이어야 함
                            jsonPath("$.questions[$idx1].selectableOptions.size()") { value(question.selectableOptionCommands.size) }
                            question.selectableOptionCommands.forEachIndexed { idx2, option ->
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
