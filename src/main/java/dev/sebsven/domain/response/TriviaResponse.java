package dev.sebsven.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
public record TriviaResponse(
        int response_code,
        List<TriviaDto> results
) {
}