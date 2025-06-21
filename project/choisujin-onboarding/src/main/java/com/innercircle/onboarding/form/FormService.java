package com.innercircle.onboarding.form;

import com.innercircle.onboarding.common.exceptions.CommonException;
import com.innercircle.onboarding.common.jpa.QClassService;
import com.innercircle.onboarding.common.response.ResponseStatus;
import com.innercircle.onboarding.form.domain.Form;
import com.innercircle.onboarding.form.domain.FormDto;
import com.innercircle.onboarding.form.domain.FormRepository;
import com.innercircle.onboarding.question.QuestionService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FormService extends QClassService<Form, Long> {

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

    // TODO. 설문지 리스트 형식 변경하기
    public List<Form> getFormList() {
        List<Form> formList
                = Optional.ofNullable(formRepository.findAll())
                .orElseThrow(() -> new CommonException(ResponseStatus.NOT_FOUND_DATA));

        log.info(formList.toString());
        return formList;
    }

    public List<Long> findQuestionSeq(String keyword, UUID formUuid) {
        return createQuery()
                .select(qAnswer.questionSeq).distinct()
                .from(qAnswer)
                .leftJoin(qQuestion).on(qQuestion.seq.eq(qAnswer.questionSeq))
                .leftJoin(qForm).on(qForm.seq.eq(qQuestion.formSeq))
                .where(
                        qForm.uuid.eq(formUuid)
                                .and(
                                        qQuestion.content.contains(keyword)
                                                .or(qAnswer.content.contains(keyword))
                                )
                )
                .fetch();
    }

    public List<FormDto.SearchQuestionsDto> getForm(UUID uuid, String keyword) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qForm.uuid.eq(uuid));
        if(!ObjectUtils.isEmpty(keyword)) {
            List<Long> findQuestionList = findQuestionSeq(keyword, uuid);
            booleanBuilder.and(qQuestion.seq.in(findQuestionList));
        }

        List<FormDto.SearchQuestionsDto> formQuestionList
                = createQuery()
                .select(Projections.fields(
                        FormDto.SearchQuestionsDto.class
                        , qForm.uuid
                        , qForm.title
                        , qForm.description.as("formDescription")
                        , qQuestion.seq.as("questionSeq")
                        , qQuestion.content.as("questionContent")
                        , qQuestion.description.as("questionDescription")
                        , qQuestion.answerType
                        , qQuestion.answerOption.stringValue()
                        , qQuestion.isDeleted
                        , qQuestion.isRequired))
                .from(qForm)
                .leftJoin(qQuestion).on(qForm.seq.eq(qQuestion.formSeq))
                .where(booleanBuilder)
                .orderBy(qQuestion.orderIdx.asc(), qQuestion.version.asc())
                .fetch();

        if (formQuestionList == null || formQuestionList.isEmpty()) {
            throw new CommonException(ResponseStatus.NOT_FOUND_DATA);
        }

        log.info("formQuestionList : {}", formQuestionList);

        List<Long> questionSeqList = formQuestionList.stream()
                .map(FormDto.SearchQuestionsDto::getQuestionSeq)
                .toList();

        log.info("questionSeqList : {}", questionSeqList);

        List<FormDto.SearchAnswerDto> answerList
                = createQuery()
                .select(Projections.fields(
                        FormDto.SearchAnswerDto.class
                        , qQuestion.seq.as("questionSeq")
                        , qQuestion.content.as("questionContent")
                        , qAnswer.content.as("answerContent")))
                .from(qAnswer)
                .leftJoin(qQuestion).on(qAnswer.questionSeq.eq(qQuestion.seq))
                .where(qQuestion.seq.in(questionSeqList))
                .orderBy(qQuestion.seq.asc())
                .fetch();

        log.info("answerList : {}", answerList);

        Map<Long, List<String>> questionAnswerMap = answerList.stream()
                .collect(Collectors.groupingBy(
                        FormDto.SearchAnswerDto::getQuestionSeq,
                        Collectors.mapping(FormDto.SearchAnswerDto::getAnswerContent, Collectors.toList())
                ));

        log.info("questionAnswerMap : {}", questionAnswerMap.size());

        // formQuestionList 에 있는 questionSeq를 기준으로 answerList를 매핑
        formQuestionList.forEach(question -> {
            List<String> answers = questionAnswerMap.get(question.getQuestionSeq());
            question.setAnswerList(answers);
        });

        return formQuestionList;
    }

}
