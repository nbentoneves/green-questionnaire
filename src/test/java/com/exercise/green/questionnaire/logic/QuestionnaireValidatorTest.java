package com.exercise.green.questionnaire.logic;

import com.exercise.green.questionnaire.domain.Question;
import com.exercise.green.questionnaire.domain.Questionnaire;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.exercise.green.questionnaire.logic.QuestionnaireValidator.hasOneAnswerPerQuestion;
import static com.exercise.green.questionnaire.logic.QuestionnaireValidator.isValid;
import static com.google.common.collect.Sets.newHashSet;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuestionnaireValidatorTest {

    @Test
    public void testIsNotValidQuestionnaire() {

        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setQuestions(newHashSet());

        assertFalse(isValid(questionnaire));

    }

    @Test
    public void testIsValidQuestionnaire() {

        Question question1 = new Question(UUID.randomUUID(), "Question", "Correct", newHashSet());
        Question question2 = new Question(UUID.randomUUID(), "Question", "Correct", newHashSet());
        Question question3 = new Question(UUID.randomUUID(), "Question", "Correct", newHashSet());
        Question question4 = new Question(UUID.randomUUID(), "Question", "Correct", newHashSet());

        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setQuestions(newHashSet(question1, question2, question3, question4));

        assertTrue(isValid(questionnaire));

    }

    @Test
    public void testQuestionnaireHasOneAnswerPerQuestion() {

        Question question = new Question(UUID.randomUUID(), "Question", "Correct", newHashSet("answer"));

        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setQuestions(newHashSet(question));

        assertTrue(hasOneAnswerPerQuestion(questionnaire));

    }

    @Test
    public void testQuestionnaireHasMoreThanOneAnswerPerQuestion() {

        Question question = new Question(UUID.randomUUID(), "Question", "Correct", newHashSet("answer", "answer2"));

        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setQuestions(newHashSet(question));

        assertFalse(hasOneAnswerPerQuestion(questionnaire));

    }



}
