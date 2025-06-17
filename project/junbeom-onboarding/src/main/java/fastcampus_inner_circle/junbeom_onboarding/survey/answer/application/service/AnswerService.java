package fastcampus_inner_circle.junbeom_onboarding.survey.answer.application.service;

import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.dto.AnswerRequest;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.dto.AnswerResponse;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.mapper.AnswerMapper;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.mapper.AnswerToDomainMapper;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.entity.AnswerJpaEntity;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.repository.AnswerJpaRepository;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.model.Answer;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.application.service.exception.AnswerValidationException;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;

    @Transactional
    public AnswerResponse submitAnswer(AnswerRequest request) {
        if (request.getFormId() == null || request.getAnswers() == null || request.getAnswers().isEmpty()) {
            throw new AnswerValidationException("formId와 answers는 필수입니다.");
        }

        Answer domain1 = AnswerToDomainMapper.toDomain(request);


        // 1. DTO → 도메인
        Answer domain = AnswerMapper.toDomain(request);
        // 2. 도메인 → JPA 엔티티
        var entity = AnswerMapper.toEntity(domain);
        // 3. 저장
        Answer save = answerRepository.save(domain);
        // 4. JPA 엔티티 → 도메인
        var savedDomain = AnswerMapper.toDomain(saved);
        // 5. 도메인 → DTO
        return AnswerMapper.toResponse(savedDomain);
    }

    @Transactional(readOnly = true)
    public List<AnswerResponse> getAnswers(Long formId) {
        List<AnswerJpaEntity> answers = answerJpaRepository.findByFormId(formId);
        return answers.stream()
                .map(AnswerMapper::toDomain)
                .map(AnswerMapper::toResponse)
                .collect(Collectors.toList());
    }
} 