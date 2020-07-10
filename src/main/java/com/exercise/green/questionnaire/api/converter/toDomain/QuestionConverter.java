package com.exercise.green.questionnaire.api.converter.toDomain;

import com.exercise.green.questionnaire.api.APIQuestion;
import com.exercise.green.questionnaire.domain.Question;
import org.springframework.core.convert.converter.Converter;

import java.util.UUID;

public class QuestionConverter implements Converter<APIQuestion, Question> {

    @Override
    public Question convert(APIQuestion apiQuestion) {

        UUID uuid = UUID.fromString(apiQuestion.getQuestionId());
        
        return new Question(uuid, apiQuestion.getQuestion(), null, apiQuestion.getAnswers());

    }
}
