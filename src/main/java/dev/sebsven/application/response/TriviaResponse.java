package dev.sebsven.application.response;

import java.util.List;

public record TriviaResponse(
        int response_code,
        List<Trivia> results
) {
}