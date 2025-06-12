package com.onboarding.survey.domain.model;

import java.util.ArrayList;
import java.util.List;

public class Questions {
    private static final String MAX_EXCEPTION_MESSAGE = "설문 받을 항목의 개수가 많습니다.";

    public static final int MAX_SIZE = 10;

    private List<Question> questions;

    public Questions() {
        questions = new ArrayList<>();
    }

    public void add(Question question) {
        questions.add(question);
    }

    public void add(String question, String description) {
        if(questions.size() + 1 > MAX_SIZE) {
            throw new IndexOutOfBoundsException(MAX_EXCEPTION_MESSAGE);
        }

        questions.add(Question.of(question, description));
    }

    public void add(String question, String description, QuestionType type) {
        if(questions.size() + 1 > MAX_SIZE) {
            throw new IndexOutOfBoundsException(MAX_EXCEPTION_MESSAGE);
        }

        questions.add(Question.of(question, description, type));
    }
}
