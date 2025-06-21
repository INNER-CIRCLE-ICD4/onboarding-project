package fastcampus.inguk_onboarding.form.post.domain.Surveys;

public enum InputType {
    SHORT_TYPE("단답형"),
    LONG_TYPE("장문형"),
    SINGLE_TYPE("단일 선택 리스트"),
    MULTIPLE_TYPE("다중 선택 리스트");

    private final String description;

    InputType(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
}
