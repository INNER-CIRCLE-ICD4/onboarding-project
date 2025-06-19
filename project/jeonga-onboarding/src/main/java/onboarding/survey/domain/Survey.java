package onboarding.survey.domain;

import jakarta.persistence.*;
import lombok.Getter;
import onboarding.survey.exception.BadRequestException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Survey {

    @Id
    @GeneratedValue
    @Getter
    private Long id;

    @Getter
    private String title;

    @Getter
    private String description;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SurveyItem> items = new ArrayList<>();

    protected Survey() {
    }

    public Survey(String title, String description, List<SurveyItem> items) {
        validateTitle(title);
        validateItems(items);
        this.title = title;
        this.description = description;
        items.forEach(this::addItem);
    }

    // 편의메서드: 연관관계 세팅
    public void addItem(SurveyItem item) {
        item.setSurvey(this);
        this.items.add(item);
    }


    private void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new BadRequestException("제목은 필수입니다.");
        }
    }

    private void validateItems(List<SurveyItem> items) {
        if (items == null || items.isEmpty()) {
            throw new BadRequestException("항목은 최소 1개 이상이어야 합니다.");
        }
        if (items.size() > 10) {
            throw new BadRequestException("항목은 최대 10개까지만 허용됩니다.");
        }
    }

    public List<SurveyItem> getItems() {
        return Collections.unmodifiableList(items);
    }
}