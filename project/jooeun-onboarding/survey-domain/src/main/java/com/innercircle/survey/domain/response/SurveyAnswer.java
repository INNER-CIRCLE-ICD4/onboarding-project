package com.innercircle.survey.domain.response;

import com.innercircle.survey.common.domain.BaseEntity;
import com.innercircle.survey.common.domain.QuestionType;
import com.innercircle.survey.common.util.UlidGenerator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 설문 항목별 응답 엔티티
 * 
 * 개별 설문 항목에 대한 응답을 관리합니다.
 * 응답은 제출 후 수정되지 않는 불변 데이터입니다.
 */
@Entity
@Table(name = "survey_answers", indexes = {
    @Index(name = "idx_answer_response_id", columnList = "response_id"),
    @Index(name = "idx_answer_question_id", columnList = "question_id"),
    @Index(name = "idx_answer_response_question", columnList = "response_id, question_id")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyAnswer extends BaseEntity {

    @Id
    @Column(name = "id", length = 26)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_id", nullable = false)
    private SurveyResponse surveyResponse;

    @NotBlank(message = "질문 ID는 필수입니다.")
    @Column(name = "question_id", nullable = false, length = 26)
    private String questionId;

    @NotBlank(message = "질문 제목은 필수입니다.")
    @Column(name = "question_title", nullable = false)
    private String questionTitle;

    @NotNull(message = "질문 타입은 필수입니다.")
    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false)
    private QuestionType questionType;

    /**
     * 응답 시점의 완전한 질문 정보 스냅샷 (JSON 형태)
     * 질문이 수정/삭제되어도 응답의 맥락을 영구 보존
     */
    @Column(name = "question_snapshot", columnDefinition = "TEXT")
    private String questionSnapshot;

    /**
     * 선택형 질문의 응답 시점 선택지들 (스냅샷)
     * 선택지가 변경되어도 원본 선택지 맥락 보존
     */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "answer_choice_snapshots", joinColumns = @JoinColumn(name = "answer_id"))
    @Column(name = "choice_text")
    @OrderColumn(name = "choice_order")
    private List<String> availableChoicesSnapshot = new ArrayList<>();

    /**
     * 응답 값들 (JSON 배열 형태로 저장)
     * - 단답형/장문형: 1개 요소
     * - 단일 선택: 1개 요소
     * - 다중 선택: 여러 요소 가능
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "answer_values", joinColumns = @JoinColumn(name = "answer_id"))
    @Column(name = "answer_value", length = 10000)
    @OrderColumn(name = "value_order")
    private List<String> answerValues = new ArrayList<>();

    /**
     * 설문 항목 응답 생성
     *
     * @param questionId 질문 ID
     * @param questionTitle 질문 제목 (스냅샷용)
     * @param questionType 질문 타입 (스냅샷용)
     * @param answerValues 응답 값들
     */
    public SurveyAnswer(String questionId, String questionTitle, QuestionType questionType, List<String> answerValues) {
        this.id = UlidGenerator.generate();
        this.questionId = questionId;
        this.questionTitle = questionTitle;
        this.questionType = questionType;
        this.answerValues = new ArrayList<>(answerValues != null ? answerValues : List.of());
        initializeTimestamps();
        
        validateAnswer();
    }

    /**
     * 설문 항목 응답 생성 (완전한 스냅샷 포함)
     *
     * @param questionId 질문 ID
     * @param questionTitle 질문 제목 (스냅샷용)
     * @param questionType 질문 타입 (스냅샷용)
     * @param answerValues 응답 값들
     * @param questionSnapshot 질문 전체 정보 JSON 스냅샷
     * @param availableChoices 응답 시점의 선택지들 (선택형 질문용)
     */
    public SurveyAnswer(String questionId, String questionTitle, QuestionType questionType, 
                       List<String> answerValues, String questionSnapshot, List<String> availableChoices) {
        this.id = UlidGenerator.generate();
        this.questionId = questionId;
        this.questionTitle = questionTitle;
        this.questionType = questionType;
        this.answerValues = new ArrayList<>(answerValues != null ? answerValues : List.of());
        this.questionSnapshot = questionSnapshot;
        this.availableChoicesSnapshot = new ArrayList<>(availableChoices != null ? availableChoices : List.of());
        initializeTimestamps();
        
        validateAnswer();
    }

    /**
     * 설문조사 응답에 할당
     *
     * @param surveyResponse 설문조사 응답
     */
    public void assignToResponse(SurveyResponse surveyResponse) {
        this.surveyResponse = surveyResponse;
    }

    /**
     * 단일 답변 값 조회 (단답형, 장문형, 단일선택용)
     *
     * @return 첫 번째 응답 값
     */
    public String getSingleAnswer() {
        return answerValues.isEmpty() ? null : answerValues.get(0);
    }

    /**
     * 모든 답변 값 조회 (다중선택용)
     *
     * @return 모든 응답 값들의 복사본
     */
    public List<String> getAllAnswers() {
        return new ArrayList<>(answerValues);
    }

    /**
     * 텍스트 형태 응답인지 확인
     *
     * @return 텍스트 응답 여부
     */
    public boolean isTextAnswer() {
        return questionType.isTextType();
    }

    /**
     * 선택형 응답인지 확인
     *
     * @return 선택형 응답 여부
     */
    public boolean isChoiceAnswer() {
        return questionType.isChoiceType();
    }

    /**
     * 단일 선택 응답인지 확인
     *
     * @return 단일 선택 응답 여부
     */
    public boolean isSingleChoice() {
        return questionType.isSingleChoice();
    }

    /**
     * 다중 선택 응답인지 확인
     *
     * @return 다중 선택 응답 여부
     */
    public boolean isMultipleChoice() {
        return questionType.isMultipleChoice();
    }

    /**
     * 응답이 비어있는지 확인
     *
     * @return 빈 응답 여부
     */
    public boolean isEmpty() {
        return answerValues.isEmpty() || 
               answerValues.stream().allMatch(value -> value == null || value.trim().isEmpty());
    }

    /**
     * 특정 값이 포함되어 있는지 확인 (검색용)
     *
     * @param searchValue 검색할 값
     * @return 포함 여부
     */
    public boolean containsValue(String searchValue) {
        if (searchValue == null || searchValue.trim().isEmpty()) {
            return false;
        }
        
        String lowerSearchValue = searchValue.toLowerCase();
        return answerValues.stream()
                .anyMatch(value -> value != null && value.toLowerCase().contains(lowerSearchValue));
    }

    /**
     * 응답 시점의 원본 선택지들 조회 (선택형 질문용)
     *
     * @return 응답 시점의 선택지 목록
     */
    public List<String> getOriginalChoices() {
        return new ArrayList<>(availableChoicesSnapshot);
    }

    /**
     * 응답 시점의 질문 정보 스냅샷 조회
     *
     * @return 질문 스냅샷 JSON
     */
    public String getQuestionSnapshot() {
        return questionSnapshot;
    }

    /**
     * 질문 제목에 특정 값이 포함되어 있는지 확인 (검색용)
     *
     * @param searchValue 검색할 값
     * @return 포함 여부
     */
    public boolean questionTitleContains(String searchValue) {
        if (searchValue == null || searchValue.trim().isEmpty()) {
            return false;
        }
        
        return questionTitle != null && 
               questionTitle.toLowerCase().contains(searchValue.toLowerCase());
    }

    /**
     * 응답 값 검증
     */
    private void validateAnswer() {
        if (answerValues == null) {
            this.answerValues = new ArrayList<>();
            return;
        }

        // 질문 타입별 응답 개수 검증
        switch (questionType) {
            case SHORT_TEXT, LONG_TEXT, SINGLE_CHOICE -> {
                if (answerValues.size() > 1) {
                    throw new IllegalArgumentException("단일 응답 질문에는 하나의 답변만 허용됩니다.");
                }
            }
            case MULTIPLE_CHOICE -> {
                // 다중 선택은 여러 응답 허용, 별도 검증 불필요
            }
        }

        // 빈 값 제거
        answerValues.removeIf(value -> value == null || value.trim().isEmpty());
    }

    /**
     * 응답의 문자열 표현 (로깅/디버깅용)
     *
     * @return 응답 요약
     */
    public String getAnswerSummary() {
        if (isEmpty()) {
            return "(응답 없음)";
        }
        
        return switch (questionType) {
            case SHORT_TEXT, LONG_TEXT -> getSingleAnswer();
            case SINGLE_CHOICE -> getSingleAnswer();
            case MULTIPLE_CHOICE -> String.join(", ", answerValues);
        };
    }

    /**
     * 설문 응답 ID를 통한 동등성 비교
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SurveyAnswer answer = (SurveyAnswer) obj;
        return id != null && id.equals(answer.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "SurveyAnswer{" +
               "id='" + id + '\'' +
               ", questionId='" + questionId + '\'' +
               ", questionTitle='" + questionTitle + '\'' +
               ", questionType=" + questionType +
               ", answerSummary='" + getAnswerSummary() + '\'' +
               '}';
    }
}
