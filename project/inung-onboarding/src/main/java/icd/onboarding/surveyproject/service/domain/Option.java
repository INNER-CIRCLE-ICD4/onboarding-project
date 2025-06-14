package icd.onboarding.surveyproject.service.domain;

import io.micrometer.common.util.StringUtils;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class Option {
	private Question question;
	private UUID id;
	private String text;
	private Integer sortOrder;

	public static Option create (String text, Integer sortOrder) {
		if (StringUtils.isBlank(text)) throw new IllegalArgumentException("옵션 텍스트는 필수입니다.");
		if (sortOrder < 0) throw new IllegalArgumentException("정렬 순서는 음수일 수 없습니다.");

		return Option.builder()
					 .text(text)
					 .sortOrder(sortOrder)
					 .id(UUID.randomUUID())
					 .build();
	}

	protected void setQuestion (Question question) {
		this.question = question;
	}
}
