package dev.sebsven.domain.response;

import java.util.List;

public record TriviaResponse(
        int response_code,
        List<TriviaDto> results
) {
}