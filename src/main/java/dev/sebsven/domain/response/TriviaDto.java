package dev.sebsven.domain.response;

import lombok.Builder;

import java.util.List;

@Builder
public record TriviaDto(
        String type,
        String difficulty,
        String category,
        String question,
        String correct_answer,
        List<String> incorrect_answers
){
}
