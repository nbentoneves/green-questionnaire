package com.exercise.green.questionnaire.api;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import com.exercise.green.questionnaire.api.converter.toAPI.APIQuestionnaireConverter;
import com.exercise.green.questionnaire.api.converter.toDomain.QuestionnaireConverter;
import com.exercise.green.questionnaire.domain.Customer;
import com.exercise.green.questionnaire.domain.Question;
import com.exercise.green.questionnaire.domain.Questionnaire;
import com.exercise.green.questionnaire.domain.QuestionnaireStatus;
import com.exercise.green.questionnaire.logic.QuestionnaireService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static com.google.common.collect.Iterables.getOnlyElement;
import static com.google.common.collect.Sets.newHashSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class QuestionnaireControllerTest {

    @Mock
    private QuestionnaireConverter questionnaireConverter;

    @Mock
    private QuestionnaireService questionnaireService;

    @Mock
    private APIQuestionnaireConverter apiQuestionnaireConverter;

    @Mock
    private Appender<ILoggingEvent> mockAppender;

    @Captor
    private ArgumentCaptor<LoggingEvent> loggingEventCaptor;

    private QuestionnaireController questionnaireController;

    @BeforeEach
    public void setUp() {
        initMocks(this);

        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.addAppender(mockAppender);

        questionnaireController = new QuestionnaireController(apiQuestionnaireConverter,
                questionnaireConverter,
                questionnaireService);
    }

    @Test
    public void testGetQuestionnaire() {

        Question question = new Question(UUID.randomUUID(), "Question",
                "Correct", newHashSet("answer"));

        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setQuestions(newHashSet(question));

        APIQuestion apiQuestion = new APIQuestion(question.getQuestionUUID().toString(), "Question", newHashSet("answer"));
        APIQuestionnaireResponse response = new APIQuestionnaireResponse(null, null, newHashSet(apiQuestion));

        when(questionnaireService.getQuestionnaire(any())).thenReturn(questionnaire);
        when(apiQuestionnaireConverter.convert(eq(questionnaire))).thenReturn(response);

        APIQuestionnaireResponse result = questionnaireController.getQuestionnaire();

        assertNotNull(result);
        assertNull(response.getCustomerID());
        assertNull(response.getStatus());
        assertEquals(apiQuestion, getOnlyElement(result.getQuestions()));

    }

    @Test
    public void testAnswerQuestionnaire() {

        APIQuestion apiQuestion = new APIQuestion("10f67bbc-91aa-4e6f-be33-964a98973b95", "Question", newHashSet("answer"));
        APIQuestionnaire apiQuestionnaire = new APIQuestionnaire(newHashSet(apiQuestion));

        Question question = new Question(UUID.fromString(apiQuestion.getQuestionId()), "Question",
                "Correct", newHashSet("answer"));

        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setQuestions(newHashSet(question));

        Customer customer = new Customer(UUID.randomUUID());
        Questionnaire result = new Questionnaire();
        result.setCustomer(customer);
        result.setQuestionnaireStatus(QuestionnaireStatus.SUITABLE);

        APIQuestionnaireResponse response = new APIQuestionnaireResponse(
                "SUITABLE",
                customer.getCustomerId().toString(),
                null);

        when(questionnaireConverter.convert(eq(apiQuestionnaire))).thenReturn(questionnaire);
        when(questionnaireService.answerQuestionnaire(eq(questionnaire))).thenReturn(result);
        when(apiQuestionnaireConverter.convert(eq(result))).thenReturn(response);

        APIQuestionnaireResponse apiResponse = questionnaireController.answerQuestionnaire(apiQuestionnaire);

        assertNotNull(apiResponse);
        assertEquals(customer.getCustomerId().toString(), apiResponse.getCustomerID());
        assertEquals("SUITABLE", apiResponse.getStatus());
        assertNull(apiResponse.getQuestions());

    }

    @Test
    public void testAnswerQuestionnaireLogging() {

        APIQuestion apiQuestion1 = new APIQuestion("10f67bbc-91aa-4e6f-be33-964a98973b95", "Question", newHashSet("answer"));
        APIQuestion apiQuestion2 = new APIQuestion("818efede-71f2-4056-9cdd-485abd9a3bc3", "Question", newHashSet("answer"));
        APIQuestionnaire apiQuestionnaire = new APIQuestionnaire(newHashSet(apiQuestion1));

        Question question = new Question(UUID.fromString(apiQuestion1.getQuestionId()), "Question - 1",
                "Correct", newHashSet("answer - 1"));

        Question question2 = new Question(UUID.fromString(apiQuestion2.getQuestionId()), "Question - 2",
                "Correct", newHashSet("answer - 2"));

        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setQuestions(newHashSet(question, question2));

        Customer customer = new Customer(UUID.randomUUID());
        Questionnaire result = new Questionnaire();
        result.setCustomer(customer);
        result.setQuestionnaireStatus(QuestionnaireStatus.SUITABLE);

        APIQuestionnaireResponse response = new APIQuestionnaireResponse(
                "SUITABLE",
                customer.getCustomerId().toString(),
                null);

        when(questionnaireConverter.convert(eq(apiQuestionnaire))).thenReturn(questionnaire);
        when(questionnaireService.answerQuestionnaire(eq(questionnaire))).thenReturn(result);
        when(apiQuestionnaireConverter.convert(eq(result))).thenReturn(response);

        APIQuestionnaireResponse apiResponse = questionnaireController.answerQuestionnaire(apiQuestionnaire);

        verify(mockAppender, times(2)).doAppend(loggingEventCaptor.capture());

        List<LoggingEvent> loggingEvents = loggingEventCaptor.getAllValues();

        assertThat(loggingEvents.get(0).getFormattedMessage(), allOf(
                containsString("question=Question - 1"),
                containsString("answers=[answer - 1]")));

        assertThat(loggingEvents.get(1).getFormattedMessage(), allOf(
                containsString("question=Question - 2"),
                containsString("answers=[answer - 2]")));

        assertNotNull(apiResponse);
        assertEquals(customer.getCustomerId().toString(), apiResponse.getCustomerID());
        assertEquals("SUITABLE", apiResponse.getStatus());
        assertNull(apiResponse.getQuestions());

    }

    @Test
    public void testAnswerEmptyQuestionnaire() {

        assertThrows(ResponseStatusException.class, () -> {
            APIQuestionnaire apiQuestionnaire = new APIQuestionnaire(newHashSet());

            Questionnaire questionnaire = new Questionnaire();
            questionnaire.setQuestions(newHashSet());

            when(questionnaireConverter.convert(eq(apiQuestionnaire))).thenReturn(questionnaire);

            questionnaireController.answerQuestionnaire(apiQuestionnaire);
        });

    }

    @Test
    public void testAnswerQuestionnaireButSomethingWrongHappened() {

        assertThrows(ResponseStatusException.class, () -> {
            APIQuestionnaire apiQuestionnaire = new APIQuestionnaire(newHashSet());
            APIQuestion apiQuestion = new APIQuestion("10f67bbc-91aa-4e6f-be33-964a98973b95", "Question", newHashSet("answer"));

            Question question = new Question(UUID.fromString(apiQuestion.getQuestionId()), "Question",
                    "Correct", newHashSet("answer"));

            Questionnaire questionnaire = new Questionnaire();
            questionnaire.setQuestions(newHashSet(question));

            when(questionnaireConverter.convert(eq(apiQuestionnaire))).thenReturn(questionnaire);
            when(questionnaireService.answerQuestionnaire(any())).thenThrow(new RuntimeException("Something happened!"));

            questionnaireController.answerQuestionnaire(apiQuestionnaire);
        });

    }

}
