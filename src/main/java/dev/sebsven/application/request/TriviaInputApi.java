package dev.sebsven.application.request;

import lombok.Builder;

import java.util.List;

@Builder
public record TriviaInputApi(
        String type,
        String difficulty,
        String category,
        String categoryId,
        String question,
        String correctAnswer,
        List<String> incorrectAnswers
){
}
