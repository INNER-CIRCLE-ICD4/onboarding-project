package survey.survey.entity.surveyquestion;

import java.util.List;

public enum InputType {
    SHORT_ANSWER {
        @Override
        public void validateQuestion(SurveyQuestion question, List<CheckCandidate> candidates) {
            if (candidates != null && !candidates.isEmpty()) {
                throw new IllegalArgumentException("단답형 질문에는 선택지를 추가할 수 없습니다.");
            }
        }

        @Override
        public boolean requiresCandidates() {
            return false;
        }
    },

    LONG_ANSWER {
        @Override
        public void validateQuestion(SurveyQuestion question, List<CheckCandidate> candidates) {
            if (candidates != null && !candidates.isEmpty()) {
                throw new IllegalArgumentException("장문형 질문에는 선택지를 추가할 수 없습니다.");
            }
        }

        @Override
        public boolean requiresCandidates() {
            return false;
        }
    },

    SINGLE_CHOICE {
        @Override
        public void validateQuestion(SurveyQuestion question, List<CheckCandidate> candidates) {
            if (candidates == null || candidates.isEmpty()) {
                throw new IllegalArgumentException("단일 선택형 질문에는 최소 하나 이상의 선택지가 필요합니다.");
            }
        }

        @Override
        public boolean requiresCandidates() {
            return true;
        }
    },

    MULTIPLE_CHOICE {
        @Override
        public void validateQuestion(SurveyQuestion question, List<CheckCandidate> candidates) {
            if (candidates == null || candidates.isEmpty()) {
                throw new IllegalArgumentException("다중 선택형 질문에는 최소 하나 이상의 선택지가 필요합니다.");
            }
        }

        @Override
        public boolean requiresCandidates() {
            return true;
        }
    };

    /**
     * 질문 유형에 따른 유효성 검증을 수행합니다.
     * @param question 질문 객체
     * @param candidates 추가하려는 선택지 목록
     * @throws IllegalArgumentException 유효성 검증 실패 시
     */
    public abstract void validateQuestion(SurveyQuestion question, List<CheckCandidate> candidates);

    /**
     * 해당 질문 유형이 선택지를 필요로 하는지 여부를 반환합니다.
     * @return 선택지 필요 여부
     */
    public abstract boolean requiresCandidates();

}
