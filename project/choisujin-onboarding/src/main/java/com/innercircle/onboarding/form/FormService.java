package com.innercircle.onboarding.form;

import com.innercircle.onboarding.form.domain.FormDto;
import com.innercircle.onboarding.form.domain.FormRepository;
import com.innercircle.onboarding.question.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FormService {

    private final FormRepository formRepository;
    private final QuestionService questionService;

    public FormService(FormRepository formRepository, QuestionService questionService) {
        this.formRepository = formRepository;
        this.questionService = questionService;
    }

    public void create(FormDto.Create createForm) {

        Long seq = formRepository.save(createForm.ofEntity()).getSeq();
        log.debug("===> [폼 생성] seq : {}", seq);

        questionService.create(seq, createForm.questionList);

    }

}
