package com.exercise.green.questionnaire.api.converter.toAPI;

import com.exercise.green.questionnaire.api.APIQuestion;
import com.exercise.green.questionnaire.api.APIQuestionnaireResponse;
import com.exercise.green.questionnaire.domain.Questionnaire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.stream.Collectors;

public final class APIQuestionnaireConverter implements Converter<Questionnaire, APIQuestionnaireResponse> {

    private final APIQuestionConverter questionConverter;

    @Autowired
    public APIQuestionnaireConverter(APIQuestionConverter questionConverter) {
        this.questionConverter = questionConverter;
    }

    @Override
    public APIQuestionnaireResponse convert(Questionnaire questionnaire) {

        Set<APIQuestion> questions = null;
        String status = null;
        String customerInfo = null;

        if (!CollectionUtils.isEmpty(questionnaire.getQuestions())) {
            questions = questionnaire.getQuestions()
                    .stream()
                    .map(questionConverter::convert)
                    .collect(Collectors.toSet());
        }

        if (questionnaire.getQuestionnaireStatus().isPresent()) {
            status = questionnaire.getQuestionnaireStatus().get().name();
        }

        if (questionnaire.getCustomer().isPresent()) {
            customerInfo = questionnaire.getCustomer().get().getCustomerId().toString();
        }

        return new APIQuestionnaireResponse(status, customerInfo, questions);
    }

}
