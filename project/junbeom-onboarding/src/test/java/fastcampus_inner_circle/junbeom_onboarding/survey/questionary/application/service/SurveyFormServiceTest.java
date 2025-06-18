package fastcampus_inner_circle.junbeom_onboarding.survey.questionary.application.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.out.entity.SurveyContentType.RADIO;
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

    @Test
    @DisplayName("설문조사 생성 테스트 - 예시 데이터 포함")
    @Rollback(false)
    void testCreateSurveyFormWithContents() {
        // given
        List<InsertContentRequest> contents = new ArrayList<>();
        contents.add(InsertContentRequest.builder()
                .name("이름")
                .describe("귀하의 성함을 입력해 주세요.")
                .type("short_answer")
                .isRequired(true)
                .options(Arrays.asList("조준범"))
                .build());
        contents.add(InsertContentRequest.builder()
                .name("만족도")
                .describe("서비스 만족도를 선택해 주세요.")
                .type("radio")
                .isRequired(true)
                .options(Arrays.asList("매우 만족", "만족", "보통", "불만족", "매우 불만족"))
                .build());
        contents.add(InsertContentRequest.builder()
                .name("개선 의견")
                .describe("서비스 개선을 위한 의견을 작성해 주세요.")
                .type("long_answer")
                .isRequired(false)
                .options(Arrays.asList("너무 좋은 서비스입니다."))
                .build());
        contents.add(InsertContentRequest.builder()
                .name("선호 기능")
                .describe("사용한 기능 중 선호하는 항목을 선택해 주세요.")
                .type("checkbox")
                .isRequired(false)
                .options(Arrays.asList("빠른 응답", "친절한 지원", "사용 편의성", "다양한 옵션"))
                .build());

        InsertFormRequest request = InsertFormRequest.builder()
                .name("고객 경험 조사")
                .describe("고객 경험을 조사하기 위한 설문지입니다.")
                .contents(contents)
                .build();

        // when
        SurveyForm createdForm = surveyFormService.createSurveyForm(request);

        // then
        assertThat(createdForm).isNotNull();
        assertThat(createdForm.getName()).isEqualTo("고객 경험 조사");
        assertThat(createdForm.getDescribe()).isEqualTo("고객 경험을 조사하기 위한 설문지입니다.");
        assertThat(createdForm.getContents()).hasSize(4);

        // 각 문항별 검증
        assertThat(createdForm.getContents().get(0).getName()).isEqualTo("이름");
        assertThat(createdForm.getContents().get(1).getType()).isEqualTo(RADIO);
        //assertThat(createdForm.getContents().get(1).getOptions()).contains("매우 만족", "만족", "보통", "불만족", "매우 불만족");

        // DB에서 조회하여 검증
        SurveyForm foundForm = surveyFormRepository.findById(createdForm.getId()).orElse(null);
        assertThat(foundForm).isNotNull();
        assertThat(foundForm.getName()).isEqualTo("고객 경험 조사");
        assertThat(foundForm.getDescribe()).isEqualTo("고객 경험을 조사하기 위한 설문지입니다.");
        assertThat(foundForm.getContents()).hasSize(4);
    }
} 