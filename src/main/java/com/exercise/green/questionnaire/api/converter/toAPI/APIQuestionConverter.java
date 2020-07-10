package com.exercise.green.questionnaire.api.converter.toAPI;

import com.exercise.green.questionnaire.api.APIQuestion;
import com.exercise.green.questionnaire.domain.Question;
import org.springframework.core.convert.converter.Converter;

public final class APIQuestionConverter implements Converter<Question, APIQuestion> {

    @Override
    public APIQuestion convert(Question question) {

        return new APIQuestion(question.getQuestionUUID().toString(),
                question.getQuestion(), question.getAnswers());

    }

}
