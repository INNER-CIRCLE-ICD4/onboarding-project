package fc.innercircle.sanghyukonboarding.formreply.infrastructure.persistence.jpa.entity.converter

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class ListToJsonConverter : AttributeConverter<List<String>?, String?> {
    private val mapper = jacksonObjectMapper()

    override fun convertToDatabaseColumn(attribute: List<String>?): String? =
        attribute?.let { mapper.writeValueAsString(it) }

    override fun convertToEntityAttribute(dbData: String?): List<String>? =
        dbData?.let { mapper.readValue(it) }
}
