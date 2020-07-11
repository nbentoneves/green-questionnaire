package com.exercise.green.questionnaire.logic;

import com.exercise.green.questionnaire.domain.Question;
import com.exercise.green.questionnaire.domain.Questionnaire;

/**
 * Validator to check the questionnaire
 */
public final class QuestionnaireValidator {

    private QuestionnaireValidator() {
    }

    public static boolean isValid(Questionnaire questionnaire) {
        //TODO: Should have the possibility to set the number of question in questionnaire
        return questionnaire.getQuestions().size() == 4;
    }

    public static boolean hasOneAnswerPerQuestion(Questionnaire questionnaire) {

        for (Question question : questionnaire.getQuestions()) {
            if (question.getAnswers().size() != 1) {
                return false;
            }
        }

        return true;
    }

}
