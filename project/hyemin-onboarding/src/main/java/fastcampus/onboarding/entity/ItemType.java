package fastcampus.onboarding.entity;

import lombok.Getter;

@Getter
public enum ItemType {
    SHORT_ANSWER("단답형"),
    PARAGRAPH("장문형"),
    SINGLE_CHOICE("단일 선택 리스트"),
    MULTIPLE_CHOICE("다중 선택 리스트");

    private final String description;

    ItemType(String description) {
        this.description = description;
    }
}
