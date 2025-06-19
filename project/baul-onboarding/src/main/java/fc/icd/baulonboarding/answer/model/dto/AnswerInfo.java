package fc.icd.baulonboarding.answer.model.dto;

import fc.icd.baulonboarding.common.model.code.InputType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class AnswerInfo {

    @Getter
    @Builder
    public static class Answer{
        private Long id;
        private Long surveyId;
        private String name;
        private String description;
        private List<AnswerItem> answerItemList;
    }

    @Getter
    @Builder
    public static class AnswerItem{
        private Long id;
        private Long answerId;
        private Long surveyItemId;
        private String question;
        private InputType inputType;
        private String content;
        private List<AnswerItemOption> answerItemOptionList;
    }

    @Getter
    @Builder
    public static class AnswerItemOption{
        private Long id;
        private Long answerItemId;
        private Long optionId;
        private String content;
    }


}
