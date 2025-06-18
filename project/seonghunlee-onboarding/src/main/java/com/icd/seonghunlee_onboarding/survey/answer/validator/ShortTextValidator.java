package com.icd.seonghunlee_onboarding.survey.answer.validator;

import com.icd.seonghunlee_onboarding.survey.answer.AnswerValidator;

import java.util.regex.Pattern;

public class ShortTextValidator implements AnswerValidator<String> {

    private static final int MAX_LENGTH = 10;

    // 허용 문자: 한글, 영문 대소문자, 숫자, 공백(띄어쓰기 1칸만 허용)
    private static final Pattern ALLOWED_PATTERN = Pattern.compile("^[가-힣a-zA-Z0-9 ]+$");

    @Override
    public boolean isValid(String input) {

        String trimmed = input.trim();

        // 길이 제한
        if (trimmed.length() > MAX_LENGTH) return false;

        // 연속된 공백 2칸 이상 여부
        if (hasMoreThanOneSpace(trimmed)) return false;

        // 특수문자 검사: 허용문자 이외 문자 있으면 false
        if (!ALLOWED_PATTERN.matcher(trimmed).matches()) return false;

        return true;
    }

    private boolean hasMoreThanOneSpace(String input) {
        return input.chars().filter(c -> c == ' ').count() > 1;
    }



}
