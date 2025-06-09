package fc.innercircle.sanghyukonboarding.surveyreply.model.vo

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Converter
import jakarta.persistence.Embeddable

@Embeddable
class SelectedItemOptions(
    @Column(
        name = "selected_item_option_text",
        nullable = false,
        columnDefinition = "TEXT not null comment '선택된 항목 옵션 텍스트'(단일/다중 선택 리스트)"
    )
    @Convert(converter = SelectedItemOptionsConverter::class)
    val texts: List<String> = emptyList(),
) {
    /**
     * ex) DB: ["사과", "바나나", "체리"] <--> texts: ["사과", "바나나", "체리"]
     */
    @Converter
    class SelectedItemOptionsConverter : AttributeConverter<SelectedItemOptions, String> {
        override fun convertToDatabaseColumn(attribute: SelectedItemOptions): String =
            attribute.texts.joinToString(separator = ",")

        override fun convertToEntityAttribute(dbData: String): SelectedItemOptions {
            val texts = dbData.split(",").map { it.trim() }
            return SelectedItemOptions(texts)
        }
    }
}
