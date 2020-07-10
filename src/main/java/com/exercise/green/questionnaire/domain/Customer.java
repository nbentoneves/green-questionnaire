package com.exercise.green.questionnaire.domain;

import com.google.common.base.MoreObjects;

import java.util.Objects;
import java.util.UUID;

public final class Customer {

    private final UUID customerId;

    public Customer(UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(customerId, customer.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("customerId", customerId)
                .toString();
    }
}
