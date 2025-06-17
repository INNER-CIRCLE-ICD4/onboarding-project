package fc.icd.jaehyeononboarding.answer.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import fc.icd.jaehyeononboarding.answer.model.dto.AnswerWithQuestionDTO;
import fc.icd.jaehyeononboarding.common.model.InputType;
import fc.icd.jaehyeononboarding.survey.model.dto.QuestionDTO;

import java.io.IOException;
import java.util.List;

public class AnswerWithQuestionDeserializer extends JsonDeserializer<AnswerWithQuestionDTO<?>> {
    @Override
    public AnswerWithQuestionDTO<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode root = p.readValueAsTree();

        InputType inputType = InputType.from(root.get("question").get("input_type").asText());

        JsonNode contentNode = root.get("content");

        switch (inputType) {
            case TEXT:
            case LONG_TEXT:
            case RADIO:
                AnswerWithQuestionDTO<String> singleAnswerDTO = new AnswerWithQuestionDTO<>();
                singleAnswerDTO.setQuestion(mapper.treeToValue(root, QuestionDTO.class));
                String content = contentNode.isNull() ? null : contentNode.asText();
                singleAnswerDTO.setContent(content);
                return singleAnswerDTO;
            case CHECKBOX:
                AnswerWithQuestionDTO<List<String>> multipleAnswerDTO = new AnswerWithQuestionDTO<>();
                List<String> contents = mapper.convertValue(contentNode, mapper.getTypeFactory().constructCollectionType(List.class, String.class));
                multipleAnswerDTO.setContent(contents);
                return multipleAnswerDTO;
            default:
                throw JsonMappingException.from(p, "Unsupported input type: " + inputType);
        }

    }
}
