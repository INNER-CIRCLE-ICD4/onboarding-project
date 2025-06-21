
package com.innercircle.onboarding.changzune_onboarding.survey.domain;

import jakarta.persistence.*;
        import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC) // ← 기본 생성자를 public 으로 변경해서 new Answer() 가능하게 함
@AllArgsConstructor
@Builder
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Survey survey;

    private Long questionId;
    private String questionName;
    private String answerValue;

    // Setter는 @Setter 애노테이션으로 모두 자동 생성되므로 따로 메서드 안 만들어도 됩니다.
}