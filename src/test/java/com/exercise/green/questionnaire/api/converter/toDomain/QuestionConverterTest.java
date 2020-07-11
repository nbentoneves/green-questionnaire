package com.exercise.green.questionnaire.api.converter.toDomain;

import com.exercise.green.questionnaire.api.APIQuestion;
import com.exercise.green.questionnaire.domain.Question;
import org.junit.jupiter.api.Test;

import static com.google.common.collect.Iterables.getOnlyElement;
import static com.google.common.collect.Sets.newHashSet;
import static org.junit.jupiter.api.Assertions.*;

public class QuestionConverterTest {

    @Test
    public void testConverter() {

        QuestionConverter converter = new QuestionConverter();

        APIQuestion apiQuestion = new APIQuestion("10f67bbc-91aa-4e6f-be33-964a98973b95", "Question", newHashSet("answer"));

        Question question = converter.convert(apiQuestion);

        assertNotNull(question);
        assertEquals("10f67bbc-91aa-4e6f-be33-964a98973b95", question.getQuestionUUID().toString());
        assertEquals("Question", question.getQuestion());
        assertEquals("answer", getOnlyElement(question.getAnswers()));
        assertNull(question.getCorrectAnswer());

    }

}
