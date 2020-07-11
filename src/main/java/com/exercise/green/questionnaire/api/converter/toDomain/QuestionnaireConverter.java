package com.exercise.green.questionnaire.api.converter.toDomain;

import com.exercise.green.questionnaire.api.APIQuestionnaire;
import com.exercise.green.questionnaire.domain.Question;
import com.exercise.green.questionnaire.domain.Questionnaire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import java.util.Set;
import java.util.stream.Collectors;

public class QuestionnaireConverter implements Converter<APIQuestionnaire, Questionnaire> {

    private final QuestionConverter questionConverter;

    @Autowired
    public QuestionnaireConverter(QuestionConverter questionConverter) {
        this.questionConverter = questionConverter;
    }

    @Override
    public Questionnaire convert(APIQuestionnaire apiQuestionnaire) {

        Set<Question> questions = apiQuestionnaire.getQuestions()
                .stream()
                .map(questionConverter::convert)
                .collect(Collectors.toSet());

        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setQuestions(questions);

        return questionnaire;
    }

}
