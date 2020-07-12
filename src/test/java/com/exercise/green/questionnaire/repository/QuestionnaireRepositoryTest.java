package com.exercise.green.questionnaire.repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionnaireRepositoryTest {

    @Test
    public void testLoadInvalidFile() {

        assertThrows(IllegalArgumentException.class, () -> new QuestionnaireRepository(null));


        assertThrows(IllegalArgumentException.class, () -> new QuestionnaireRepository(""));
        assertThrows(RuntimeException.class, () -> new QuestionnaireRepository("classpath:invalid.yml"));

        assertThrows(IllegalStateException.class, () -> new QuestionnaireRepository("classpath:questionsLessFour.yml"));

    }

    @Test
    public void testLoadValidFile() {

        QuestionnaireRepository questionnaireRepository = new QuestionnaireRepository("classpath:questions.yml");

        assertNotNull(questionnaireRepository);
        assertEquals(8, questionnaireRepository.getQuestions().size());
        assertTrue(questionnaireRepository.getQuestions().containsKey(0));
        assertTrue(questionnaireRepository.getQuestions().containsKey(1));

        questionnaireRepository.getQuestions().forEach((key, value) ->
                assertTrue(questionnaireRepository.getQuestion(value.getQuestionUUID().toString()).isPresent()));

    }

}
