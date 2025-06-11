package com.innercircle.survey.domain.survey;

import com.innercircle.survey.common.constants.SurveyConstants;
import com.innercircle.survey.common.domain.BaseEntity;
import com.innercircle.survey.common.util.UlidGenerator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 설문조사 엔티티
 * 
 * 설문조사의 기본 정보와 설문 항목들을 관리합니다.
 * 다중 인스턴스 환경을 고려하여 ULID를 사용합니다.
 */
@Entity
@Table(name = "surveys", indexes = {
    @Index(name = "idx_survey_created_at", columnList = "created_at")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Survey extends BaseEntity {

    @Id
    @Column(name = "id", length = 26)
    private String id;

    @NotBlank(message = "설문조사 제목은 필수입니다.")
    @Size(min = SurveyConstants.Survey.MIN_TITLE_LENGTH, 
          max = SurveyConstants.Survey.MAX_TITLE_LENGTH,
          message = "설문조사 제목은 {min}자 이상 {max}자 이하여야 합니다.")
    @Column(name = "title", nullable = false)
    private String title;

    @Size(max = SurveyConstants.Survey.MAX_DESCRIPTION_LENGTH,
          message = "설문조사 설명은 {max}자 이하여야 합니다.")
    @Column(name = "description", length = 1000)
    private String description;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("displayOrder ASC")
    private List<SurveyQuestion> questions = new ArrayList<>();

    /**
     * 설문조사 생성
     *
     * @param title 설문조사 제목
     * @param description 설문조사 설명
     */
    public Survey(String title, String description) {
        this.id = UlidGenerator.generate();
        this.title = title;
        this.description = description;
        initializeTimestamps();
    }

    /**
     * 설문조사 정보 수정
     *
     * @param title 새로운 제목
     * @param description 새로운 설명
     */
    public void updateInfo(String title, String description) {
        this.title = title;
        this.description = description;
    }

    /**
     * 설문 항목 추가
     *
     * @param question 추가할 설문 항목
     */
    public void addQuestion(SurveyQuestion question) {
        questions.add(question);
        question.assignToSurvey(this, questions.size());
    }

    /**
     * 모든 설문 항목을 새로운 항목들로 교체
     * 기존 응답 보존을 위해 기존 항목들은 비활성화 처리
     *
     * @param newQuestions 새로운 설문 항목들
     */
    public void updateQuestions(List<SurveyQuestion> newQuestions) {
        validateQuestionsCount(newQuestions.size());
        
        // 기존 질문들을 비활성화 (기존 응답 보존)
        questions.forEach(SurveyQuestion::deactivate);
        
        // 새로운 질문들 추가
        for (int i = 0; i < newQuestions.size(); i++) {
            SurveyQuestion question = newQuestions.get(i);
            questions.add(question);
            question.assignToSurvey(this, i + 1);
        }
    }

    /**
     * 활성화된 설문 항목들만 조회
     *
     * @return 활성화된 설문 항목 리스트
     */
    public List<SurveyQuestion> getActiveQuestions() {
        return questions.stream()
                .filter(SurveyQuestion::isActive)
                .toList();
    }

    /**
     * 설문 항목 개수 검증
     *
     * @param count 설문 항목 개수
     */
    private void validateQuestionsCount(int count) {
        if (count < SurveyConstants.Survey.MIN_QUESTIONS_COUNT || 
            count > SurveyConstants.Survey.MAX_QUESTIONS_COUNT) {
            throw new IllegalArgumentException(SurveyConstants.ErrorMessages.INVALID_QUESTION_COUNT);
        }
    }

    /**
     * 설문조사가 응답 가능한 상태인지 확인
     *
     * @return 응답 가능 여부
     */
    public boolean isAnswerable() {
        return !getActiveQuestions().isEmpty();
    }

    /**
     * 현재 설문조사의 스냅샷 생성 (응답 제출 시점용)
     * 
     * @return 설문조사 정보의 JSON 스냅샷
     */
    public String createSnapshot() {
        // 실제 구현에서는 Jackson ObjectMapper 사용 권장
        return String.format("""
            {
                "surveyId": "%s",
                "title": "%s",
                "description": "%s",
                "version": %d,
                "capturedAt": "%s",
                "questionCount": %d
            }
            """, 
            id, 
            title.replace("\"", "\\\""), 
            description != null ? description.replace("\"", "\\\"") : "",
            getVersion(),
            java.time.LocalDateTime.now(),
            getActiveQuestions().size()
        );
    }

    /**
     * 설문조사 ID를 통한 동등성 비교
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Survey survey = (Survey) obj;
        return id != null && id.equals(survey.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
