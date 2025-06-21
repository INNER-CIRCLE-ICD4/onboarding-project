package com.onboarding.model.survey;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Setter
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

    public void setOptionList(List<Option> options) {
        this.options = options;
    }

    public List<Option> getOptionList() {
        return Collections.unmodifiableList(options);
    }
}
