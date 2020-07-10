package com.exercise.green.questionnaire.domain;

import com.google.common.base.MoreObjects;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public final class Question {

    private final UUID questionUUID;

    private final String question;

    private final String correctAnswer;

    private final Set<String> answers;

    public Question(UUID questionUUID, String question, String correctAnswer, Set<String> answers) {
        this.questionUUID = questionUUID;
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

        Question question = (Question) o;
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
