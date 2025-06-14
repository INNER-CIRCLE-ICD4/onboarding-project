package fc.icd.jaehyeononboarding.survey.service;

import fc.icd.jaehyeononboarding.common.model.NoDataResponse;
import fc.icd.jaehyeononboarding.survey.model.dto.QuestionDTO;
import fc.icd.jaehyeononboarding.survey.model.dto.SurveyCreateDTO;
import fc.icd.jaehyeononboarding.survey.model.entity.Question;
import fc.icd.jaehyeononboarding.survey.model.entity.QuestionGroup;
import fc.icd.jaehyeononboarding.survey.model.entity.Survey;
import fc.icd.jaehyeononboarding.survey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class SurveyServiceImpl implements SurveyService {

    private final SurveyRepository surveyRepository;

    @Transactional
    @Override
    public NoDataResponse createSurvey(SurveyCreateDTO createDTO) {

        String name = createDTO.getName();
        String description = createDTO.getDescription();
        List<QuestionDTO> questionDTOs = createDTO.getQuestions();

        Survey survey = Survey.create(name, description, false);
        QuestionGroup group = QuestionGroup.create(1);

        List<Question> list = IntStream.range(0, questionDTOs.size())
                .mapToObj(i -> questionDTOs.get(i).toEntity(i)).toList();

        survey.setQuestionGroups(List.of(group));
        group.setSurvey(survey);
        group.setQuestions(list);

        surveyRepository.save(survey);

        System.out.println(survey);

        return new NoDataResponse();
    }

}
