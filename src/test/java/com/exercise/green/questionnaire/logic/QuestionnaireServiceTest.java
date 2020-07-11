package com.exercise.green.questionnaire.logic;

import com.exercise.green.questionnaire.domain.Question;
import com.exercise.green.questionnaire.domain.Questionnaire;
import com.exercise.green.questionnaire.domain.QuestionnaireStatus;
import com.exercise.green.questionnaire.repository.QuestionnaireRepository;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;

import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.collect.Sets.newHashSet;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class QuestionnaireServiceTest {

    @Mock
    private QuestionnaireRepository questionnaireRepository;

    @Mock
    private Random random;

    private QuestionnaireService questionnaireService;

    @BeforeEach
    public void setUp() {
        initMocks(this);

        this.questionnaireService = new QuestionnaireService(questionnaireRepository);
    }

    @Test
    public void testGetQuestionnaire() {

        Map<Integer, Question> questions = buildQuestionnaire();

        AtomicInteger controlIterations = new AtomicInteger(0);

        when(questionnaireRepository.getQuestions()).thenReturn(questions);
        //Get the first 4 questions
        when(random.nextInt(eq(questions.size()))).thenAnswer(
                (Answer<Integer>) invocationOnMock -> controlIterations.getAndIncrement());

        Questionnaire questionnaire = this.questionnaireService.getQuestionnaire(random);

        verify(random, times(4)).nextInt(eq(questions.size()));

        assertNotNull(questionnaire);
        assertEquals(4, questionnaire.getQuestions().size());
        assertTrue(questionnaire.getQuestions().stream().anyMatch(q -> "q1".equals(q.getQuestion())));
        assertTrue(questionnaire.getQuestions().stream().anyMatch(q -> "q2".equals(q.getQuestion())));
        assertTrue(questionnaire.getQuestions().stream().anyMatch(q -> "q3".equals(q.getQuestion())));
        assertTrue(questionnaire.getQuestions().stream().anyMatch(q -> "q4".equals(q.getQuestion())));

    }

    @Test
    public void testGetQuestionnaireButAvoidRepeatedQuestions() {

        Map<Integer, Question> questions = buildQuestionnaire();

        AtomicInteger controlIterations = new AtomicInteger(1);

        when(questionnaireRepository.getQuestions()).thenReturn(questions);
        when(random.nextInt(eq(questions.size()))).thenAnswer(
                (Answer<Integer>) invocationOnMock -> {

                    int value = controlIterations.getAndIncrement();

                    if (value == 2) return 0;
                    if (value == 3) return 3;
                    if (value == 4) return 5;
                    if (value == 5) return 2;

                    return 5;
                });

        Questionnaire questionnaire = this.questionnaireService.getQuestionnaire(random);

        verify(random, times(5)).nextInt(eq(questions.size()));

        assertNotNull(questionnaire);
        assertEquals(4, questionnaire.getQuestions().size());
        assertTrue(questionnaire.getQuestions().stream().anyMatch(q -> "q1".equals(q.getQuestion())));
        assertTrue(questionnaire.getQuestions().stream().anyMatch(q -> "q6".equals(q.getQuestion())));
        assertTrue(questionnaire.getQuestions().stream().anyMatch(q -> "q4".equals(q.getQuestion())));
        assertTrue(questionnaire.getQuestions().stream().anyMatch(q -> "q3".equals(q.getQuestion())));

    }

    @Test
    public void testAnswerQuestionnaireButHasMoreThanOneAnswerPerQuestion() {

        assertThrows(RuntimeException.class, () -> {
            Question question = new Question(UUID.randomUUID(), "q1", "c1", newHashSet("a1", "a2"));
            Questionnaire questionnaire = new Questionnaire();
            questionnaire.setQuestions(newHashSet(question));

            this.questionnaireService.answerQuestionnaire(questionnaire);
        });

    }

    @Test
    public void testAnswerQuestionnaireWithInvalidIdentifier() {

        assertThrows(RuntimeException.class, () -> {

            UUID uuidQ1 = UUID.randomUUID();
            UUID uuidQ2 = UUID.randomUUID();
            UUID uuidQ3 = UUID.randomUUID();
            UUID uuidQ4 = UUID.randomUUID();

            Question question1User = new Question(uuidQ1, "q1", null, newHashSet("c1"));
            Question question2User = new Question(uuidQ2, "q2", null, newHashSet("d2"));
            Question question3User = new Question(uuidQ3, "q3", null, newHashSet("e1"));
            Question question4User = new Question(uuidQ4, "q4", null, newHashSet("p2"));
            Questionnaire questionnaire = new Questionnaire();
            questionnaire.setQuestions(newHashSet(question1User, question2User, question3User, question4User));

            when(questionnaireRepository.getQuestion(uuidQ1.toString())).thenReturn(Optional.empty());

            this.questionnaireService.answerQuestionnaire(questionnaire);

        });

    }

    @Test
    public void testAnswerQuestionnaireButIsNotValid() {

        Question question = new Question(UUID.randomUUID(), "q1", "c1", newHashSet("a1"));
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setQuestions(newHashSet(question));

        Questionnaire result = this.questionnaireService.answerQuestionnaire(questionnaire);

        assertNotNull(result);
        assertNotNull(result.getCustomer());
        assertTrue(result.getQuestionnaireStatus().isPresent());
        assertEquals(QuestionnaireStatus.NOT_COMPLETE, result.getQuestionnaireStatus().get());

    }

    @Test
    public void testAnswerQuestionnaireSuitable() {

        UUID uuidQ1 = UUID.randomUUID();
        UUID uuidQ2 = UUID.randomUUID();
        UUID uuidQ3 = UUID.randomUUID();
        UUID uuidQ4 = UUID.randomUUID();

        Question question1Repo = new Question(uuidQ1, "q1", "c1", newHashSet("c1", "c2"));
        Question question2Repo = new Question(uuidQ2, "q2", "d2", newHashSet("d1", "d2"));
        Question question3Repo = new Question(uuidQ3, "q3", "e2", newHashSet("e1", "e2"));
        Question question4Repo = new Question(uuidQ4, "q4", "p2", newHashSet("p1", "p2"));

        Question question1User = new Question(uuidQ1, "q1", null, newHashSet("c1"));
        Question question2User = new Question(uuidQ2, "q2", null, newHashSet("d2"));
        Question question3User = new Question(uuidQ3, "q3", null, newHashSet("e2"));
        Question question4User = new Question(uuidQ4, "q4", null, newHashSet("p2"));
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setQuestions(newHashSet(question1User, question2User, question3User, question4User));

        when(questionnaireRepository.getQuestion(uuidQ1.toString())).thenReturn(Optional.of(question1Repo));
        when(questionnaireRepository.getQuestion(uuidQ2.toString())).thenReturn(Optional.of(question2Repo));
        when(questionnaireRepository.getQuestion(uuidQ3.toString())).thenReturn(Optional.of(question3Repo));
        when(questionnaireRepository.getQuestion(uuidQ4.toString())).thenReturn(Optional.of(question4Repo));

        Questionnaire result = this.questionnaireService.answerQuestionnaire(questionnaire);

        verify(questionnaireRepository, times(4)).getQuestion(any());

        assertNotNull(result);
        assertNotNull(result.getCustomer());
        assertTrue(result.getQuestionnaireStatus().isPresent());
        assertEquals(QuestionnaireStatus.SUITABLE, result.getQuestionnaireStatus().get());

    }

    @Test
    public void testAnswerQuestionnaireNotSuitable() {

        UUID uuidQ1 = UUID.randomUUID();
        UUID uuidQ2 = UUID.randomUUID();
        UUID uuidQ3 = UUID.randomUUID();
        UUID uuidQ4 = UUID.randomUUID();

        Question question1Repo = new Question(uuidQ1, "q1", "c1", newHashSet("c1", "c2"));
        Question question2Repo = new Question(uuidQ2, "q2", "d2", newHashSet("d1", "d2"));
        Question question3Repo = new Question(uuidQ3, "q3", "e2", newHashSet("e1", "e2"));
        Question question4Repo = new Question(uuidQ4, "q4", "p2", newHashSet("p1", "p2"));

        Question question1User = new Question(uuidQ1, "q1", null, newHashSet("c1"));
        Question question2User = new Question(uuidQ2, "q2", null, newHashSet("d2"));
        //This answer isn't correct
        Question question3User = new Question(uuidQ3, "q3", null, newHashSet("e1"));
        Question question4User = new Question(uuidQ4, "q4", null, newHashSet("p2"));
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setQuestions(newHashSet(question1User, question2User, question3User, question4User));

        when(questionnaireRepository.getQuestion(uuidQ1.toString())).thenReturn(Optional.of(question1Repo));
        when(questionnaireRepository.getQuestion(uuidQ2.toString())).thenReturn(Optional.of(question2Repo));
        when(questionnaireRepository.getQuestion(uuidQ3.toString())).thenReturn(Optional.of(question3Repo));
        when(questionnaireRepository.getQuestion(uuidQ4.toString())).thenReturn(Optional.of(question4Repo));

        Questionnaire result = this.questionnaireService.answerQuestionnaire(questionnaire);

        assertNotNull(result);
        assertNotNull(result.getCustomer());
        assertTrue(result.getQuestionnaireStatus().isPresent());
        assertEquals(QuestionnaireStatus.NOT_SUITABLE, result.getQuestionnaireStatus().get());

    }


    private Map<Integer, Question> buildQuestionnaire() {

        return ImmutableMap.<Integer, Question>builder()
                .put(0, new Question(UUID.randomUUID(), "q1", "c1", newHashSet()))
                .put(1, new Question(UUID.randomUUID(), "q2", "c1", newHashSet()))
                .put(2, new Question(UUID.randomUUID(), "q3", "c1", newHashSet()))
                .put(3, new Question(UUID.randomUUID(), "q4", "c1", newHashSet()))
                .put(4, new Question(UUID.randomUUID(), "q5", "c1", newHashSet()))
                .put(5, new Question(UUID.randomUUID(), "q6", "c1", newHashSet()))
                .put(6, new Question(UUID.randomUUID(), "q7", "c1", newHashSet()))
                .build();

    }
}

