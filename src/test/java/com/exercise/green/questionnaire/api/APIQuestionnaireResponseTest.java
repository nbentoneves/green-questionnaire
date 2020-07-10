package com.exercise.green.questionnaire.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class APIQuestionnaireResponseTest {

    @Test
    public void testDeserializerQuestionnaireResponse() throws Exception {

        Path path = Paths.get(getClass().getClassLoader().getResource("questionnaireResponse.json").toURI());

        String json = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);

        APIQuestionnaireResponse questionnaireResponse = new ObjectMapper().readValue(json, APIQuestionnaireResponse.class);

        assertEquals("NOT_SUITABLE", questionnaireResponse.getStatus());
        assertEquals("99d640c9-f523-4a3b-9819-66c813b4a89a", questionnaireResponse.getCustomerID());

    }

    @Test
    public void testToString() throws Exception {

        Path path = Paths.get(getClass().getClassLoader().getResource("questionnaireResponse.json").toURI());

        String json = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);

        APIQuestionnaireResponse questionnaireResponse = new ObjectMapper().readValue(json, APIQuestionnaireResponse.class);

        assertThat(questionnaireResponse.toString(), allOf(
                containsString("status="),
                containsString("customerID=")));

    }

}
