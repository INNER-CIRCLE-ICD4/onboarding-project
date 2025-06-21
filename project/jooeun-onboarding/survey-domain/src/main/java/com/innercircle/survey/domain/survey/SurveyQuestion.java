package com.innercircle.survey.domain.survey;

import com.innercircle.survey.common.constants.SurveyConstants;
import com.innercircle.survey.common.domain.BaseEntity;
import com.innercircle.survey.common.domain.QuestionType;
import com.innercircle.survey.common.util.UlidGenerator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 설문 항목 엔티티
 * 
 * 개별 설문 항목의 정보를 관리합니다.
 * 기존 응답 보존을 위해 활성/비활성 상태를 관리합니다.
 */
@Entity
@Table(name = "survey_questions", indexes = {
    @Index(name = "idx_question_survey_id", columnList = "survey_id"),
    @Index(name = "idx_question_active_order", columnList = "survey_id, active, display_order")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyQuestion extends BaseEntity {

    @Id
    @Column(name = "id", length = 26)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @NotBlank(message = "설문 항목 제목은 필수입니다.")
    @Size(min = SurveyConstants.Question.MIN_TITLE_LENGTH,
          max = SurveyConstants.Question.MAX_TITLE_LENGTH,
          message = "설문 항목 제목은 {min}자 이상 {max}자 이하여야 합니다.")
    @Column(name = "title", nullable = false)
    private String title;

    @Size(max = SurveyConstants.Question.MAX_DESCRIPTION_LENGTH,
          message = "설문 항목 설명은 {max}자 이하여야 합니다.")
    @Column(name = "description", length = 500)
    private String description;

    @NotNull(message = "설문 항목 입력 형태는 필수입니다.")
    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false)
    private QuestionType questionType;

    @Column(name = "required", nullable = false)
    private boolean required = false;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    /**
     * 선택형 질문의 옵션들 (JSON 형태로 저장)
     * 단일/다중 선택 리스트에서만 사용
     */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "question_options", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "option_text")
    @OrderColumn(name = "option_order")
    private List<String> options = new ArrayList<>();

    /**
     * 설문 항목 생성 (텍스트 입력형)
     *
     * @param title 항목 제목
     * @param description 항목 설명
     * @param questionType 입력 형태
     * @param required 필수 여부
     */
    public SurveyQuestion(String title, String description, QuestionType questionType, boolean required) {
        this.id = UlidGenerator.generate();
        this.title = title;
        this.description = description;
        this.questionType = questionType;
        this.required = required;
        initializeTimestamps();
    }

    /**
     * 설문 항목 생성 (선택형)
     *
     * @param title 항목 제목
     * @param description 항목 설명
     * @param questionType 입력 형태
     * @param required 필수 여부
     * @param options 선택 옵션들
     */
    public SurveyQuestion(String title, String description, QuestionType questionType, 
                         boolean required, List<String> options) {
        this.id = UlidGenerator.generate();
        this.title = title;
        this.description = description;
        this.questionType = questionType;
        this.required = required;
        this.options = new ArrayList<>(options != null ? options : List.of());
        initializeTimestamps();
        
        // 선택형 질문 검증
        if (questionType.isChoiceType()) {
            validateOptions();
        }
    }

    /**
     * 설문조사에 항목 할당
     *
     * @param survey 설문조사
     * @param order 표시 순서
     */
    public void assignToSurvey(Survey survey, int order) {
        this.survey = survey;
        this.displayOrder = order;
    }

    /**
     * 설문 항목 정보 수정
     *
     * @param title 새로운 제목
     * @param description 새로운 설명
     * @param required 새로운 필수 여부
     */
    public void updateInfo(String title, String description, boolean required) {
        this.title = title;
        this.description = description;
        this.required = required;
    }

    /**
     * 선택 옵션 수정 (선택형 질문만)
     *
     * @param options 새로운 선택 옵션들
     */
    public void updateOptions(List<String> options) {
        if (!questionType.isChoiceType()) {
            throw new IllegalArgumentException("텍스트 입력형 질문에는 선택 옵션을 설정할 수 없습니다.");
        }
        
        this.options.clear();
        this.options.addAll(options);
        validateOptions();
    }

    /**
     * 항목 비활성화 (기존 응답 보존용)
     */
    public void deactivate() {
        this.active = false;
    }

    /**
     * 항목 활성화
     */
    public void activate() {
        this.active = true;
    }

    /**
     * 응답 값 검증
     *
     * @param answerValues 응답 값들
     * @return 검증 성공 여부
     */
    public boolean isValidAnswer(List<String> answerValues) {
        // 필수 항목 검증
        if (required && (answerValues == null || answerValues.isEmpty())) {
            return false;
        }

        // 비필수 항목이고 응답이 없는 경우 통과
        if (answerValues == null || answerValues.isEmpty()) {
            return true;
        }

        return switch (questionType) {
            case SHORT_TEXT, LONG_TEXT -> validateTextAnswer(answerValues);
            case SINGLE_CHOICE -> validateSingleChoiceAnswer(answerValues);
            case MULTIPLE_CHOICE -> validateMultipleChoiceAnswer(answerValues);
        };
    }

    /**
     * 텍스트 답변 검증
     */
    private boolean validateTextAnswer(List<String> answerValues) {
        if (answerValues.size() != 1) {
            return false;
        }
        
        String answer = answerValues.get(0);
        int maxLength = questionType == QuestionType.SHORT_TEXT 
                       ? SurveyConstants.Response.MAX_TEXT_ANSWER_LENGTH
                       : SurveyConstants.Response.MAX_LONG_TEXT_ANSWER_LENGTH;
        
        return answer.length() <= maxLength;
    }

    /**
     * 단일 선택 답변 검증
     */
    private boolean validateSingleChoiceAnswer(List<String> answerValues) {
        if (answerValues.size() != 1) {
            return false;
        }
        
        return options.contains(answerValues.get(0));
    }

    /**
     * 다중 선택 답변 검증
     */
    private boolean validateMultipleChoiceAnswer(List<String> answerValues) {
        return answerValues.stream().allMatch(options::contains);
    }

    /**
     * 선택 옵션 검증
     */
    private void validateOptions() {
        if (options == null || options.isEmpty()) {
            throw new IllegalArgumentException("선택형 질문에는 최소 1개의 옵션이 필요합니다.");
        }
        
        if (options.size() > SurveyConstants.Question.MAX_OPTIONS_COUNT) {
            throw new IllegalArgumentException("선택 옵션은 최대 " + SurveyConstants.Question.MAX_OPTIONS_COUNT + "개까지 가능합니다.");
        }
        
        for (String option : options) {
            if (option == null || option.trim().isEmpty()) {
                throw new IllegalArgumentException("선택 옵션은 빈 값일 수 없습니다.");
            }
            if (option.length() > SurveyConstants.Question.MAX_OPTION_LENGTH) {
                throw new IllegalArgumentException("선택 옵션은 " + SurveyConstants.Question.MAX_OPTION_LENGTH + "자 이하여야 합니다.");
            }
        }
    }

    /**
     * 질문 정보의 JSON 스냅샷 생성 (응답 제출 시점용)
     * 
     * @return 질문 정보의 JSON 스냅샷
     */
    public String createSnapshot() {
        // 실제 구현에서는 Jackson ObjectMapper 사용 권장
        String optionsJson = options.isEmpty() ? "[]" : 
            "[\"" + String.join("\", \"", options.stream()
                .map(opt -> opt.replace("\"", "\\\""))
                .toList()) + "\"]";
                
        return String.format("""
            {
                "questionId": "%s",
                "title": "%s",
                "description": "%s",
                "questionType": "%s",
                "required": %b,
                "displayOrder": %d,
                "options": %s,
                "capturedAt": "%s"
            }
            """, 
            id, 
            title.replace("\"", "\\\""), 
            description != null ? description.replace("\"", "\\\"") : "",
            questionType.name(),
            required,
            displayOrder,
            optionsJson,
            java.time.LocalDateTime.now()
        );
    }

    /**
     * 설문 항목 ID를 통한 동등성 비교
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SurveyQuestion question = (SurveyQuestion) obj;
        return id != null && id.equals(question.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
