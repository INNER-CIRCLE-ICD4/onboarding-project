package fastcampus_inner_circle.junbeom_onboarding.survey.answer.application.service;

import java.util.List;
import java.util.Optional;

import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.mapper.AnwerToResponseDtoMapperForUpdate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.dto.AnswerRequest;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.dto.AnswerResponse;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.mapper.AnswerToDomainMapper;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.mapper.AnswerToResponseDtoMapper;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.application.service.exception.AnswerValidationException;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.model.Answer;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;

    @Transactional
    public AnswerResponse submitAnswer(AnswerRequest request) {
        if (request.getFormId() == null || request.getAnswers() == null || request.getAnswers().isEmpty()) {
            throw new AnswerValidationException("formId와 answers는 필수입니다.");
        }

        Answer domain = AnswerToDomainMapper.toDomain(request);
        Answer answer = answerRepository.save(domain);
        return AnswerToResponseDtoMapper.toResponse(answer);
    }

    @Transactional(readOnly = true)
    public Optional<AnswerResponse> getAnswerById(Long answerId) {
        return answerRepository.findById(answerId)
                .map(AnswerToResponseDtoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<AnswerResponse> getAnswersByFormId(Long formId) {
        List<Answer> answers = answerRepository.findByFormId(formId);
        return answers.stream()
                .map(AnwerToResponseDtoMapperForUpdate::toResponse)
                .toList();

    }
}