package onboarding.survey.domain;

public enum InputType {
    /** 단답형 입력 */
    SHORT_TEXT,

    /** 장문형 입력 */
    LONG_TEXT,

    /** 하나만 선택할 수 있는 리스트 */
    SINGLE_CHOICE,

    /** 여러 개 선택할 수 있는 리스트 */
    MULTIPLE_CHOICE;

    /**
     * 현재 입력 유형이 선택형(SINGLE_CHOICE, MULTIPLE_CHOICE)인지 여부를 반환.
     * @return 선택형이면 true, 아니면 false
     */
    public boolean isSelectable() {
        return this == SINGLE_CHOICE || this == MULTIPLE_CHOICE;
    }
}
