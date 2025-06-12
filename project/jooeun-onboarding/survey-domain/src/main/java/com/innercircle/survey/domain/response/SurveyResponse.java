package com.innercircle.survey.domain.response;

import com.innercircle.survey.common.domain.BaseEntity;
import com.innercircle.survey.common.util.UlidGenerator;
import com.innercircle.survey.domain.survey.Survey;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 설문조사 응답 엔티티
 * 
 * 특정 설문조사에 대한 전체 응답을 관리합니다.
 * 응답은 제출 후 수정되지 않는 불변 데이터입니다.
 */
@Entity
@Table(name = "survey_responses", indexes = {
    @Index(name = "idx_response_survey_id", columnList = "survey_id"),
    @Index(name = "idx_response_submitted_at", columnList = "created_at"),
    @Index(name = "idx_response_survey_submitted", columnList = "survey_id, created_at")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyResponse extends BaseEntity {

    @Id
    @Column(name = "id", length = 26)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @Column(name = "respondent_info", length = 100)
    private String respondentInfo;

    @OneToMany(mappedBy = "surveyResponse", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SurveyAnswer> answers = new ArrayList<>();

    /**
     * 설문조사 응답 생성
     *
     * @param survey 설문조사
     * @param respondentInfo 응답자 정보 (선택사항)
     */
    public SurveyResponse(Survey survey, String respondentInfo) {
        this.id = UlidGenerator.generate();
        this.survey = survey;
        this.respondentInfo = respondentInfo;
        initializeTimestamps();
    }

    /**
     * 응답 추가
     *
     * @param answer 개별 항목 응답
     */
    public void addAnswer(SurveyAnswer answer) {
        answers.add(answer);
        answer.assignToResponse(this);
    }

    /**
     * 특정 질문의 응답 조회
     *
     * @param questionId 질문 ID
     * @return 해당 질문의 응답 (없으면 null)
     */
    public SurveyAnswer getAnswerByQuestionId(String questionId) {
        return answers.stream()
                .filter(answer -> answer.getQuestionId().equals(questionId))
                .findFirst()
                .orElse(null);
    }

    /**
     * 응답이 완료되었는지 확인
     * 
     * @return 응답 완료 여부
     */
    public boolean isCompleted() {
        return !answers.isEmpty();
    }

    /**
     * 응답 통계 정보 조회
     * 
     * @return 응답한 질문 수
     */
    public int getAnsweredQuestionCount() {
        return answers.size();
    }

    /**
     * 설문조사 응답 ID를 통한 동등성 비교
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SurveyResponse response = (SurveyResponse) obj;
        return id != null && id.equals(response.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
