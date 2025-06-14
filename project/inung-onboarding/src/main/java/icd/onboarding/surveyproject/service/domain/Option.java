package icd.onboarding.surveyproject.service.domain;

import icd.onboarding.surveyproject.service.exception.InvalidOptionInfoException;
import icd.onboarding.surveyproject.service.exception.NotNegativeNumberException;
import io.micrometer.common.util.StringUtils;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Option {
	private Question question;
	private final UUID id;
	private final String text;
	private final Integer sortOrder;

	public Option (String text, Integer sortOrder) {
		this.id = UUID.randomUUID();
		this.text = text;
		this.sortOrder = sortOrder;
	}

	public static Option create (String text, Integer sortOrder) {
		if (StringUtils.isBlank(text)) throw new InvalidOptionInfoException();
		if (sortOrder < 0) throw new NotNegativeNumberException();

		return new Option(text, sortOrder);
	}

	protected void setQuestion (Question question) {
		this.question = question;
	}
}
