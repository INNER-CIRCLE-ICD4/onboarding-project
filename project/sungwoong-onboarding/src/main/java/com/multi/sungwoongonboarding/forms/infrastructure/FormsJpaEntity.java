package com.multi.sungwoongonboarding.forms.infrastructure;

import com.multi.sungwoongonboarding.common.entity.BaseEntity;
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


}
