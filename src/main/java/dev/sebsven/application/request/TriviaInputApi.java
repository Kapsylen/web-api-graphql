package dev.sebsven.application.request;

import java.util.List;

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
