package fastcampus_inner_circle.junbeom_onboarding.survey.questionary.application.service;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.in.dto.InsertContentRequest;
import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.in.dto.InsertFormRequest;
import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.domain.model.SurveyContentOption;
import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.domain.model.SurveyForm;
import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.domain.repository.SurveyFormRepository;

@SpringBootTest
@Transactional
class SurveyFormServiceTest {
    @Autowired
    private SurveyFormService surveyFormService;

    @Autowired
    private SurveyFormRepository surveyFormRepository;

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

        List<SurveyContentOption> actualOptions = saved.getContents().get(1).getOptions();
        System.out.println("Actual Options:");
        for (int i = 0; i < actualOptions.size(); i++) {
            SurveyContentOption option = actualOptions.get(i);
            System.out.println("Option " + i + ": id=" + option.getId() + ", contentId=" + option.getContentId() + ", text=" + option.getText());
        }


        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("테스트 설문");
        assertThat(saved.getContents()).hasSize(2);
        assertThat(saved.getContents().get(1).getOptions()).containsExactly(
                SurveyContentOption.builder().text("남자").build(),
                SurveyContentOption.builder().text("여자").build()
        );
    }

    @Test
    @DisplayName("설문조사 생성 테스트")
    @Rollback(false)
    void testCreateSurveyForm() {
        // given
        InsertFormRequest request = InsertFormRequest.builder()
                .name("테스트 설문지")
                .describe("테스트용 설문지입니다.")
                .build();

        // when
        SurveyForm createdForm = surveyFormService.createSurveyForm(request);

        // then
        assertThat(createdForm).isNotNull();
        assertThat(createdForm.getName()).isEqualTo("테스트 설문지");
        assertThat(createdForm.getDescribe()).isEqualTo("테스트용 설문지입니다.");

        // DB에서 조회하여 검증
        SurveyForm foundForm = surveyFormRepository.findById(createdForm.getId()).orElse(null);
        assertThat(foundForm).isNotNull();
        assertThat(foundForm.getName()).isEqualTo("테스트 설문지");
        assertThat(foundForm.getDescribe()).isEqualTo("테스트용 설문지입니다.");
    }
} 