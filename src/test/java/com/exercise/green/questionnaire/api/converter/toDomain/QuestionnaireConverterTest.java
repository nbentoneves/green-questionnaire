package com.exercise.green.questionnaire.api.converter.toDomain;

import com.exercise.green.questionnaire.api.APIQuestion;
import com.exercise.green.questionnaire.api.APIQuestionnaire;
import com.exercise.green.questionnaire.domain.Question;
import com.exercise.green.questionnaire.domain.Questionnaire;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.UUID;

import static com.google.common.collect.Iterables.getOnlyElement;
import static com.google.common.collect.Sets.newHashSet;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class QuestionnaireConverterTest {

    @Mock
    private QuestionConverter questionConverter;

    @BeforeEach
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void testConverter() {

        Question question = new Question(UUID.fromString("10f67bbc-91aa-4e6f-be33-964a98973b95"), "Question", "Correct", newHashSet("answer"));
        APIQuestion apiQuestion = new APIQuestion("10f67bbc-91aa-4e6f-be33-964a98973b95", "Question", newHashSet("answer"));

        when(questionConverter.convert(eq(apiQuestion))).thenReturn(question);

        QuestionnaireConverter questionnaireConverter = new QuestionnaireConverter(questionConverter);

        APIQuestionnaire apiQuestionnaire = new APIQuestionnaire(newHashSet(apiQuestion));

        Questionnaire questionnaire = questionnaireConverter.convert(apiQuestionnaire);

        assertNotNull(questionnaire);
        assertFalse(questionnaire.getCustomer().isPresent());
        assertFalse(questionnaire.getQuestionnaireStatus().isPresent());
        assertEquals(question, getOnlyElement(questionnaire.getQuestions()));

    }

    @Test
    public void testConverterEmptyList() {

        QuestionnaireConverter questionnaireConverter = new QuestionnaireConverter(questionConverter);

        APIQuestionnaire apiQuestionnaire = new APIQuestionnaire(newHashSet());
        Questionnaire questionnaire = questionnaireConverter.convert(apiQuestionnaire);

        verifyNoInteractions(questionConverter);

        assertNotNull(questionnaire);
        assertNull(questionnaire.getQuestions());

    }

}
