package com.innercircle.survey.domain.integration;

import com.innercircle.survey.common.domain.QuestionType;
import com.innercircle.survey.domain.response.SurveyAnswer;
import com.innercircle.survey.domain.response.SurveyResponse;
import com.innercircle.survey.domain.survey.Survey;
import com.innercircle.survey.domain.survey.SurveyQuestion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 설문 수정 시 기존 응답 보존 기능 통합 테스트
 * 
 * 핵심 비즈니스 요구사항인 "설문 수정 후에도 기존 응답은 유지되어야 함"을 검증합니다.
 */
@DisplayName("설문 수정 시 기존 응답 보존 통합 테스트")
class SurveyModificationResponsePreservationTest {

    @Test
    @DisplayName("설문 정보 수정 후 기존 응답의 스냅샷이 보존되는지 확인")
    void 설문_정보_수정_후_기존응답_스냅샷_보존() {
        // Given: 원본 설문조사 생성
        Survey originalSurvey = new Survey("원본 설문 제목", "원본 설문 설명");
        SurveyQuestion originalQuestion = new SurveyQuestion(
            "원본 질문", "원본 질문 설명", QuestionType.SHORT_TEXT, true
        );
        originalSurvey.addQuestion(originalQuestion);

        // 응답 제출 (스냅샷 포함)
        SurveyResponse response = new SurveyResponse(originalSurvey, "응답자1");
        String questionSnapshot = originalQuestion.createSnapshot();
        SurveyAnswer answer = new SurveyAnswer(
            originalQuestion.getId(),
            originalQuestion.getTitle(),
            originalQuestion.getQuestionType(),
            List.of("원본 응답"),
            questionSnapshot,
            List.of()
        );
        response.addAnswer(answer);

        // When: 설문조사 정보 수정
        originalSurvey.updateInfo("수정된 설문 제목", "수정된 설문 설명");

        // Then: 응답의 스냅샷은 원본 정보를 유지
        assertThat(answer.getQuestionTitle()).isEqualTo("원본 질문");
        assertThat(answer.getQuestionSnapshot()).contains("원본 질문");
        assertThat(answer.getQuestionSnapshot()).contains("원본 질문 설명");
        
        // 현재 설문은 수정된 상태
        assertThat(originalSurvey.getTitle()).isEqualTo("수정된 설문 제목");
        
        // 응답은 여전히 의미를 가짐
        assertThat(answer.getSingleAnswer()).isEqualTo("원본 응답");
    }

    @Test
    @DisplayName("선택형 질문의 선택지 변경 후 기존 응답 맥락 보존")
    void 선택지_변경_후_기존응답_맥락_보존() {
        // Given: 선택형 질문이 있는 설문조사
        Survey survey = new Survey("만족도 조사", "서비스 만족도를 조사합니다");
        List<String> originalChoices = List.of("매우 불만족", "불만족", "보통", "만족", "매우 만족");
        SurveyQuestion question = new SurveyQuestion(
            "서비스 만족도", "전반적인 서비스에 대해 평가해주세요", 
            QuestionType.SINGLE_CHOICE, true, originalChoices
        );
        survey.addQuestion(question);

        // 기존 응답 제출 (선택지 스냅샷 포함)
        SurveyResponse response = new SurveyResponse(survey, "고객1");
        String questionSnapshot = question.createSnapshot();
        SurveyAnswer answer = new SurveyAnswer(
            question.getId(),
            question.getTitle(),
            question.getQuestionType(),
            List.of("매우 만족"),
            questionSnapshot,
            originalChoices
        );
        response.addAnswer(answer);

        // When: 선택지 변경 (기존 "매우 만족" 제거, 새 옵션 추가)
        List<String> newChoices = List.of("불만족", "보통", "만족", "매우 만족+");
        question.updateOptions(newChoices);

        // Then: 응답의 선택지 스냅샷은 원본 유지
        assertThat(answer.getOriginalChoices()).containsExactly("매우 불만족", "불만족", "보통", "만족", "매우 만족");
        assertThat(answer.getSingleAnswer()).isEqualTo("매우 만족");
        
        // 현재 질문은 새로운 선택지 사용
        assertThat(question.getOptions()).containsExactly("불만족", "보통", "만족", "매우 만족+");
        
        // 응답이 현재 선택지와 호환되지 않더라도 원본 맥락은 보존됨
        assertThat(answer.isStillValidAgainstCurrentChoices(newChoices)).isFalse();
        assertThat(answer.isStillValidAgainstCurrentChoices(originalChoices)).isTrue();
    }

