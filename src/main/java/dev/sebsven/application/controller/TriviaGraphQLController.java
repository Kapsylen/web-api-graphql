package dev.sebsven.application.controller;

import dev.sebsven.application.request.TriviaInputApi;
import dev.sebsven.application.response.TriviaOutputApi;
import dev.sebsven.domain.TriviaService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class TriviaGraphQLController {

    private final TriviaService triviaService;

    public TriviaGraphQLController(TriviaService triviaService) {
        this.triviaService = triviaService;
    }

    @QueryMapping
    public TriviaOutputApi triviaById(@Argument Integer id) {
        return triviaService.triviaById(id);
    }

    @SchemaMapping(typeName = "Query", value = "incorrectAnswers")
    public List<String> incorrectAnswersByTriviaId(@Argument Integer id) {
        return triviaService.getIncorrectAnswers(id);
    }

    @QueryMapping
    public List<TriviaOutputApi> allTrivia() {
        return triviaService.getAllTrivia("10", null, null);
    }

    @QueryMapping
    public List<TriviaOutputApi> triviaByType(@Argument String type) {
        return triviaService.getByType(type);
    }

    @MutationMapping
    public TriviaOutputApi newTrivia(@Argument TriviaInputApi triviaInputApi) {
      return triviaService.create(triviaInputApi);
    }
}