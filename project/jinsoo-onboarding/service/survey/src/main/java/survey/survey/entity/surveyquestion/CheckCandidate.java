package survey.survey.entity.surveyquestion;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckCandidate that = (CheckCandidate) o;
        return checkCandidateIndex == that.checkCandidateIndex &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(checkCandidateIndex, name);
    }

}

