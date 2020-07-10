package com.exercise.green.questionnaire.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.util.Set;

public final class APIQuestionnaire {

    private final Set<APIQuestion> questions;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public APIQuestionnaire(@JsonProperty("questions") Set<APIQuestion> questions) {
        this.questions = questions;
    }

    public Set<APIQuestion> getQuestions() {
        return questions;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("questions", questions)
                .toString();
    }
}
