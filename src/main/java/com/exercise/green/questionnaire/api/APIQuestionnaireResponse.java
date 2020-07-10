package com.exercise.green.questionnaire.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class APIQuestionnaireResponse {

    private final String status;

    private final String customerID;

    private final Set<APIQuestion> questions;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public APIQuestionnaireResponse(@JsonProperty("status") String status,
                                    @JsonProperty("customerID") String customerID,
                                    @JsonProperty("questions") Set<APIQuestion> questions) {
        this.status = status;
        this.customerID = customerID;
        this.questions = questions;

    }

    public String getStatus() {
        return status;
    }

    public String getCustomerID() {
        return customerID;
    }

    public Set<APIQuestion> getQuestions() {
        return questions;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("status", status)
                .add("customerID", customerID)
                .add("questions", questions)
                .toString();
    }
}
