package com.exercise.green.questionnaire.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.Set;

import static com.google.common.base.MoreObjects.toStringHelper;

public final class APIQuestion {

    private final String questionId;

    private final String question;

    private final Set<String> answers;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public APIQuestion(@JsonProperty("questionId") String questionId,
                       @JsonProperty("question") String question,
                       @JsonProperty("answers") Set<String> answers) {
        this.questionId = questionId;
        this.question = question;
        this.answers = answers;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getQuestion() {
        return question;
    }

    public Set<String> getAnswers() {
        return answers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        APIQuestion object = (APIQuestion) o;
        return Objects.equals(questionId, object.questionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionId);
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("questionId", questionId)
                .add("question", question)
                .add("answers", answers)
                .toString();
    }
}
