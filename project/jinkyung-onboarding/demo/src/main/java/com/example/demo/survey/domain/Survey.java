package com.example.demo.survey.domain;

import com.example.demo.answer.domain.Answer;
import com.example.demo.item.domain.Item;
import com.example.demo.survey.domain.dto.UpdateSurveyDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "survey")
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "survey_id")
    private Long surveyId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "survey")
    private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    public void updateSurvey(UpdateSurveyDTO updateSurveyDTO) {
        if(updateSurveyDTO.getName() != null) this.name = updateSurveyDTO.getName();
        if(updateSurveyDTO.getDescription() != null) this.description = updateSurveyDTO.getDescription();
//        updateSurveyDTO.getItems().forEach(itemDto -> {
//            Item item = items.stream()
//                    .filter(i -> i.getItemNo() == (itemDto.getItemNo()))
//                    .findFirst()
//                    .orElseThrow(() -> new IllegalArgumentException("Item not found"));
//            item.updateItem(itemDto);
//        });


        this.createAt = LocalDateTime.now();
    }

}
