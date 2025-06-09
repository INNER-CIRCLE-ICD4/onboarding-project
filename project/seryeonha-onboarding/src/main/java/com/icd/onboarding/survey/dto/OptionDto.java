package com.icd.onboarding.survey.dto;

import com.icd.onboarding.survey.domain.Option;
import lombok.*;

public class OptionDto {

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Create {
        private String content;

        public Option toEntity() {
            return Option.builder()
                    .content(content)
                    .build();
        }
    }

    @NoArgsConstructor
    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class Read {
        private Long id;
        private String content;

        public static Read fromEntity(Option option) {
            return Read.builder()
                    .id(option.getId())
                    .content(option.getContent())
                    .build();
        }
    }
}
