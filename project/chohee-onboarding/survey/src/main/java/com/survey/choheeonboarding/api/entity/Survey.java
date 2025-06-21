package com.survey.choheeonboarding.api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Survey {

    @Id
    private String id;
    private String title;
    private String description;
    private String createNm;
    private String createdDt;
    private String modifyNm;
    private String modifyDt;


}

