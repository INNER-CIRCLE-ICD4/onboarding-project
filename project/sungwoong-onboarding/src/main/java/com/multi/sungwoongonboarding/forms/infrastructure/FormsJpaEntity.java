package com.multi.sungwoongonboarding.forms.infrastructure;

import com.multi.sungwoongonboarding.common.entity.BaseEntity;
import com.multi.sungwoongonboarding.forms.domain.Forms;
import jakarta.persistence.*;

@Entity
@Table(name = "forms")
public class FormsJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "form_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    public static FormsJpaEntity from(Forms forms) {

        FormsJpaEntity formsJpaEntity = new FormsJpaEntity();
        formsJpaEntity.id = forms.getId();
        formsJpaEntity.title = forms.getTitle();
        formsJpaEntity.description = forms.getDescription();
        return formsJpaEntity;
    }

    public Forms toDomain() {
        return Forms.builder()
                .id(this.id)
                .title(this.title)
                .description(this.description)
                .build();
    }
}
