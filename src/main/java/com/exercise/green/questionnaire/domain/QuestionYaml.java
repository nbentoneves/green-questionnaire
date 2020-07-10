package com.exercise.green.questionnaire.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public final class QuestionYaml {

    private final UUID questionUUID;

    private final String question;

    private final String correctAnswer;

    private final Set<String> answers;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public QuestionYaml(
            @JsonProperty("question") String question,
            @JsonProperty("correctAnswer") String correctAnswer,
            @JsonProperty("answers") Set<String> answers) {
        this.questionUUID = UUID.randomUUID();
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.answers = answers;
    }

    public UUID getQuestionUUID() {
        return questionUUID;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
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

        QuestionYaml question = (QuestionYaml) o;
        return Objects.equals(questionUUID, question.questionUUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionUUID);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("questionUUID", questionUUID)
                .add("question", question)
                .add("correctAnswer", correctAnswer)
                .add("answers", answers)
                .toString();
    }
}
