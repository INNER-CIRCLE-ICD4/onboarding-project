package fastcampus.inguk_onboarding.form.response.repository;

import fastcampus.inguk_onboarding.form.response.Response;
import fastcampus.inguk_onboarding.form.response.application.interfaces.ResponseRepository;
import fastcampus.inguk_onboarding.form.response.jpa.JpaSurveyResponseRepository;
import fastcampus.inguk_onboarding.form.response.repository.entity.response.SurveyAnswerEntity;
import fastcampus.inguk_onboarding.form.response.repository.entity.response.SurveyResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ResponseRepositoryImpl implements ResponseRepository {
    
    private final JpaSurveyResponseRepository jpaSurveyResponseRepository;

    @Override
    public Response save(Response response) {
        try {
            log.info("응답 저장 시작 - surveyId: {}, surveyVersionId: {}", response.getSurveyId(), response.getSurveyVersionId());
            
            SurveyResponseEntity responseEntity = new SurveyResponseEntity();
            responseEntity.setSurveyId(response.getSurveyId());
            responseEntity.setSurveyVersionId(response.getSurveyVersionId());
            
            // 응답 항목들을 답변 엔티티로 변환하여 저장
            response.getResponseItems().forEach(item -> {
                SurveyAnswerEntity answerEntity = new SurveyAnswerEntity();
                answerEntity.setItemOrder(item.getItemOrder());
                answerEntity.setAnswer(item.getAnswer());
                responseEntity.addAnswer(answerEntity);
            });
            
            SurveyResponseEntity savedEntity = jpaSurveyResponseRepository.save(responseEntity);
            log.info("응답 저장 성공 - responseId: {}", savedEntity.getId());
            
            return toResponse(savedEntity);
        } catch (Exception e) {
            log.error("응답 저장 실패", e);
            throw new RuntimeException("응답 저장 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    @Override
    public Response findById(Long id) {
        try {
            log.info("응답 조회 시작 - responseId: {}", id);
            
            SurveyResponseEntity responseEntity = jpaSurveyResponseRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("응답을 찾을 수 없습니다. ID: " + id));
            
            log.info("응답 엔티티 조회 성공 - responseId: {}, answersSize: {}", id, responseEntity.getAnswers().size());
            
            Response response = toResponse(responseEntity);
            log.info("응답 변환 성공 - responseId: {}", id);
            
            return response;
        } catch (Exception e) {
            log.error("응답 조회 실패 - responseId: {}", id, e);
            throw new RuntimeException("응답 조회 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Response> findBySurveyId(Long surveyId) {
        try {
            log.info("설문조사 전체 응답 조회 시작 - surveyId: {}", surveyId);
            
            List<SurveyResponseEntity> responseEntities = jpaSurveyResponseRepository.findBySurveyId(surveyId);
            
            log.info("설문조사 전체 응답 엔티티 조회 성공 - surveyId: {}, 응답 수: {}", surveyId, responseEntities.size());
            
            List<Response> responses = responseEntities.stream()
                    .map(this::toResponse)
                    .toList();
            
            log.info("설문조사 전체 응답 변환 성공 - surveyId: {}, 응답 수: {}", surveyId, responses.size());
            
            return responses;
        } catch (Exception e) {
            log.error("설문조사 전체 응답 조회 실패 - surveyId: {}", surveyId, e);
            throw new RuntimeException("설문조사 응답 조회 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }
    
    private Response toResponse(SurveyResponseEntity entity) {
        try {
            log.debug("응답 변환 시작 - entityId: {}", entity.getId());
            
            Response response = Response.builder()
                    .id(entity.getId())
                    .surveyId(entity.getSurveyId())
                    .surveyVersionId(entity.getSurveyVersionId())
                    .responseItems(entity.getAnswers().stream()
                            .map(answer -> {
                                try {
                                    return fastcampus.inguk_onboarding.form.response.domain.ResponseItem.builder()
                                            .itemOrder(answer.getItemOrder())
                                            .answer(answer.getAnswer())
                                            .build();
                                } catch (Exception e) {
                                    // 삭제된 항목에 대한 응답 처리
                                    log.warn("삭제된 항목에 대한 응답 발견. 응답 ID: {}, 답변: {}", 
                                            entity.getId(), answer.getAnswer());
                                    
                                    // 삭제된 항목의 경우 순서를 -1로 설정하여 구분
                                    return fastcampus.inguk_onboarding.form.response.domain.ResponseItem.builder()
                                            .itemOrder(-1) // 삭제된 항목 표시
                                            .answer(answer.getAnswer() + " [삭제된 항목]")
                                            .build();
                                }
                            })
                            .toList())
                    .build();
                    
            log.debug("응답 변환 완료 - entityId: {}, responseItemsSize: {}", entity.getId(), response.getResponseItems().size());
            return response;
        } catch (Exception e) {
            log.error("응답 변환 실패 - entityId: {}", entity.getId(), e);
            throw new RuntimeException("응답 변환 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }
}
