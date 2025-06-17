package com.onboarding.model.survey;

import com.onboarding.model.QuestionType;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Questions {
    private static final String MAX_EXCEPTION_MESSAGE = "설문 받을 항목의 개수가 많습니다.";
    private static final String NO_SUCH_QUESTION = "항목을 찾을 수 없습니다.";

    public static final int MAX_SIZE = 10;

    private List<Question> questions;

    public Questions() {
        questions = new ArrayList<>();
    }

    public void add(Question question) {
        questions.add(question);
    }

    public void add(String id, String question, String description) {
        if(questions.size() + 1 > MAX_SIZE) {
            throw new IndexOutOfBoundsException(MAX_EXCEPTION_MESSAGE);
        }

        questions.add(Question.of(id, question, description));
    }

    public void add(String id, String question, String description, QuestionType type) {
        if(questions.size() + 1 > MAX_SIZE) {
            throw new IndexOutOfBoundsException(MAX_EXCEPTION_MESSAGE);
        }

        questions.add(Question.of(id, question, description, type));
    }

    public void update(String id, Question question) {
        Question result = getById(id);

        result.markDeleted();

        questions.add(question);
    }

    public Question getById(String id) {
        return questions.stream()
                .filter(item -> item.isEqualById(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_QUESTION));
    }
}
