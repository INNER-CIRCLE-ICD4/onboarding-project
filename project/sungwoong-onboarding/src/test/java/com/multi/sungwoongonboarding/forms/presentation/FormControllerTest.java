package com.multi.sungwoongonboarding.forms.presentation;

import com.multi.sungwoongonboarding.forms.application.FormService;
import com.multi.sungwoongonboarding.forms.dto.FormCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(FormController.class)
class FormControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FormService formService;

    @Test
    @DisplayName("설문 조사서 등록 요청 - RequestBody")
    public void testCreateForm() throws Exception {

        // Given
        // 설문 조사서 등록에 필요한 데이터 준비
        String requestData = """
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

        // When
        // FormController를 사용하여 설문 조사서 등록 요청
        mockMvc.perform(post("/api/v1/forms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestData))
                .andDo(print())
                .andExpect(status().isOk());

        //Then
        // FormService의 createForms 메소드가 호출되었는지 검증
//        verify(formService, times(1)).createForms(any(FormCreateRequest.class));

    }

    @Test
    @DisplayName("설문 조사서 등록 요청 - 잘못된 RequestBody")
    public void testCreateFormWithInvalidRequest() throws Exception {

        // Given
        // 잘못된 설문 조사서 등록 데이터 준비
        String invalidRequestData = """
                {
                  "title": "",
                  "description": "",
                  "questionCreateRequests": []
                }
                """;

        // When & Then
        // 잘못된 요청에 대해 400 Bad Request 응답을 기대
        mockMvc.perform(post("/api/v1/forms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestData))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("E-004"))
                .andExpect(jsonPath("$.data").exists());

    }

    @Test
    @DisplayName("설문 조사서 등록 요청 테스트 - 잘못된 RequestBody, 질문이 선택 형식인데 옵션의 내용이 없는 경우")
    public void testCreateForm_fail() throws Exception {

        // Given
        // 설문 조사서 등록에 필요한 데이터 준비
        String requestData = """
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
                          "optionText": "",
                          "optionOrder": 1
                        },
                        {
                          "optionText": "",
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

        // When
        // FormController를 사용하여 설문 조사서 등록 요청
        mockMvc.perform(post("/api/v1/forms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestData))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


}