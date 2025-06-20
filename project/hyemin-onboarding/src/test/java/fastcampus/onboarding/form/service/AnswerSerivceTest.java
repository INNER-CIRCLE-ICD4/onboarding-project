package fastcampus.onboarding.form.service;

import fastcampus.onboarding.form.dto.request.AnswerRequestDto;
import fastcampus.onboarding.form.entity.*;
import fastcampus.onboarding.form.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class AnswerSerivceTest {
    @Mock
    private FormRepository formRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private OptionRepository optionRepository;
    @Mock
    private FormResponseRepository formResponseRepository;
    @Mock
    private ItemRepsponseRepository itemRepsponseRepository;

    @InjectMocks
    private AnswerService answerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createdAnswer_success() {
        // given
        Long formSeq = 1L;

        // Option
        Option option = Option.builder()
                .optionContent("옵션1")
                .build();
        ReflectionTestUtils.setField(option, "optionSeq", 101L);

        // Item
        Item item = Item.builder()
                .itemTitle("질문1")
                .itemContent("질문 내용")
                .itemType(ItemType.SINGLE_CHOICE)
                .isRequired(true)
                .build();
        ReflectionTestUtils.setField(item, "itemSeq", 201L);
        item.addOption(option);

        // Form
        Form form = Form.builder()
                .formTitle("테스트 설문지")
                .formContent("내용")
                .build();
        form.addItem(item);

        // Repository mock
        when(formRepository.findById(formSeq)).thenReturn(Optional.of(form));
        when(itemRepository.findByForm(form)).thenReturn(List.of(item));
        when(optionRepository.findByOptionSeqInAndItem(List.of(101L), item)).thenReturn(List.of(option));

        FormResponse mockFormResponse = FormResponse.builder().form(form).build();
        when(formResponseRepository.save(any(FormResponse.class))).thenReturn(mockFormResponse);

        when(itemRepsponseRepository.save(any(ItemResponse.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 요청 DTO
        AnswerRequestDto.ItemAnswerDto itemAnswerDto = AnswerRequestDto.ItemAnswerDto.builder()
                .itemSeq(201L)
                .optionSeqs(List.of(101L))
                .build();

        AnswerRequestDto answerRequestDto = AnswerRequestDto.builder()
                .answers(List.of(itemAnswerDto))
                .build();

        // when + then (예외 발생하지 않으면 성공)
        assertDoesNotThrow(() -> answerService.createdAnswer(formSeq, answerRequestDto));

        // 검증
        verify(formResponseRepository, times(1)).save(any(FormResponse.class));
        verify(itemRepsponseRepository, times(1)).save(any(ItemResponse.class));
    }

}
