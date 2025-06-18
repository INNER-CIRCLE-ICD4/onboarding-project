package com.multi.sungwoongonboarding.forms.presentation;

import com.multi.sungwoongonboarding.forms.infrastructure.FormJpaRepository;
import com.multi.sungwoongonboarding.options.infrastructure.OptionsJpaRepository;
import com.multi.sungwoongonboarding.questions.infrastructure.QuestionJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class FormRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    FormJpaRepository formJpaRepository;
    @Autowired
    OptionsJpaRepository optionsJpaRepository;
    @Autowired
    QuestionJpaRepository questionJpaRepository;

    @AfterEach
    public void tearDown() {
        // 테스트 후 데이터 정리
        // 예: 데이터베이스 초기화, 테스트 데이터 삭제 등
        // 현재는 특별한 정리 작업이 필요하지 않음
        optionsJpaRepository.deleteAll();
        questionJpaRepository.deleteAll();
        formJpaRepository.deleteAll();
    }


    @Test
    @DisplayName("설문 조사서 등록 요청 성공 - RequestBody")
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
                      "isRequired": true,
                      "optionCreateRequests": [
                        {
                          "optionText": "10대"
                        },
                        {
                          "optionText": "20대"
                        },
                        {
                          "optionText": "30대"
                        },
                        {
                          "optionText": "40대 이상"
                        }
                      ]
                    },
                    {
                      "questionText": "저희 서비스를 얼마나 자주 이용하십니까?",
                      "questionType": "MULTIPLE_CHOICE",
                      "isRequired": true,
                      "optionCreateRequests": [
                        {
                          "optionText": "매일"
                        },
                        {
                          "optionText": "주 2-3회"
                        },
                        {
                          "optionText": "월 1-2회"
                        },
                        {
                          "optionText": "거의 이용하지 않음"
                        }
                      ]
                    },
                    {
                      "questionText": "가장 만족스러운 기능은 무엇입니까? (복수 선택 가능)",
                      "questionType": "SINGLE_CHOICE",
                      "isRequired": false,
                      "optionCreateRequests": [
                        {
                          "optionText": "사용자 인터페이스"
                        },
                        {
                          "optionText": "검색 기능"
                        },
                        {
                          "optionText": "고객 지원"
                        },
                        {
                          "optionText": "콘텐츠 품질"
                        },
                        {
                          "optionText": "기타"
                        }
                      ]
                    },
                    {
                      "questionText": "서비스 개선을 위한 제안사항이 있으시면 자유롭게 작성해주세요.",
                      "questionType": "SHORT_ANSWER",
                      "isRequired": false,
                      "optionCreateRequests": []
                    },
                    {
                      "questionText": "서비스 개선을 위한 제안사항이 있으시면 자유롭게 작성해주세요.(500자 이내)",
                      "questionType": "LONG_ANSWER",
                      "isRequired": true,
                      "optionCreateRequests": []
                    }
                  ]
                }
                """;


        // Expected
        // FormController를 사용하여 설문 조사서 등록 요청
        mockMvc.perform(post("/api/v1/forms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestData))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data.questionResponses", hasSize(5)))
                .andExpect(jsonPath("$.data.questionResponses[0].optionResponses", hasSize(4)));

    }

    @Test
    @DisplayName("설문 조사서 등록 요청 실패 - 설문지 제목과 질문이 비어있는 경우")
    public void testCreateFormWithInvalidRequest() throws Exception {

        // Given
        // 잘못된 설문 조사서 등록 데이터 준비
        // 제목과 질문이 비어있으
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
                .andExpect(jsonPath("$.errorDetail").exists())
                .andExpect(jsonPath("$.errorDetail", hasSize(2)));

    }

    @Test
    @DisplayName("설문 조사서 등록 요청 실패 - 질문이 선택 형식인데 옵션의 내용이 없는 경우")
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
                      "isRequired": true,
                      "optionCreateRequests": [
                        {
                          "optionText": "10대"
                        },
                        {
                          "optionText": "20대"
                        },
                        {
                          "optionText": "30대"
                        },
                        {
                          "optionText": "40대 이상"
                        }
                      ]
                    },
                    {
                      "questionText": "저희 서비스를 얼마나 자주 이용하십니까?",
                      "questionType": "MULTIPLE_CHOICE",
                      "isRequired": true,
                      "optionCreateRequests": [
                        {
                          "optionText": "매일"
                        },
                        {
                          "optionText": "주 2-3회"
                        },
                        {
                          "optionText": "월 1-2회"
                        },
                        {
                          "optionText": "거의 이용하지 않음"
                        }
                      ]
                    },
                    {
                      "questionText": "가장 만족스러운 기능은 무엇입니까? (복수 선택 가능)",
                      "questionType": "SINGLE_CHOICE",
                      "isRequired": false,
                      "optionCreateRequests": [
                        {
                          "optionText": ""
                        },
                        {
                          "optionText": ""
                        },
                        {
                          "optionText": "고객 지원"
                        },
                        {
                          "optionText": "콘텐츠 품질"
                        },
                        {
                          "optionText": "기타"
                        }
                      ]
                    },
                    {
                      "questionText": "서비스 개선을 위한 제안사항이 있으시면 자유롭게 작성해주세요.",
                      "questionType": "SHORT_ANSWER",
                      "isRequired": false,
                      "optionCreateRequests": []
                    },
                    {
                      "questionText": "서비스 개선을 위한 제안사항이 있으시면 자유롭게 작성해주세요.(500자 이내)",
                      "questionType": "LONG_ANSWER",
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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorDetail", hasSize(2)));
    }


}