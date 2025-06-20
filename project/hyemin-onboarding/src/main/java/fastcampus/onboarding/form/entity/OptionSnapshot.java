package fastcampus.onboarding.form.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Embeddable
@Getter
public class OptionSnapshot {
    private Long optionSeq;
    private String optionContentSnapshot;
}