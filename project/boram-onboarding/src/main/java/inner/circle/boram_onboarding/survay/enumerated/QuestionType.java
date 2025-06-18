package inner.circle.boram_onboarding.survay.enumerated;

public enum QuestionType {
    SHORT,          // 단답형
    LONG,           // 장문형
    SINGLE_CHOICE,  // 단일 선택 리스트
    MULTIPLE_CHOICE ; // 다중 선택 리스트

    public boolean isChoice() {
        return this == SINGLE_CHOICE || this == MULTIPLE_CHOICE;
    }

}
