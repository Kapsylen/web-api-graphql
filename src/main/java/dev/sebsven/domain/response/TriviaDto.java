package dev.sebsven.domain.response;

import java.util.List;

public record TriviaDto(
        String type,
        String difficulty,
        String category,
        String question,
        String correct_answer,
        List<String> incorrect_answers
){
}
