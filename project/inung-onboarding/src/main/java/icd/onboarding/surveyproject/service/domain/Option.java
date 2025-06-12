package icd.onboarding.surveyproject.service.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
<<<<<<< HEAD
<<<<<<< HEAD
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
=======
>>>>>>> f24b238 (feat: Option 도메인 단위 테스트 성공)
=======
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
>>>>>>> 2f6452b (feat: Question 테스트 케이스 작성 및 일부 구현)

import java.util.UUID;

@Getter
@Setter
@Builder
public class Option {
<<<<<<< HEAD
<<<<<<< HEAD
	private @NotNull UUID questionId;
	private @Nullable UUID id;
	private @NotNull String text;
=======
	private UUID questionId;
	private UUID id;
	private String text;
>>>>>>> f24b238 (feat: Option 도메인 단위 테스트 성공)
=======
	private @NotNull UUID questionId;
	private @Nullable UUID id;
	private @NotNull String text;
>>>>>>> 2f6452b (feat: Question 테스트 케이스 작성 및 일부 구현)
	private Integer sortOrder;

	public static Option create (UUID questionId, String text, Integer sortOrder) {
		if (questionId == null) throw new IllegalArgumentException("질문의 Id는 비어 있을 수 없습니다.");
		if (text == null || text.isBlank()) throw new IllegalArgumentException("옵션 텍스트는 필수입니다.");
		if (sortOrder < 0) throw new IllegalArgumentException("정렬 순서는 음수일 수 없습니다.");

		return Option.builder()
					 .questionId(questionId)
					 .text(text)
					 .sortOrder(sortOrder)
					 .id(UUID.randomUUID())
					 .build();
	}
}
