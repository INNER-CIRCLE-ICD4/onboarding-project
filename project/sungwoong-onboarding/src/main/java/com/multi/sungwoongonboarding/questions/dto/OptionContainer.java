package com.multi.sungwoongonboarding.questions.dto;

import com.multi.sungwoongonboarding.questions.domain.Questions;

import java.util.List;

public interface OptionContainer {
    String getType();
    List<?> getOptions();
}
