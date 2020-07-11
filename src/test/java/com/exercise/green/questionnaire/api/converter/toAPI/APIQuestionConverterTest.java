package com.exercise.green.questionnaire.api.converter.toAPI;

import com.exercise.green.questionnaire.api.APIQuestion;
import com.exercise.green.questionnaire.domain.Question;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.google.common.collect.Iterables.getOnlyElement;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class APIQuestionConverterTest {

    @Test
    public void testConverter() {

        Question question = new Question(UUID.randomUUID(), "Question", "Correct", Sets.newHashSet("answer"));

        APIQuestionConverter converter = new APIQuestionConverter();

        APIQuestion apiQuestion = converter.convert(question);

        assertNotNull(apiQuestion);
        assertNotNull(apiQuestion.getQuestionId());
        assertEquals("Question", apiQuestion.getQuestion());
        assertEquals("answer", getOnlyElement(apiQuestion.getAnswers()));

    }

}
