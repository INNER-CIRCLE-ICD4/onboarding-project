package com.multi.sungwoongonboarding.forms.infrastructure;

import com.multi.sungwoongonboarding.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "form_history")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FormHistoryJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "form_history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id")
    private FormsJpaEntity form;

    @Column(name = "form_version")
    private int version;


    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "question_count")
    private int questionCount;

    public static FormHistoryJpaEntity createHistory(FormsJpaEntity existingForm) {

        return FormHistoryJpaEntity.builder()
                .form(existingForm)
                .title(existingForm.getTitle())
                .description(existingForm.getDescription())
                .version(existingForm.getVersion())
                .questionCount(existingForm.getQuestions().size())
                .build();

    }


}
