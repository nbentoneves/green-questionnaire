package com.exercise.green.questionnaire.repository;

import com.exercise.green.questionnaire.domain.Question;
import com.exercise.green.questionnaire.domain.QuestionYaml;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.requireNonNull;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

/**
 * This repository has the responsability to load the questionnaire from a YAML file.
 * If you want to implement a different repository we should create a factory to select each type of
 * repository should be create.
 */
@Repository
public class QuestionnaireRepository {

    private final static Logger LOGGER = LoggerFactory.getLogger(QuestionnaireRepository.class);

    /**
     * Note: To increase the performance when get random questions
     * I decided to use Map because I can get a questions using the key (at map the cust is O(1))
     */
    private final Map<Integer, Question> questionsRepository;

    @Autowired
    public QuestionnaireRepository(String fileName) {

        checkArgument(isNotBlank(fileName), "fileName can not be null or empty");

        try {
            File file = new File(requireNonNull(getClass().getClassLoader().getResource(fileName), "Can not find the correct file").toURI());

            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

            List<Question> questions = mapper
                    .readValue(file, new TypeReference<List<QuestionYaml>>() {
                    })
                    .stream()
                    .map(yml -> new Question(yml.getQuestionUUID(), yml.getQuestion(),
                            yml.getCorrectAnswer(), yml.getAnswers()))
                    .collect(Collectors.toList());

            this.questionsRepository = IntStream.range(0, questions.size())
                    .boxed()
                    .collect(Collectors.toMap(i -> i, questions::get));

        } catch (Exception ex) {
            LOGGER.warn("opr=create, msg='Something wrong happened'", ex);
            throw new RuntimeException("Please fix the QuestionnaireRepository", ex);
        }

        checkState(this.questionsRepository.size() >= 4, "the questions repository should have more then 4 questions");

    }

    public Optional<Question> getQuestion(String uuid) {
        return questionsRepository.values()
                .stream()
                .filter(question -> uuid.equals(question.getQuestionUUID().toString())).findFirst();
    }

    public Map<Integer, Question> getQuestions() {
        return questionsRepository;
    }

}