    @Test
    @DisplayName("질문 전체 재구성 후 기존 응답들의 개별 보존")
    void 질문_전체_재구성_후_기존응답들_개별_보존() {
        // Given: 여러 질문이 있는 설문조사
        Survey survey = new Survey("종합 설문조사", "다양한 항목 조사");
        
        SurveyQuestion question1 = new SurveyQuestion("이름", "성명을 입력해주세요", QuestionType.SHORT_TEXT, true);
        SurveyQuestion question2 = new SurveyQuestion("선호 색상", "좋아하는 색상을 선택해주세요", 
            QuestionType.SINGLE_CHOICE, false, List.of("빨강", "파랑", "노랑"));
        
        survey.addQuestion(question1);
        survey.addQuestion(question2);

        // 기존 응답들 제출
        SurveyResponse response1 = new SurveyResponse(survey, "응답자1");
        response1.addAnswer(new SurveyAnswer(question1.getId(), question1.getTitle(), 
            question1.getQuestionType(), List.of("홍길동"), question1.createSnapshot(), List.of()));
        response1.addAnswer(new SurveyAnswer(question2.getId(), question2.getTitle(), 
            question2.getQuestionType(), List.of("빨강"), question2.createSnapshot(), question2.getOptions()));

        SurveyResponse response2 = new SurveyResponse(survey, "응답자2");
        response2.addAnswer(new SurveyAnswer(question1.getId(), question1.getTitle(), 
            question1.getQuestionType(), List.of("김철수"), question1.createSnapshot(), List.of()));

        // When: 설문 전체 재구성 (기존 질문들 비활성화, 새 질문들 추가)
        SurveyQuestion newQuestion1 = new SurveyQuestion("이메일", "이메일 주소를 입력해주세요", QuestionType.SHORT_TEXT, true);
        SurveyQuestion newQuestion2 = new SurveyQuestion("성별", "성별을 선택해주세요", 
            QuestionType.SINGLE_CHOICE, false, List.of("남성", "여성", "기타"));
        
        survey.updateQuestions(List.of(newQuestion1, newQuestion2));

        // Then: 기존 질문들은 비활성화되지만 응답은 보존
        assertThat(question1.isActive()).isFalse();
        assertThat(question2.isActive()).isFalse();
        
        // 새로운 활성 질문들
        assertThat(survey.getActiveQuestions()).hasSize(2);
        assertThat(survey.getActiveQuestions().get(0).getTitle()).isEqualTo("이메일");
        assertThat(survey.getActiveQuestions().get(1).getTitle()).isEqualTo("성별");

        // 기존 응답들은 모든 스냅샷 정보와 함께 보존
        SurveyAnswer preservedAnswer1 = response1.getAnswerByQuestionId(question1.getId());
        SurveyAnswer preservedAnswer2 = response1.getAnswerByQuestionId(question2.getId());
        SurveyAnswer preservedAnswer3 = response2.getAnswerByQuestionId(question1.getId());

        assertThat(preservedAnswer1.getQuestionTitle()).isEqualTo("이름");
        assertThat(preservedAnswer1.getSingleAnswer()).isEqualTo("홍길동");
        
        assertThat(preservedAnswer2.getQuestionTitle()).isEqualTo("선호 색상");
        assertThat(preservedAnswer2.getSingleAnswer()).isEqualTo("빨강");
        assertThat(preservedAnswer2.getOriginalChoices()).containsExactly("빨강", "파랑", "노랑");
        
        assertThat(preservedAnswer3.getQuestionTitle()).isEqualTo("이름");
        assertThat(preservedAnswer3.getSingleAnswer()).isEqualTo("김철수");
    }

    @Test
    @DisplayName("버전 관리를 통한 설문 변경 이력 추적")
    void 버전_관리를_통한_설문_변경_이력_추적() {
        // Given: 초기 설문조사
        Survey survey = new Survey("버전 테스트 설문", "버전 관리 테스트");
        Long initialVersion = survey.getVersion();
        
        SurveyQuestion question = new SurveyQuestion("초기 질문", "초기 설명", QuestionType.SHORT_TEXT, true);
        survey.addQuestion(question);

        // 응답 제출
        SurveyResponse response = new SurveyResponse(survey, "테스터");
        String initialSnapshot = survey.createSnapshot();
        
        // When: 여러 번의 수정 작업
        survey.updateInfo("수정된 제목 1차", "수정된 설명 1차");
        Long version1 = survey.getVersion();
        
        survey.updateInfo("수정된 제목 2차", "수정된 설명 2차");
        Long version2 = survey.getVersion();

        // Then: 버전이 순차적으로 증가
        assertThat(version1).isGreaterThan(initialVersion);
        assertThat(version2).isGreaterThan(version1);
        
        // 초기 스냅샷에는 원본 정보가 보존됨
        assertThat(initialSnapshot).contains("버전 테스트 설문");
        assertThat(initialSnapshot).contains("버전 관리 테스트");
        
        // 현재 설문은 최신 정보
        assertThat(survey.getTitle()).isEqualTo("수정된 제목 2차");
        assertThat(survey.getDescription()).isEqualTo("수정된 설명 2차");
    }
}
