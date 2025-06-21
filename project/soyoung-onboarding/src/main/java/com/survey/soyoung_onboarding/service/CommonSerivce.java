package com.survey.soyoung_onboarding.service;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class CommonSerivce {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
}
