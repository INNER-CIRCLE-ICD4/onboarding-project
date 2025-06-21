package com.onboarding.api.dto.response;

import com.onboarding.api.dto.request.OptionsReq;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = true)
public class SingleChoiceQuestionRes extends QuestionRes {
    private List<OptionsRes> options;

    public SingleChoiceQuestionRes(String questionId, String title, String description, String type, boolean required, List<OptionsRes> options) {
        super(questionId, title, description, type, required);
        this.options = options;
    }
}
