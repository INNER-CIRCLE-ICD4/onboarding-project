package com.multi.sungwoongonboarding.forms.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.multi.sungwoongonboarding.common.config.JpaAuditingConfig;
import com.multi.sungwoongonboarding.forms.application.repository.FormRepository;
import com.multi.sungwoongonboarding.forms.domain.Forms;
import com.multi.sungwoongonboarding.forms.dto.FormCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@EnableJpaAuditing
public class FormServiceTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    FormService formService;

    @Autowired
    FormRepository formRepository;

    @Test
    @DisplayName("설문 조사서 등록 테스트")
    public void testCreateForm() throws JsonProcessingException {
        // Given
        // 설문 조사서 등록에 필요한 데이터 준비
        String jsonData = testData();
        FormCreateRequest formCreateRequest = objectMapper.readValue(jsonData, FormCreateRequest.class);

        // When
        // FormService를 사용하여 설문 조사서 등록
        formService.createForms(formCreateRequest);
        List<Forms> all = formRepository.findAll();

        // Then
        // 등록된 설문 조사서가 올바르게 저장되었는지 검증
        assertThat(all.size()).isEqualTo(1);
        assertThat(all.get(0).getQuestions().size()).isEqualTo(5);
    }

    private String testData(){

        return """
                {
                  "title": "사용자 만족도 설문조사",
                  "description": "저희 서비스에 대한 귀하의 의견을 듣고자 합니다.",
                  "questionCreateRequests": [
                    {
                      "questionText": "귀하의 연령대는 어떻게 되십니까?",
                      "questionType": "SINGLE_CHOICE",
                      "questionOrder": 1,
                      "isRequired": true,
                      "optionCreateRequests": [
                        {
                          "optionText": "10대",
                          "optionOrder": 1
                        },
                        {
                          "optionText": "20대",
                          "optionOrder": 2
                        },
                        {
                          "optionText": "30대",
                          "optionOrder": 3
                        },
                        {
                          "optionText": "40대 이상",
                          "optionOrder": 4
                        }
                      ]
                    },
                    {
                      "questionText": "저희 서비스를 얼마나 자주 이용하십니까?",
                      "questionType": "MULTIPLE_CHOICE",
                      "questionOrder": 2,
                      "isRequired": true,
                      "optionCreateRequests": [
                        {
                          "optionText": "매일",
                          "optionOrder": 1
                        },
                        {
                          "optionText": "주 2-3회",
                          "optionOrder": 2
                        },
                        {
                          "optionText": "월 1-2회",
                          "optionOrder": 3
                        },
                        {
                          "optionText": "거의 이용하지 않음",
                          "optionOrder": 4
                        }
                      ]
                    },
                    {
                      "questionText": "가장 만족스러운 기능은 무엇입니까? (복수 선택 가능)",
                      "questionType": "SINGLE_CHOICE",
                      "questionOrder": 3,
                      "isRequired": false,
                      "optionCreateRequests": [
                        {
                          "optionText": "사용자 인터페이스",
                          "optionOrder": 1
                        },
                        {
                          "optionText": "검색 기능",
                          "optionOrder": 2
                        },
                        {
                          "optionText": "고객 지원",
                          "optionOrder": 3
                        },
                        {
                          "optionText": "콘텐츠 품질",
                          "optionOrder": 4
                        },
                        {
                          "optionText": "기타",
                          "optionOrder": 5
                        }
                      ]
                    },
                    {
                      "questionText": "서비스 개선을 위한 제안사항이 있으시면 자유롭게 작성해주세요.",
                      "questionType": "SHORT_ANSWER",
                      "questionOrder": 4,
                      "isRequired": false,
                      "optionCreateRequests": []
                    },
                    {
                      "questionText": "서비스 개선을 위한 제안사항이 있으시면 자유롭게 작성해주세요.(500자 이내)",
                      "questionType": "LONG_ANSWER",
                      "questionOrder": 5,
                      "isRequired": true,
                      "optionCreateRequests": []
                    }
                  ]
                }
                """;

    }
}