package icd.onboarding.surveyproject.common;

public class NotImplementedTestException extends RuntimeException {
	public NotImplementedTestException () {
		super("아직 테스트 코드가 구현되지 않았습니다.");
	}
}
