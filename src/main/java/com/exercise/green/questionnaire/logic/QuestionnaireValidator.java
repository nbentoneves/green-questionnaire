package com.exercise.green.questionnaire.logic;

import com.exercise.green.questionnaire.domain.Question;
import com.exercise.green.questionnaire.domain.Questionnaire;

public class QuestionnaireValidator {

    public static boolean isValid(Questionnaire questionnaire) {
        return questionnaire.getQuestions().size() == 4;
    }

    public static boolean haveOneAnswerPerQuestion(Questionnaire questionnaire) {

        for (Question question : questionnaire.getQuestions()) {
            if (question.getAnswers().size() != 1) {
                return false;
            }
        }

        return true;
    }

}
