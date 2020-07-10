package com.exercise.green.questionnaire.logic;

import com.exercise.green.questionnaire.domain.Customer;
import com.exercise.green.questionnaire.domain.Question;
import com.exercise.green.questionnaire.domain.Questionnaire;
import com.exercise.green.questionnaire.domain.QuestionnaireStatus;
import com.exercise.green.questionnaire.repository.QuestionnaireRepository;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.exercise.green.questionnaire.logic.QuestionnaireValidator.haveOneAnswerPerQuestion;
import static com.exercise.green.questionnaire.logic.QuestionnaireValidator.isValid;
import static com.google.common.collect.Iterables.getOnlyElement;

@Service
public class QuestionnaireService {

    private final static Logger LOGGER = LoggerFactory.getLogger(QuestionnaireService.class);

    private final QuestionnaireRepository questionnaireRepository;

    @Autowired
    public QuestionnaireService(QuestionnaireRepository questionnaireRepository) {
        this.questionnaireRepository = questionnaireRepository;
    }

    public Questionnaire getQuestionnaire() {

        Questionnaire questionnaire = new Questionnaire();
        Set<Question> questionsForQuestionnaire = Sets.newHashSet();
        Map<Integer, Question> questions = this.questionnaireRepository.getQuestions();

        Random random = new Random();
        Set<Integer> listOfGenerator = Sets.newHashSet();

        while (questionsForQuestionnaire.size() < 4) {

            int nextQuestionIndex = random.nextInt(questions.size());

            if (!listOfGenerator.contains(nextQuestionIndex)) {
                LOGGER.debug("opr=getQuestionnaire, msg='Next question index', nextQuestionIndex={}", nextQuestionIndex);

                Question question = questions.get(nextQuestionIndex);

                LOGGER.info("opr=getQuestionnaire, msg='Question returned by repository', question={}", question);

                questionsForQuestionnaire.add(question);
                listOfGenerator.add(nextQuestionIndex);
            }
        }

        LOGGER.info("opr=getQuestionnaire, msg='Questionnaire returned', questionsForQuestionnaire={}", questionsForQuestionnaire);

        questionnaire.setQuestions(questionsForQuestionnaire);

        return questionnaire;

    }

    public Questionnaire answerQuestionnaire(Questionnaire questionnaireFromUser) {

        Customer customer = new Customer(UUID.randomUUID());

        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setCustomer(customer);

        if (!haveOneAnswerPerQuestion(questionnaireFromUser)) {
            throw new RuntimeException("The questionnaire not allowed more than one answer per question");
        }

        if (!isValid(questionnaireFromUser)) {
            questionnaire.setQuestionnaireStatus(QuestionnaireStatus.NOT_COMPLETE);
        } else {

            AtomicBoolean isValid = new AtomicBoolean(true);

            for (Question question : questionnaireFromUser.getQuestions()) {

                if (!isValid.get()) {
                    continue;
                }

                Optional<Question> repoQuestion = this.questionnaireRepository.getQuestion(question.getQuestionUUID().toString());

                if (!repoQuestion.isPresent()) {
                    String message = String.format("The question with uuid %s not exist at system", question.getQuestionUUID().toString());
                    throw new RuntimeException(message);
                }

                isValid.set(getOnlyElement(question.getAnswers()).equals(repoQuestion.get().getCorrectAnswer()));
            }

            if (isValid.get()) {
                questionnaire.setQuestionnaireStatus(QuestionnaireStatus.SUITABLE);
            } else {
                questionnaire.setQuestionnaireStatus(QuestionnaireStatus.NOT_SUITABLE);
            }
        }

        return questionnaire;

    }

}
