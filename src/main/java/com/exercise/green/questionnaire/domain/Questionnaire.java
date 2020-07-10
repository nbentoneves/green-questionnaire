package com.exercise.green.questionnaire.domain;

import com.google.common.base.MoreObjects;

import java.util.Optional;
import java.util.Set;

public class Questionnaire {

    private Set<Question> questions;

    private QuestionnaireStatus questionnaireStatus;

    private Customer customer;

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public Optional<QuestionnaireStatus> getQuestionnaireStatus() {
        return Optional.ofNullable(questionnaireStatus);
    }

    public Optional<Customer> getCustomer() {
        return Optional.ofNullable(customer);
    }

    public void setQuestionnaireStatus(QuestionnaireStatus questionnaireStatus) {
        this.questionnaireStatus = questionnaireStatus;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("questions", questions)
                .add("questionnaireStatus", questionnaireStatus)
                .add("customer", customer)
                .toString();
    }
}
