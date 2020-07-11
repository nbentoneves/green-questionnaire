package com.exercise.green.questionnaire;

import com.exercise.green.questionnaire.api.APIQuestion;
import com.exercise.green.questionnaire.api.APIQuestionnaire;
import com.exercise.green.questionnaire.api.APIQuestionnaireResponse;
import com.exercise.green.questionnaire.domain.Question;
import com.exercise.green.questionnaire.repository.QuestionnaireRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.google.common.collect.Sets.newHashSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// FIXME: This test should be moved to another module of the project.
@SpringBootTest
@AutoConfigureMockMvc
class GreenQuestionnaireApplicationTests {

    @Autowired
    private ObjectMapper jsonMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestionnaireRepository questionnaireRepository;

    @BeforeEach
    public void setUp() {
        buildQuestionnaire();
    }

    @Test
    @DisplayName("Get a questionnaire with four random questions")
    public void getQuestionnaireWithFourRandomQuestions() throws Exception {

        MvcResult result = mockMvc.perform(get("/questionnaire/get")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        APIQuestionnaireResponse response = jsonMapper.readValue(result.getResponse().getContentAsString(), APIQuestionnaireResponse.class);

        assertNotNull(response);
        assertEquals(4, response.getQuestions().size());

    }

    @Test
    @DisplayName("Answer a questionnaire with all correct questions")
    public void answerQuestionnaireResultSuitable() throws Exception {

        MvcResult result = mockMvc.perform(get("/questionnaire/get")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        APIQuestionnaireResponse questionnaire = jsonMapper.readValue(result.getResponse().getContentAsString(), APIQuestionnaireResponse.class);

        Set<APIQuestion> questionsWithAnswer = newHashSet();

        for (APIQuestion question : questionnaire.getQuestions()) {

            Question q1 = new Question(UUID.fromString(question.getQuestionId()), "q1", "c1", newHashSet("c1, c2"));
            when(questionnaireRepository.getQuestion(question.getQuestionId())).thenReturn(Optional.of(q1));

            questionsWithAnswer.add(new APIQuestion(question.getQuestionId(), "q1", newHashSet("c1")));
        }

        APIQuestionnaire request = new APIQuestionnaire(questionsWithAnswer);

        MvcResult resultAnswer = mockMvc.perform(post("/questionnaire/answer")
                .content(jsonMapper.writeValueAsString(request))
                .contentType("application/json"))
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        APIQuestionnaireResponse response = jsonMapper.readValue(resultAnswer.getResponse().getContentAsString(), APIQuestionnaireResponse.class);

        assertNotNull(response);
        assertNotNull(response.getCustomerID());
        assertEquals("SUITABLE", response.getStatus());

    }

    @Test
    @DisplayName("Answer a questionnaire with not correct questions")
    public void answerQuestionnaireResultNotSuitable() throws Exception {

        MvcResult result = mockMvc.perform(get("/questionnaire/get")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        APIQuestionnaireResponse questionnaire = jsonMapper.readValue(result.getResponse().getContentAsString(), APIQuestionnaireResponse.class);

        Set<APIQuestion> questionsWithAnswer = newHashSet();

        for (APIQuestion question : questionnaire.getQuestions()) {

            Question q1 = new Question(UUID.fromString(question.getQuestionId()), "q1", "c1", newHashSet("c1, c2"));
            when(questionnaireRepository.getQuestion(question.getQuestionId())).thenReturn(Optional.of(q1));

            questionsWithAnswer.add(new APIQuestion(question.getQuestionId(), "q1", newHashSet("c2")));
        }

        APIQuestionnaire request = new APIQuestionnaire(questionsWithAnswer);

        MvcResult resultAnswer = mockMvc.perform(post("/questionnaire/answer")
                .content(jsonMapper.writeValueAsString(request))
                .contentType("application/json"))
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        APIQuestionnaireResponse response = jsonMapper.readValue(resultAnswer.getResponse().getContentAsString(), APIQuestionnaireResponse.class);

        assertNotNull(response);
        assertNotNull(response.getCustomerID());
        assertEquals("NOT_SUITABLE", response.getStatus());

    }

    @Test
    @DisplayName("Answer a questionnaire but missing questions")
    public void answerQuestionnaireResultNotComplete() throws Exception {

        MvcResult result = mockMvc.perform(get("/questionnaire/get")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        Set<APIQuestion> questionsWithAnswer = newHashSet();
        questionsWithAnswer.add(new APIQuestion(UUID.randomUUID().toString(), "q1", newHashSet("c2")));

        APIQuestionnaire request = new APIQuestionnaire(questionsWithAnswer);

        MvcResult resultAnswer = mockMvc.perform(post("/questionnaire/answer")
                .content(jsonMapper.writeValueAsString(request))
                .contentType("application/json"))
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        APIQuestionnaireResponse response = jsonMapper.readValue(resultAnswer.getResponse().getContentAsString(), APIQuestionnaireResponse.class);

        assertNotNull(response);
        assertNotNull(response.getCustomerID());
        assertEquals("NOT_COMPLETE", response.getStatus());

    }

    private void buildQuestionnaire() {

        Map<Integer, Question> repo = ImmutableMap.<Integer, Question>builder()
                .put(0, new Question(UUID.randomUUID(), "q1", "c1", newHashSet()))
                .put(1, new Question(UUID.randomUUID(), "q2", "c1", newHashSet()))
                .put(2, new Question(UUID.randomUUID(), "q3", "c1", newHashSet()))
                .put(3, new Question(UUID.randomUUID(), "q4", "c1", newHashSet()))
                .put(4, new Question(UUID.randomUUID(), "q5", "c1", newHashSet()))
                .put(5, new Question(UUID.randomUUID(), "q6", "c1", newHashSet()))
                .put(6, new Question(UUID.randomUUID(), "q7", "c1", newHashSet()))
                .build();

        when(questionnaireRepository.getQuestions()).thenReturn(repo);

    }

}
