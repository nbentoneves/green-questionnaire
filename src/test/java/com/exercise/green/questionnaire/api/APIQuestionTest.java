package com.exercise.green.questionnaire.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class APIQuestionTest {

    @Test
    public void testDeserializerQuestion() throws Exception {

        Path path = Paths.get(getClass().getClassLoader().getResource("question.json").toURI());

        String questionJson = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);

        APIQuestion question = new ObjectMapper().readValue(questionJson, APIQuestion.class);

        assertEquals("How are you?", question.getQuestion());
        assertEquals(2, question.getAnswers().size());
        assertTrue(question.getAnswers().stream().anyMatch("Fine"::equals));
        assertTrue(question.getAnswers().stream().anyMatch("Not Fine"::equals));

    }

    @Test
    public void testEqualsToAvoidCollisions() throws Exception {

        Path path = Paths.get(getClass().getClassLoader().getResource("question.json").toURI());

        String questionJson = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);

        APIQuestion question = new ObjectMapper().readValue(questionJson, APIQuestion.class);
        APIQuestion questionEqual = new ObjectMapper().readValue(questionJson, APIQuestion.class);

        Set<APIQuestion> questions = Sets.newHashSet(question, questionEqual);

        assertEquals(1, questions.size());

    }

    @Test
    public void testHashCode() throws Exception {

        Path pathQuestion = Paths.get(getClass().getClassLoader().getResource("question.json").toURI());
        Path pathQuestionEquals = Paths.get(getClass().getClassLoader().getResource("questionDiff.json").toURI());

        String questionJson = new String(Files.readAllBytes(pathQuestion), StandardCharsets.UTF_8);
        String questionJsonEquals = new String(Files.readAllBytes(pathQuestionEquals), StandardCharsets.UTF_8);

        APIQuestion question = new ObjectMapper().readValue(questionJson, APIQuestion.class);
        APIQuestion questionEquals = new ObjectMapper().readValue(questionJsonEquals, APIQuestion.class);

        assertEquals(Objects.hashCode(question), Objects.hashCode(questionEquals));
    }

    @Test
    public void testToString() throws Exception {

        Path path = Paths.get(getClass().getClassLoader().getResource("question.json").toURI());

        String questionJson = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);

        APIQuestion question = new ObjectMapper().readValue(questionJson, APIQuestion.class);

        assertThat(question.toString(), allOf(
                containsString("questionId="),
                containsString("question="),
                containsString("answers=")));

    }

}
