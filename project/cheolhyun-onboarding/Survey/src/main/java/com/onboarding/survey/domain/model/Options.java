package com.onboarding.survey.domain.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@EqualsAndHashCode
public class Options {
    private List<Option> options;

    public Options() {
        options = new ArrayList<>();
    }

    public Options(String text) {
        this.options = new ArrayList<>() {
            {
                add(Option.of(text));
            }
        };
    }

    public Options(List<String> textList) {
        this.options = textList.stream()
                .map(Option::of)
                .toList();
    }

    public void add(String text) {
        options.add(new Option(text));
    }
}
