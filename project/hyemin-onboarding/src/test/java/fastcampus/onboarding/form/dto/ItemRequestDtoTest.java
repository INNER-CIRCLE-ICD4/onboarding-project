package fastcampus.onboarding.form.dto;


import fastcampus.onboarding.form.dto.request.ItemRequestDto;
import fastcampus.onboarding.form.dto.request.OptionRequestDto;
import fastcampus.onboarding.form.entity.ItemType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemRequestDtoTest {
    @Test
    void testItemRequestDtoCreation() {
        String itemTitle = "질문 제목";
        ItemType itemType = ItemType.SINGLE_CHOICE;
        List<OptionRequestDto> options = Arrays.asList(
                new OptionRequestDto("옵션 1"),
                new OptionRequestDto("옵션 2")
        );

        // when
        ItemRequestDto dto = new ItemRequestDto();
        dto.setItemTitle(itemTitle);
        dto.setItemType(itemType);
        dto.setOptions(options);

        // then
        assertEquals(itemTitle, dto.getItemTitle());
        assertEquals(itemType, dto.getItemType());
        assertEquals(2, dto.getOptions().size());
        assertEquals("옵션 1", dto.getOptions().get(0).getOptionContent());
    }
}
