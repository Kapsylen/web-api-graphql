package dev.sebsven.domain.response;

import lombok.Builder;

import java.util.List;

@Builder
public record TriviaResponse(
        int response_code,
        List<TriviaDto> results
) {
}