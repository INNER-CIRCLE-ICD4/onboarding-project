package survey.survey.entity.surveyquestion;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CheckCandidate {
    private int checkCandidateIndex;
    private String name;

    private CheckCandidate(int index, String name) {
        this.checkCandidateIndex = index;
        this.name = name;
    }
    public static CheckCandidate of(int index, String name) {
        return new CheckCandidate(index, name);
    }
}

