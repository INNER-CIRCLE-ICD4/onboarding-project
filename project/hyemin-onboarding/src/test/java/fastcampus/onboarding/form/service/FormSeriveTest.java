package fastcampus.onboarding.form.service;

import fastcampus.onboarding.common.exception.CustomException;
import fastcampus.onboarding.common.exception.ErrorCode;
import fastcampus.onboarding.form.dto.request.*;
import fastcampus.onboarding.form.entity.Form;
import fastcampus.onboarding.form.entity.Item;
import fastcampus.onboarding.form.entity.ItemType;
import fastcampus.onboarding.form.entity.Option;
import fastcampus.onboarding.form.repository.FormRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FormServiceTest {

    @Mock
    private FormRepository formRepository;

    @InjectMocks
    private FormService formService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createFormSuccess() {
        // given
        FormCreateRequestDto dto = FormCreateRequestDto.builder()
                .formTitle("설문 제목")
                .formContent("설문 내용")
                .items(List.of(
                        ItemRequestDto.builder()
                                .itemTitle("문항1")
                                .itemContent("문항1 내용")
                                .itemType(ItemType.SINGLE_CHOICE)
                                .isRequired(true)
                                .options(List.of(
                                        OptionRequestDto.builder().optionContent("옵션1").build(),
                                        OptionRequestDto.builder().optionContent("옵션2").build()
                                ))
                                .build()
                ))
                .build();

        // when
        formService.createForm(dto);

        // then
        verify(formRepository, times(1)).save(any(Form.class));
    }

    @Test
    void createFormTooManyTen() {
        // given
        List<ItemRequestDto> items =
                java.util.stream.IntStream.range(0, 11)
                        .mapToObj(i -> ItemRequestDto.builder()
                                .itemTitle("문항" + i)
                                .itemContent("내용" + i)
                                .itemType(ItemType.SINGLE_CHOICE)
                                .isRequired(true)
                                .options(List.of())
                                .build())
                        .toList();

        FormCreateRequestDto dto = FormCreateRequestDto.builder()
                .formTitle("제목")
                .formContent("내용")
                .items(items)
                .build();

        // when & then
        assertThrows(CustomException.class, () -> formService.createForm(dto));
        verify(formRepository, never()).save(any(Form.class));
    }

    @Test
    void updateFormSuccess() {
        // given
        Long formSeq = 1L;

        // 기존 Form, Item, Option 구성
        Option option = Option.builder()
                .optionContent("기존 옵션")
                .build();
        // 수동으로 식별자 설정
        ReflectionTestUtils.setField(option, "optionSeq", 101L);

        Item item = Item.builder()
                .itemTitle("기존 문항")
                .itemContent("기존 문항 내용")
                .itemType(ItemType.SINGLE_CHOICE)
                .isRequired(true)
                .build();
        item.addOption(option);
        ReflectionTestUtils.setField(item, "itemSeq", 201L);

        Form form = Form.builder()
                .formTitle("기존 제목")
                .formContent("기존 내용")
                .build();
        form.addItem(item);

        when(formRepository.findById(formSeq)).thenReturn(Optional.of(form));

        // 수정 요청 DTO 구성
        OptionUpdateRequestDto optionDto = OptionUpdateRequestDto.builder()
                .optionSeq(101L)
                .optionContent("수정된 옵션")
                .build();

        ItemUpdateRequestDto itemDto = ItemUpdateRequestDto.builder()
                .itemSeq(201L)
                .itemTitle("수정된 문항")
                .itemContent("수정된 문항 내용")
                .itemType(ItemType.MULTIPLE_CHOICE)
                .isRequired(false)
                .options(List.of(optionDto))
                .build();

        FormUpdateRequestDto updateDto = FormUpdateRequestDto.builder()
                .formTitle("수정된 제목")
                .formContent("수정된 내용")
                .items(List.of(itemDto))
                .build();

        // when
        formService.updateForm(formSeq, updateDto);

        // then
        verify(formRepository).save(form);
        assertEquals("수정된 제목", form.getFormTitle());
        assertEquals("수정된 내용", form.getFormContent());

        Item updatedItem = form.getItems().get(0);
        assertEquals("수정된 문항", updatedItem.getItemTitle());
        assertEquals("수정된 문항 내용", updatedItem.getItemContent());
        assertEquals(ItemType.MULTIPLE_CHOICE, updatedItem.getItemType());
        assertFalse(updatedItem.isRequired());

        Option updatedOption = updatedItem.getOptions().get(0);
        assertEquals("수정된 옵션", updatedOption.getOptionContent());
    }


    @Test
    void updateFormNotFound() {
        // given
        Long formSeq = 999L;
        FormUpdateRequestDto dto = FormUpdateRequestDto.builder()
                .formTitle("제목")
                .formContent("내용")
                .items(List.of())
                .build();

        when(formRepository.findById(formSeq)).thenReturn(Optional.empty());

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> formService.updateForm(formSeq, dto));
        assertEquals(ErrorCode.FORM_NOT_FOUND, exception.getErrorCode());
        verify(formRepository, never()).save(any());
    }
}