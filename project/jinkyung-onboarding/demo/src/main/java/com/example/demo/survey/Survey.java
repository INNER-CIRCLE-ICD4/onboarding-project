package com.example.demo.survey;

import com.example.demo.item.Item;
import com.example.demo.survey.Dto.UpdateSurveyDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private List<Item> items;

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
