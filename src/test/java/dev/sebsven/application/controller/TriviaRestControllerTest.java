package dev.sebsven.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sebsven.application.request.TriviaInputApi;
import dev.sebsven.application.response.TriviaOutputApi;
import dev.sebsven.domain.TriviaService;
import dev.sebsven.domain.error.ResourceNotFoundException;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TriviaRestController.class)
class TriviaRestControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    private TriviaService triviaService;

    @Test
    void when_post_trivia_then_should_return_200() throws Exception {
        var triviaInputApi = new TriviaInputApi("type", "difficulty","category","10","question", "correctAnswer", Arrays.asList("incorrectAnswer1", "incorrectAnswer2", "incorrectAnswer3"));
        var createdTrivia = new TriviaOutputApi(1,"", "type", "difficulty","category","question", Arrays.asList("incorrectAnswer1", "incorrectAnswer2", "incorrectAnswer3"));
        given(triviaService.create(triviaInputApi)).willReturn(createdTrivia);

        String triviaRequest = new ObjectMapper().writeValueAsString(triviaInputApi);

        mvc.perform(MockMvcRequestBuilders
                        .post("/trivia")
                        .content(triviaRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.difficulty").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.category").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.question").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.incorrectAnswers").exists());
    }

   @Test
    public void whenPostRequest_create_trivia_api_given_invalid_number_of_incorrect_answers_then_return_bad_request_with_correct_error_message() throws Exception {
        var triviaInputApi = new TriviaInputApi("type", "difficulty","category","10","question", "correctAnswer", Arrays.asList("Rome", "Berlin", "London", "Amsterdam"));
        String triviaRequest = new ObjectMapper().writeValueAsString(triviaInputApi);
        mvc.perform(MockMvcRequestBuilders.post("/trivia")
                        .content(triviaRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.apierror.status", Is.is("BAD_REQUEST")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apierror.message", Is.is("Validation error")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apierror.subErrors.[0].rejectedValue[0]", Is.is("Rome")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apierror.subErrors.[0].rejectedValue[1]", Is.is("Berlin")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apierror.subErrors.[0].rejectedValue[2]", Is.is("London")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apierror.subErrors.[0].rejectedValue[3]", Is.is("Amsterdam")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apierror.subErrors.[0].message", Is.is("size must be between 3 and 3")));

    }

    @Test
    public void whenPostRequest_create_trivia_api_given_invalid_number_of_incorrect_answers_and_values_is_empty_then_return_bad_request_with_correct_error_message() throws Exception {
        var triviaInputApi = new TriviaInputApi("type", "difficulty","category","10","question", "correctAnswer", Arrays.asList("London", "Amsterdam", ""));
        String triviaRequest = new ObjectMapper().writeValueAsString(triviaInputApi);
        mvc.perform(MockMvcRequestBuilders.post("/trivia")
                        .content(triviaRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.apierror.status", Is.is("BAD_REQUEST")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apierror.message", Is.is("Validation error")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apierror.subErrors.[0].message", Is.is("List cannot contain empty fields")));

    }


    @Test
    void when_get_trivia_by_id_then_should_return_200() throws Exception {
        var triviaOutputApi = new TriviaOutputApi(1, "type", "difficulty", "category", "question", "correctAnswer",  Arrays.asList("incorrectAnswer1", "incorrectAnswer2", "incorrectAnswer3"));
        given(triviaService.triviaById(1)).willReturn(triviaOutputApi);

        mvc.perform(MockMvcRequestBuilders
                        .get("/trivia/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("type"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.difficulty").value("difficulty"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("category"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.question").value("question"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.correctAnswer").value("correctAnswer"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incorrectAnswers.[0]").value("incorrectAnswer1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incorrectAnswers.[1]").value("incorrectAnswer2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incorrectAnswers.[2]").value("incorrectAnswer3"));
    }


    @Test
    void when_get_trivia_by_id_given_not_exist_then_should_return_404() throws Exception {
        given(triviaService.triviaById(1)).willThrow(ResourceNotFoundException.class);

        mvc.perform(MockMvcRequestBuilders
                .get("/trivia/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void when_delete_trivia_then_should_return_204() throws Exception {
        TriviaOutputApi triviaOutputApi = new TriviaOutputApi(1, "type", "difficulty", "category", "question", "correctAnswer", Arrays.asList("incorrectAnswer1", "incorrectAnswer2", "incorrectAnswer3"));
        given(triviaService.triviaById(1)).willReturn(triviaOutputApi);
        mvc.perform(MockMvcRequestBuilders
                        .delete("/trivia/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void when_delete_trivia_by_id_given_not_exist_then_should_return_404() throws Exception {

        doThrow(ResourceNotFoundException.class).when(triviaService).triviaById(1);

        mvc.perform(MockMvcRequestBuilders
                        .get("/trivia/{id}", 1))
                .andExpect(status().isNotFound());
    }


    @Test
    void when_update_trivia_then_should_return_200() throws Exception {
        var triviaInputApi = new TriviaInputApi("type", "easy", "data science", "9", "question", "correctAnswer", Arrays.asList("incorrectAnswer1", "incorrectAnswer2", "incorrectAnswer3"));
        var updatedTrivia = new TriviaOutputApi(1, "type2", "hard", "9", "question2", "correctAnswer", Arrays.asList("incorrectAnswer1", "incorrectAnswer2", "incorrectAnswer3"));
        given(triviaService.update(1, triviaInputApi)).willReturn(updatedTrivia);

        String triviaRequest = new ObjectMapper().writeValueAsString(triviaInputApi);

        mvc.perform(MockMvcRequestBuilders
                        .put("/trivia/{id}", 1)
                        .content(triviaRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(updatedTrivia.id()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value(updatedTrivia.type()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.difficulty").value(updatedTrivia.difficulty()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category").value(updatedTrivia.category()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.question").value(updatedTrivia.question()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incorrectAnswers.[0]").value(updatedTrivia.incorrectAnswers().get(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incorrectAnswers.[1]").value(updatedTrivia.incorrectAnswers().get(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.incorrectAnswers.[2]").value(updatedTrivia.incorrectAnswers().get(2)));
    }

    @Test
    void when_update_trivia_given_not_exist_then_should_return_404() throws Exception {
        var triviaInputApi = new TriviaInputApi("type", "easy", "data science", "9", "question", "correctAnswer", Arrays.asList("incorrectAnswer1", "incorrectAnswer2", "incorrectAnswer3"));
        String triviaRequest = new ObjectMapper().writeValueAsString(triviaInputApi);
        given(triviaService.update(1, triviaInputApi)).willThrow(ResourceNotFoundException.class);


        mvc.perform(MockMvcRequestBuilders
                        .put("/trivia/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(triviaRequest))
                .andExpect(status().isNotFound());

    }

    @Test
    void when_get_all_trivia_by_category_then_should_return_200() throws Exception {
        var triviaOutputApiList = Arrays.asList(
                new TriviaOutputApi(1, "type1", "easy", "1", "question1", "correctAnswer1", Arrays.asList("incorrectAnswer1", "incorrectAnswer2", "incorrectAnswer3")),
                new TriviaOutputApi(2, "type2", "easy", "1", "question2", "correctAnswer2", Arrays.asList("incorrectAnswer4", "incorrectAnswer5", "incorrectAnswer6"))
        );
        given(triviaService.getAllTrivia("10", "1", null)).willReturn(triviaOutputApiList);

        mvc.perform(MockMvcRequestBuilders
                        .get("/trivias")
                        .param("amount", "10")
                        .param("category", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].type").value("type1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].difficulty").value("easy"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].category").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].question").value("question1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].correctAnswer").value("correctAnswer1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].incorrectAnswers.[0]").value("incorrectAnswer1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].incorrectAnswers.[1]").value("incorrectAnswer2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].incorrectAnswers.[2]").value("incorrectAnswer3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].type").value("type2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].difficulty").value("easy"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].category").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].question").value("question2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].correctAnswer").value("correctAnswer2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].incorrectAnswers.[0]").value("incorrectAnswer4"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].incorrectAnswers.[1]").value("incorrectAnswer5"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].incorrectAnswers.[2]").value("incorrectAnswer6"));
    }
}