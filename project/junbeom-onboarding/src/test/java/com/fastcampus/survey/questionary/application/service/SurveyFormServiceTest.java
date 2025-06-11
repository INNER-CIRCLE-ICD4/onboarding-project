package com.fastcampus.survey.questionary.application.service;

import com.fastcampus.survey.questionary.adapter.in.dto.InsertContentRequest;
import com.fastcampus.survey.questionary.adapter.in.dto.InsertFormRequest;
import com.fastcampus.survey.questionary.domain.model.SurveyForm;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Transactional
class SurveyFormServiceTest {
    @Mock
    private SurveyFormService surveyFormService;




    @Test
    void 설문_생성_성공_H2DB_INSERT() {
        // given
        InsertContentRequest item1 = new InsertContentRequest(
                "이름", "이름을 입력하세요", "short_answer", true, null
        );
        InsertContentRequest item2 = new InsertContentRequest(
                "성별", "성별을 선택하세요", "radio", true, List.of("남자", "여자")
        );
        InsertFormRequest request = InsertFormRequest.builder()
                .name("테스트 설문")
                .describe("테스트 설명")
                .contents(List.of(item1, item2))
                .build();

        // when
        SurveyForm saved = surveyFormService.createSurveyForm(request);

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("테스트 설문");
        assertThat(saved.getContents()).hasSize(2);
        assertThat(saved.getContents().get(1).getOptions()).containsExactly(
                com.fastcampus.survey.questionary.domain.model.SurveyContentOption.builder().text("남자").build(),
                com.fastcampus.survey.questionary.domain.model.SurveyContentOption.builder().text("여자").build()
        );
    }
} 