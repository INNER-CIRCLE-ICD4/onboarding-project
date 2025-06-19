package fc.icd.jaehyeononboarding.answer.model.dto;

import fc.icd.jaehyeononboarding.answer.model.entity.Answer;
import fc.icd.jaehyeononboarding.common.constants.ResultCodes;
import fc.icd.jaehyeononboarding.survey.model.entity.Question;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Data
public class AnswerRetrieveDTO {

    private long questionGroupId;
    private int version;
    private LocalDateTime submittedAt;
    private List<AnswerWithQuestionDTO<?>> answers;

    public static AnswerRetrieveDTO from(Answer answer) {
        AnswerRetrieveDTO data = new AnswerRetrieveDTO();
        data.setQuestionGroupId(answer.getQuestionGroup().getId());
        data.setVersion(answer.getQuestionGroup().getVersion());
        data.setSubmittedAt(answer.getCreatedAt());
        List<AnswerWithQuestionDTO<?>> answers = new ArrayList<>();
        List<Question> questions = answer.getQuestionGroup().getQuestions();
        List<?> textList = answer.getText();
        IntStream.range(0, questions.size()).forEach(i -> {
            Question question = questions.get(i);
            Object text = textList.get(i);
            AnswerWithQuestionDTO<?> answerWithQuestion = null;
            if(text == null) {
                answerWithQuestion = new AnswerWithQuestionDTO();
            } else if(text instanceof String singleText) {
                SingleAnswerDTO single = new SingleAnswerDTO();
                single.setContent(singleText);
                answerWithQuestion = single;
            } else if(text instanceof List<?>) {
                MultipleAnswerDTO multiple = new MultipleAnswerDTO();
                multiple.setContent((List<String>)text);
                answerWithQuestion = multiple;
            } else {
                throw new RuntimeException(ResultCodes.RC_30000.getMessage());
            }

            answerWithQuestion.setLabel(question.getLabel());
            answerWithQuestion.setDescription(question.getDescription());
            answerWithQuestion.setInputType(question.getInputType());
            answerWithQuestion.setRequired(question.getRequired());
            answerWithQuestion.setOptions(question.getOptions());
            answers.add(answerWithQuestion);
        });
        data.setAnswers(answers);
        return data;
    }

}
