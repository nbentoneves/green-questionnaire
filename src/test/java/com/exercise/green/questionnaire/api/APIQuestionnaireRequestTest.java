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

public class APIQuestionnaireRequestTest {

    @Test
    public void testDeserializerQuestionnaireRequest() throws Exception {

        Path path = Paths.get(getClass().getClassLoader().getResource("questionnaireRequest.json").toURI());

        String json = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);

        APIQuestionnaire questionnaire = new ObjectMapper().readValue(json, APIQuestionnaire.class);

        assertEquals(2, questionnaire.getQuestions().size());

    }

    @Test
    public void testToString() throws Exception {

        Path path = Paths.get(getClass().getClassLoader().getResource("questionnaireRequest.json").toURI());

        String json = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);

        APIQuestionnaire questionnaire = new ObjectMapper().readValue(json, APIQuestionnaire.class);

        assertThat(questionnaire.toString(), allOf(
                containsString("questions=")));

    }

}
