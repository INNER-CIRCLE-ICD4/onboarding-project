package com.survey.service

import com.survey.dto.ResponseRequest
import com.survey.dto.SurveyItemRequest
import com.survey.dto.SurveyOptionRequest
import com.survey.exception.RequiredFieldMissingException
import com.survey.exception.ResponseOptionNotAllowedException
import com.survey.exception.ResponseRequiredFieldMissingException
import com.survey.exception.ResponseSurveyNotFoundException
import com.survey.exception.ResponseTypeMismatchException
import com.survey.exception.SurveyItemCountException
import com.survey.exception.SurveyNotFoundException
import com.survey.exception.SurveyOptionReactivationException
import com.survey.exception.SurveyOptionsMissingException
import com.survey.exception.SurveyTypeConflictException
import com.survey.exception.SurveyUpdateFieldMissingException
import com.survey.model.Response
import com.survey.model.ResponseItem
import com.survey.model.Survey
import com.survey.model.SurveyItem
import com.survey.model.SurveyOption
import com.survey.repository.ResponseItemRepository
import com.survey.repository.ResponseRepository
import com.survey.repository.SurveyItemRepository
import com.survey.repository.SurveyOptionRepository
import com.survey.repository.SurveyRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class SurveyService(
    private val surveyItemRepository: SurveyItemRepository,
    private val surveyOptionRepository: SurveyOptionRepository,
    private val responseRepository: ResponseRepository,
    private val responseItemRepository: ResponseItemRepository,
    private val surveyRepository: SurveyRepository
) {

    fun createSurvey(title: String, description: String?): Survey {
        if(title.isBlank()) throw RequiredFieldMissingException()
        val survey = Survey(title = title, description = description)
        return surveyRepository.save(survey)
    }

    fun getAllSurveys(): List<Survey> = surveyRepository.findAll()

//    설문의 전체 항목 조회
    fun getSurveyItemsBySurveyId(surveyId: UUID): List<SurveyItem> {
//        설문 ID 존재 확인
        surveyRepository.findById(surveyId).orElseThrow { SurveyNotFoundException() }

//        설문 항목만 필터링해서 반환
        return surveyItemRepository.findAll().filter{it.survey.id == surveyId}
    }
    
//    문항 저장
    fun saveSurveyItem(request: SurveyItemRequest): SurveyItem {
//        필수 필드 확인
        if(request.question.isBlank()) throw RequiredFieldMissingException()

        val options = request.options ?: throw SurveyOptionsMissingException()

//        선택형 항목인데 옵션이 없는 경우
        if(request.type in listOf("single_choice", "multiple_choice") && request.options.isEmpty()) {
            throw RequiredFieldMissingException()
        }
        
        // 항목 수가 10개 초과하는 경우
        val itemCount = surveyItemRepository.findAll().count {it.survey.id == request.surveyId}
        if(itemCount >= 10) throw SurveyItemCountException()

        val survey = surveyRepository.findById(request.surveyId).orElseThrow(){SurveyNotFoundException()}

        val surveyItem = SurveyItem(
            survey = survey,
            question = request.question,
            type = request.type,
            itemOrder = request.itemOrder
        )

        val savedItem = surveyItemRepository.save(surveyItem)

        val savedOptions: MutableList<SurveyOption> = options.map { opt ->
            SurveyOption(
                surveyItem = savedItem,
                optionValue = opt.optionValue,
                optionOrder = opt.optionOrder
            )
        }.toMutableList()
        surveyOptionRepository.saveAll(savedOptions)

        return savedItem.copy(options = savedOptions)
    }

//    문항 수정
    fun updateSurveyItem(id: UUID, request: SurveyItemRequest): SurveyItem {
//        1 - 기본 항목 조회 및 기본 검증
        val existing = surveyItemRepository.findById(id).orElseThrow { SurveyNotFoundException() }
    
//        필수 필드 확인
        if(request.question.isBlank()) throw SurveyUpdateFieldMissingException()

//        타입이 변경되었고, 기존 응답이 존재하는 경우
        if(existing.type != request.type) {
            val hasResponse = responseItemRepository.findAll().any {it.surveyItem.id == id}
            if(hasResponse) throw SurveyTypeConflictException()
        }

//        2 - 항목 본문(question, type, order) 업데이트
        val updatedItem = existing.copy(
            question = request.question,
            type = request.type,
            itemOrder = request.itemOrder
        )
        surveyItemRepository.save(updatedItem)

//        3 - 옵션 수정, 삭제, 추가 반영
//        기존내용과 변함 없는 option 들은 수정하지 않기 위해 참고할 기존 옵션들 조회(현재 options에서 갖고온 id 들 중에서만)
        val orgOpts = surveyOptionRepository.findAll().filter{it.surveyItem.id == id}

//        사용자에게 받은 options
        val newDtos = request.options ?: emptyList()
        val existingOpts = surveyOptionRepository.findAll().filter {it.surveyItem.id == id}
//        최종 저장할 options
        val toSave = mutableListOf<SurveyOption>()

        newDtos.forEach { dto ->
            if(dto.id != null) {
//                기존 옵션
                val org = surveyOptionRepository.findById(dto.id).orElseThrow({SurveyUpdateFieldMissingException()})

//                active false -> true로 변경하려는 경우
                if(!org.active && dto.active) {
                    throw SurveyOptionReactivationException()
                }

                if(!dto.active) {
//                    옵션 삭제
                    org.active = false
                    org.updateDt = LocalDateTime.now()
                    toSave += org
                } else {
//                    수정 or 변경 없음
                    val unchanged = org.optionValue == dto.optionValue && org.optionOrder == dto.optionOrder && org.active
                    if(unchanged) {
//                        옵션 변경 없음
                        toSave += org
                    } else {
//                        수정된 경우 원본 active false, 신규 등록
                        org.active = false
                        org.updateDt = LocalDateTime.now()
                        toSave += org

                        val created = SurveyOption(
                            surveyItem = updatedItem,
                            optionValue = dto.optionValue,
                            optionOrder = dto.optionOrder,
                            active = true
                        )
                        toSave += created
                    }
                }
            } else if(dto.active) {
                // 신규 생성
                toSave += SurveyOption(
                    surveyItem = updatedItem,
                    optionValue = dto.optionValue,
                    optionOrder = dto.optionOrder,
                    active = true
                )
            }
            // active = false & id = null은 무시(삭제 의도로 판단함)
        }
        surveyOptionRepository.saveAll(toSave)
        
//        4 - JSON 응답용: active true > false 순서, 그 외 order 순 정렬
        val sortedOptions = toSave.sortedWith(compareByDescending<SurveyOption> { it.active }.thenBy { it.optionOrder }).toMutableList()

        return updatedItem.copy(options = sortedOptions)
    }

    // 특정 문항의 옵션 목록 조회
    fun getOptionsByItemId(itemId: UUID): List<SurveyOption> {
        val item = surveyItemRepository.findById(itemId).orElseThrow { SurveyNotFoundException() }
        return surveyOptionRepository.findAll().filter { it.surveyItem.id == item.id }
    }

//    문항 옵션 저장
//    fun saveSurveyOption(option: SurveyOptionRequest): SurveyOption {
//        if(option.optionValue.isBlank()) throw RequiredFieldMissingException()
//        throw UnsupportedOperationException("SurveyOption 단독 생성은 지원하지 않습니다. SurveyItem 생성 시 함께 전달되어야 합니다.")
//    }

//    설문 응답 저장
    fun saveResponse(request: ResponseRequest): Response {
//        최소 한개 이상의 답변이 있어야 함
        if(request.responseItems.isEmpty()) throw ResponseRequiredFieldMissingException()

//        설문이 존재하는지 확인
        val survey = surveyRepository.findById(request.surveyId).orElseThrow{ ResponseSurveyNotFoundException() }

//        response 생성, 저장
        val response = Response(
            survey = survey,
            respondent = request.respondent
        )

        val savedResponse = responseRepository.save(response)

//        답변 항목 검증, 변환
        val responseItems = request.responseItems.map {item ->
            val surveyItem = surveyItemRepository.findById(item.surveyItemId).orElseThrow {ResponseTypeMismatchException()}

//            주관식인지 선택형인지 검증
            if(!item.answer.isNullOrBlank() && item.optionIds.isNotEmpty()) {
                throw ResponseTypeMismatchException()
            }

            when(surveyItem.type) {
//                단일 선택형인데 선택된 옵션이 1개가 아닌 경우
                "single_choice" -> if(item.optionIds.size != 1) throw ResponseTypeMismatchException()
//                다중 선택형인데 선택된 옵션이 없을 경우
                "multi_choice" -> if(item.optionIds.isEmpty()) throw ResponseRequiredFieldMissingException()
//                주관식의 경우 optionIds 무시, answer 필수
                else -> if(item.answer.isNullOrBlank()) throw ResponseRequiredFieldMissingException()
            }

//            설문 옵션이 active=true인 옵션만 허용
            val activeOptionIds = surveyOptionRepository.findAll().filter{it.surveyItem.id == surveyItem.id && it.active}.map{it.id}

//            응답이 허용된 옵션 목록에 포함되어 있는지 확인
            if(!item.optionIds.all {it in activeOptionIds}) {
                throw ResponseOptionNotAllowedException()
            }

//            최종
            val selectedOptions = surveyOptionRepository.findAllById(item.optionIds)
            ResponseItem(
                response = savedResponse,
                surveyItem = surveyItem,
                answer = item.answer ?: "",
                selectedOptions = selectedOptions
            )
        }
        responseItemRepository.saveAll(responseItems)
        return savedResponse
    }

//    모든 응답 조회
    fun getAllResponses(): List<Response> = responseRepository.findAll()

}