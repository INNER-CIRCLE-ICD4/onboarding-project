package fastcampus.onboarding.form.dto.response;

import fastcampus.onboarding.form.entity.ItemResponse;
import fastcampus.onboarding.form.entity.ItemType;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Slf4j
@ToString
public class AnswerItemDto {
    private Long itemResponseSeq;
    private Long itemSeq;
    private String textValue;
    private String title;
    private String content;
    private ItemType itemType;
    private Boolean isRequired;
    private List<AnwerOptionDto> selectedOptions;

    public AnswerItemDto(Long itemResponseSeq, Long itemSeq, String textValue, String itemTitleSnapshot, String itemContentSnapshot, Boolean isRequiredSnapshot) {
        this.itemResponseSeq = itemResponseSeq;
        this.itemSeq = itemSeq;
        this.textValue = textValue;
        this.title = itemTitleSnapshot;
        this.content = itemContentSnapshot;
        this.isRequired = isRequiredSnapshot;
    }

    public static AnswerItemDto fromEntity(ItemResponse itemResponse) {
        log.info("fromEntity itemResponse={}", itemResponse.toString());
        return new AnswerItemDto(
                itemResponse.getItemResponseSeq(),
                itemResponse.getItemSeq(),
                itemResponse.getTextValue() != null ? itemResponse.getTextValue() : "",
                itemResponse.getItemTitleSnapshot(),
                itemResponse.getItemContentSnapshot(),
                itemResponse.getIsRequiredSnapshot()
        );
    }
}
