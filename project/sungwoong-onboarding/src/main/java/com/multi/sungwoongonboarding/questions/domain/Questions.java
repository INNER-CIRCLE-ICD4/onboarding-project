package com.multi.sungwoongonboarding.questions.domain;

import com.multi.sungwoongonboarding.options.domain.Options;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@RequiredArgsConstructor
public class Questions {
    public enum QuestionType {
        //단문, 장문, 단일 선택, 복수 선택
        SHORT_ANSWER, LONG_ANSWER, SINGLE_CHOICE, MULTIPLE_CHOICE;

        public boolean isChoiceType() {
            return this == SINGLE_CHOICE || this == MULTIPLE_CHOICE;
        }

        public boolean isTextType() {
            return this == SHORT_ANSWER || this == LONG_ANSWER;
        }
    }

    private final Long id;
    private final String questionText;
    private final QuestionType questionType;
    private final boolean isRequired;
    private final int version;
    private final boolean deleted;
    private final List<Options> options;
}
