package com.exercise.green.questionnaire.api.converter.toAPI;

import com.exercise.green.questionnaire.api.APIQuestion;
import com.exercise.green.questionnaire.api.APIQuestionnaireResponse;
import com.exercise.green.questionnaire.domain.Customer;
import com.exercise.green.questionnaire.domain.Question;
import com.exercise.green.questionnaire.domain.Questionnaire;
import com.exercise.green.questionnaire.domain.QuestionnaireStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.UUID;

import static com.google.common.collect.Iterables.getOnlyElement;
import static com.google.common.collect.Sets.newHashSet;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class APIQuestionnaireConverterTest {

    @Mock
    private APIQuestionConverter apiQuestionConverter;

    @BeforeEach
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void testConverter() {

        UUID uuid = UUID.randomUUID();

        Question question = new Question(uuid, "Question", "Correct", newHashSet("answer"));
        APIQuestion apiQuestion = new APIQuestion(uuid.toString(), "Question", newHashSet("answer"));

        when(apiQuestionConverter.convert(eq(question))).thenReturn(apiQuestion);

        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setCustomer(new Customer(UUID.randomUUID()));
        questionnaire.setQuestionnaireStatus(QuestionnaireStatus.SUITABLE);
        questionnaire.setQuestions(newHashSet(question));

        APIQuestionnaireConverter converter = new APIQuestionnaireConverter(apiQuestionConverter);

        APIQuestionnaireResponse apiQuestionnaire = converter.convert(questionnaire);

        assertNotNull(apiQuestionnaire);
        assertNotNull(apiQuestionnaire.getCustomerID());
        assertEquals("SUITABLE", apiQuestionnaire.getStatus());
        assertEquals(apiQuestion, getOnlyElement(apiQuestionnaire.getQuestions()));

    }

    @Test
    public void testConverterWithNullValues() {

        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setCustomer(null);
        questionnaire.setQuestionnaireStatus(null);
        questionnaire.setQuestions(newHashSet());

        APIQuestionnaireConverter converter = new APIQuestionnaireConverter(apiQuestionConverter);

        APIQuestionnaireResponse apiQuestionnaire = converter.convert(questionnaire);

        assertNotNull(apiQuestionnaire);
        assertNull(apiQuestionnaire.getCustomerID());
        assertNull(apiQuestionnaire.getStatus());
        assertNull(apiQuestionnaire.getQuestions());

    }

}
