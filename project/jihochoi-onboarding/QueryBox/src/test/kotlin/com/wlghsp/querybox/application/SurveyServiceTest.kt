package com.wlghsp.querybox.application

import com.wlghsp.querybox.domain.survey.QuestionType
import com.wlghsp.querybox.ui.dto.QuestionRequest
import com.wlghsp.querybox.ui.dto.QuestionUpdateRequest
import com.wlghsp.querybox.ui.dto.SurveyCreateRequest
import com.wlghsp.querybox.ui.dto.SurveyUpdateRequest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import

@DataJpaTest
@Import(SurveyService::class)
class SurveyServiceTest @Autowired constructor(
    val surveyService: SurveyService,
) {

    @DisplayName("설문 생성 성공")
    @Test
    fun createSurvey_success() {
        val request = SurveyCreateRequest(
            title = "테스트 설문",
            description = "설명입니다",
            questions = listOf(
                QuestionRequest(
                    name = "언어?",
                    description = "좋아하는 언어는?",
                    type = QuestionType.SHORT_TEXT,
                    required = true,
                    options = null
                )
            )
        )

        val id = surveyService.create(request)

        val survey = surveyService.findSurveyWithQuestionsById(id)
        assertThat(survey.title).isEqualTo("테스트 설문")
        assertThat(survey.getQuestions()).hasSize(1)
    }

    @DisplayName("설문 수정 성공")
    @Test
    fun updateSurvey_success() {
        // given
        val id = surveyService.create(
            SurveyCreateRequest(
                title = "원래 제목",
                description = "원래 설명",
                questions = listOf(
                    QuestionRequest(
                        name = "언어?",
                        description = "언어 질문",
                        type = QuestionType.SHORT_TEXT,
                        required = true,
                        options = null
                    )
                )
            )
        )

        val survey = surveyService.findSurveyWithQuestionsById(id)
        val questionId = survey.getQuestions().first().id

        // when
        val updateRequest = SurveyUpdateRequest(
            title = "변경된 제목",
            description = "변경된 설명",
            questions = listOf(
                QuestionUpdateRequest(
                    id = questionId,
                    name = "바뀐 질문",
                    description = "바뀐 설명",
                    type = QuestionType.SHORT_TEXT,
                    required = false,
                    options = null
                )
            )
        )
        surveyService.update(id, updateRequest)


        val updated = surveyService.findSurveyWithQuestionsById(id)
        assertThat(updated.title).isEqualTo("변경된 제목")
        assertThat(updated.getQuestions().first().name).isEqualTo("바뀐 질문")
    }

    @DisplayName("설문 id로 설문 조회 가능")
    @Test
    fun findSurveyWithQuestionsById_success() {
        val id = surveyService.create(
            SurveyCreateRequest(
                title = "조회 테스트",
                description = "조회 설명",
                questions = listOf(
                    QuestionRequest(
                        name = "이름은?",
                        description = "감자",
                        type = QuestionType.SHORT_TEXT,
                        required = false,
                        options = null
                    )
                ),
            )
        )

        val found = surveyService.findSurveyWithQuestionsById(id)

        assertThat(found.title).isEqualTo("조회 테스트")
        assertThat(found.description).isEqualTo("조회 설명")
    }
}