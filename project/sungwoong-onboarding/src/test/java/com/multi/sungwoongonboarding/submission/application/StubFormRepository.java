package com.multi.sungwoongonboarding.submission.application;


import com.multi.sungwoongonboarding.forms.application.repository.FormRepository;
import com.multi.sungwoongonboarding.forms.domain.Forms;
import com.multi.sungwoongonboarding.options.domain.Options;
import com.multi.sungwoongonboarding.questions.domain.Questions;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public class StubFormRepository implements FormRepository {

    @Override
    public Forms save(Forms forms) {
        return null;
    }

    @Override
    public Forms findById(Long id) {

        return Forms.builder()
                .id(1L)
                .title("햄버거 선호도 조사")
                .version(1)
                .description("햄버거를 좋아하시나요?")
                .createdAt(LocalDateTime.of(2025, Month.JULY, 18, 20, 38))
                .questions(List.of(
                        Questions.builder()
                                .id(1L)
                                .questionText("왜 좋아하시나요?")
                                .isRequired(true)
                                .questionType(Questions.QuestionType.SHORT_ANSWER)
                                .deleted(false)
                                .build(),
                        Questions.builder()
                                .id(2L)
                                .questionText("어떤 브랜드를 선호하시나요?")
                                .isRequired(true)
                                .questionType(Questions.QuestionType.SINGLE_CHOICE)
                                .deleted(false)
                                .options(
                                        List.of(
                                                Options.builder()
                                                        .id(1L)
                                                        .optionText("맥도날드")
                                                        .build(),
                                                Options.builder()
                                                        .id(2L)
                                                        .optionText("버거킹")
                                                        .build()

                                        ))
                                .build()
                ))
                .formsHistories(List.of())
                .userId("노성웅")
                .build();
    }

    @Override
    public List<Forms> findAll() {
        return List.of();
    }

    @Override
    public Forms update(Long formId, Forms forms) {
        return null;
    }
}
