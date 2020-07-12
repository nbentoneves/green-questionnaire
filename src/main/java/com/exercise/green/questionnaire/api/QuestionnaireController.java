package com.exercise.green.questionnaire.api;

import com.exercise.green.questionnaire.api.converter.toAPI.APIQuestionnaireConverter;
import com.exercise.green.questionnaire.api.converter.toDomain.QuestionnaireConverter;
import com.exercise.green.questionnaire.domain.Questionnaire;
import com.exercise.green.questionnaire.logic.QuestionnaireService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Random;

import static org.springframework.util.CollectionUtils.isEmpty;

@RestController
public class QuestionnaireController {

    private final static Logger LOGGER = LoggerFactory.getLogger(QuestionnaireController.class);

    private final APIQuestionnaireConverter apiQuestionnaireConverter;

    private final QuestionnaireConverter questionnaireConverter;

    private final QuestionnaireService questionnaireService;

    @Autowired
    public QuestionnaireController(APIQuestionnaireConverter apiQuestionnaireConverter,
                                   QuestionnaireConverter questionnaireConverter,
                                   QuestionnaireService questionnaireService) {
        this.apiQuestionnaireConverter = apiQuestionnaireConverter;
        this.questionnaireConverter = questionnaireConverter;
        this.questionnaireService = questionnaireService;
    }

    @GetMapping("/questionnaire/get")
    public APIQuestionnaireResponse getQuestionnaire() {

        Questionnaire questionnaire = this.questionnaireService.getQuestionnaire(new Random());

        LOGGER.info("opr=getQuestionnaire, msg='Get questionnaire for the user', questionnaire={}", questionnaire);

        return apiQuestionnaireConverter.convert(questionnaire);

    }

    @PostMapping("/questionnaire/answer")
    public APIQuestionnaireResponse answerQuestionnaire(@RequestBody APIQuestionnaire questionnaire) {

        try {

            Questionnaire questionnaireFromUser = this.questionnaireConverter.convert(questionnaire);

            if (isEmpty(questionnaireFromUser.getQuestions())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request, please check the body");
            }

            questionnaireFromUser.getQuestions()
                    .forEach(question ->
                            LOGGER.info("opr=answerQuestionnaire, msg='Answer for the user', question={}", question));

            Questionnaire result = this.questionnaireService.answerQuestionnaire(questionnaireFromUser);

            return apiQuestionnaireConverter.convert(result);
        } catch (Exception ex) {
            LOGGER.error("opr=answerQuestionnaire, msg='Something is wrong'", ex);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request, please contact the service admin");
        }

    }

}
