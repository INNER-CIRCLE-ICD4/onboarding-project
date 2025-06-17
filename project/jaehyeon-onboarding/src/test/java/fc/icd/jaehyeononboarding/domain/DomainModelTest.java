package fc.icd.jaehyeononboarding.domain;

import fc.icd.jaehyeononboarding.common.model.InputType;
import fc.icd.jaehyeononboarding.survey.model.entity.Question;
import fc.icd.jaehyeononboarding.survey.model.entity.QuestionGroup;
import fc.icd.jaehyeononboarding.survey.model.entity.Survey;
import fc.icd.jaehyeononboarding.survey.repository.QuestionGroupRepository;
import fc.icd.jaehyeononboarding.survey.repository.QuestionRepository;
import fc.icd.jaehyeononboarding.survey.repository.SurveyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DomainModelTest {

    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private QuestionGroupRepository questionGroupRepository;
    @Autowired
    private QuestionRepository questionRepository;
    
    @Test
    void 설문_저장_테스트() {
        Survey survey = new Survey();
        survey.setDescription("설명");
        survey.setLatestVersion(1);
        survey.setIsDeleted(false);

        Survey saved = surveyRepository.save(survey);

        Survey found = surveyRepository.findById(saved.getId()).orElse(null);
        System.out.println("saved : " + saved);
        System.out.println("found : " + found);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(saved.getId());
        assertThat(found.getDescription()).isEqualTo(saved.getDescription());
        assertThat(found.getLatestVersion()).isEqualTo(saved.getLatestVersion());
        assertThat(found.getIsDeleted()).isEqualTo(saved.getIsDeleted());
    }

    @Test
    void 설문항목그룹_저장_테스트() {
        QuestionGroup group = new QuestionGroup();
        group.setVersion(1);
        group.setCreatedAt(LocalDateTime.now());
        QuestionGroup saved = questionGroupRepository.save(group);
        QuestionGroup found = questionGroupRepository.findById(saved.getId()).orElse(null);
        System.out.println("saved : " + saved);
        System.out.println("found : " + found);
        assertThat(found).isNotNull();
        assertThat(found.getVersion()).isEqualTo(group.getVersion());
        assertThat(found.getCreatedAt()).isEqualTo(group.getCreatedAt());
    }

    @Test
    void 설문항목_저장_테스트() {
        Question question = new Question();
        question.setLabel("테스트 문항");
        question.setInputType(InputType.TEXT);
        question.setRequired(true);
        question.setCreatedAt(LocalDateTime.now());
        Question saved = questionRepository.save(question);
        Question found = questionRepository.findById(saved.getId()).orElse(null);
        System.out.println("saved : " + saved);
        System.out.println("found : " + found);
        assertThat(found).isNotNull();
        assertThat(found.getLabel()).isEqualTo(question.getLabel());
        assertThat(found.getInputType()).isEqualTo(question.getInputType());
        assertThat(found.getRequired()).isEqualTo(question.getRequired());
        assertThat(found.getCreatedAt()).isEqualTo(question.getCreatedAt());
    }

    @Test
    void 설문_전체_저장_테스트() {
        Survey survey = Survey.createForInsert("테스트 설문", "테스트 설문 설명");

        QuestionGroup group = QuestionGroup.create(1);
        survey.setQuestionGroups(List.of(group));
        group.setSurvey(survey);

        List<Question> questions = new ArrayList<>();
        questions.add(Question.create(1, "테스트 문항-TEXT", null, InputType.TEXT, true, null));
        questions.add(Question.create(2, "테스트 문항2-LONG_TEXT", null, InputType.LONG_TEXT, true, null));
        questions.add(Question.create(3, "테스트 문항3-RADIO", null, InputType.RADIO, true, List.of("1","2","3")));
        questions.add(Question.create(4, "테스트 문항4-CHECKBOX", null, InputType.CHECKBOX, true, List.of("4","5","6")));

        for(Question q : questions) {
            q.setQuestionGroup(group);
        }
        group.setQuestions(questions);


        Survey saved = surveyRepository.save(survey);
        System.out.println("saved : " + saved);
        Survey found = surveyRepository.findById(saved.getId()).orElse(null);
        System.out.println("found : " + found);

        assertThat(saved.getLatestVersion()).isEqualTo(found.getLatestVersion());
        for(int i=0; i<questions.size(); i++) {
            Question savedQuestion = saved.getQuestionGroups().get(0).getQuestions().get(i);
            Question foundQuestion = found.getQuestionGroups().get(0).getQuestions().get(i);

            assertThat(savedQuestion.getId()).isEqualTo(foundQuestion.getId());
            assertThat(savedQuestion.getPosition()).isEqualTo(foundQuestion.getPosition());
            assertThat(savedQuestion.getLabel()).isEqualTo(foundQuestion.getLabel());
            assertThat(savedQuestion.getDescription()).isEqualTo(foundQuestion.getDescription());
            assertThat(savedQuestion.getInputType()).isEqualTo(foundQuestion.getInputType());
            assertThat(savedQuestion.getRequired()).isEqualTo(foundQuestion.getRequired());
            assertThat(savedQuestion.getOptions()).isEqualTo(foundQuestion.getOptions());
            assertThat(savedQuestion.getCreatedAt()).isEqualTo(foundQuestion.getCreatedAt());

        }


    }
}
