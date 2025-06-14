//package fastcampus.onboarding.form.service;
//
//import fastcampus.onboarding.form.dto.request.FormCreateRequestDto;
//import fastcampus.onboarding.form.entity.Form;
//import fastcampus.onboarding.form.entity.Item;
//import fastcampus.onboarding.form.entity.ItemType;
//import fastcampus.onboarding.form.entity.Option;
//import fastcampus.onboarding.form.dto.request.ItemRequestDto;
//import fastcampus.onboarding.form.dto.request.OptionRequestDto;
//import fastcampus.onboarding.form.repository.FormRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import java.util.List;
//import static org.assertj.core.api.Assertions.assertThat;
//
//@ExtendWith(MockitoExtension.class)
//public class FormServiceTest {
//    @Mock
//    private FormRepository formRepository;
//    @InjectMocks
//    private FormService formService;
//
//    @Test
//    void shouldCreateFormWithItemsAndOptions() {
//        // Given
//        OptionRequestDto optionDto = OptionRequestDto.builder()
//                .optionContent("Option 1")
//                .build();
//        ItemRequestDto itemDto = ItemRequestDto.builder()
//                .itemTitle("Question 1")
//                .itemContent("What is your choice?")
//                .itemType(ItemType.MULTIPLE_CHOICE)
//                .isRequired(true)
//                .options(List.of(optionDto))
//                .build();
//        FormCreateRequestDto dto = FormCreateRequestDto.builder()
//                .formTitle("Test Form")
//                .formContent("This is a test form")
//                .items(List.of(itemDto))
//                .build();
//
//        // When
//        formService.createForm(dto);
//
//        // Then
//        List<Form> forms = formRepository.findAll();
//        assertThat(forms).hasSize(1);
//        Form savedForm = forms.get(0);
//        assertThat(savedForm.getFormTitle()).isEqualTo("Test Form");
//        assertThat(savedForm.getFormContent()).isEqualTo("This is a test form");
//        assertThat(savedForm.getItems()).hasSize(1);
//
//        Item savedItem = savedForm.getItems().get(0);
//        assertThat(savedItem.getItemTitle()).isEqualTo("Question 1");
//        assertThat(savedItem.getItemContent()).isEqualTo("What is your choice?");
//        assertThat(savedItem.getItemType()).isEqualTo(ItemType.MULTIPLE_CHOICE);
//        assertThat(savedItem.isRequired()).isTrue();
//        assertThat(savedItem.getOptions()).hasSize(1);
//
//        Option savedOption = savedItem.getOptions().get(0);
//        assertThat(savedOption.getOptionContent()).isEqualTo("Option 1");
//    }
//}