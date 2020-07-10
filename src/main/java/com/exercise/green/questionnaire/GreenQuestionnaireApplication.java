package com.exercise.green.questionnaire;

import com.exercise.green.questionnaire.api.converter.toAPI.APIQuestionConverter;
import com.exercise.green.questionnaire.api.converter.toAPI.APIQuestionnaireConverter;
import com.exercise.green.questionnaire.api.converter.toDomain.QuestionConverter;
import com.exercise.green.questionnaire.api.converter.toDomain.QuestionnaireConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class GreenQuestionnaireApplication {

    public static void main(String[] args) {
        SpringApplication.run(GreenQuestionnaireApplication.class, args);
    }

    @Autowired
    private Environment environment;

    @Bean(name = "fileName")
    public String getFileName() {
        return environment.getProperty("file.question");
    }

    @Bean
    @Scope("singleton")
    public APIQuestionConverter apiQuestionConverter() {
        return new APIQuestionConverter();
    }

    @Bean
    @Scope("singleton")
    public APIQuestionnaireConverter apiQuestionnaireConverter(APIQuestionConverter questionConverter) {
        return new APIQuestionnaireConverter(questionConverter);
    }

    @Bean
    @Scope("singleton")
    public QuestionConverter questionConverter() {
        return new QuestionConverter();
    }

    @Bean
    @Scope("singleton")
    public QuestionnaireConverter questionnaireConverter(QuestionConverter questionConverter) {
        return new QuestionnaireConverter(questionConverter);
    }


}
