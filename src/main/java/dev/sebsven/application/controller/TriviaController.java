package dev.sebsven.application.controller;

import dev.sebsven.application.response.TriviaApi;
import dev.sebsven.domain.TriviaService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class TriviaController {

    private final TriviaService triviaService;

    public TriviaController(TriviaService triviaService) {
        this.triviaService = triviaService;
    }

    @QueryMapping
    public TriviaApi triviaById(@Argument Integer id) {
        return triviaService.triviaById(id).orElse(null);
    }

    @SchemaMapping(typeName = "Query", value = "incorrectAnswers")
    public List<String> incorrectAnswersByTriviaId(@Argument Integer id) {
        return triviaService.getIncorrectAnswers(id);
    }

    @QueryMapping
    public List<TriviaApi> allTrivia() {
        return triviaService.getAllTrivia();
    }

    @QueryMapping
    public List<TriviaApi> triviaByType(@Argument String type) {
        return triviaService.getByType(type);
    }
}