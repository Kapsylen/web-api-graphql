package dev.sebsven.application.response;

import dev.sebsven.infrastructure.IncorrectAnswer;
import dev.sebsven.infrastructure.entity.Trivia;

import java.util.List;

public record TriviaApi(
        Integer id,
        String type,
        String difficulty,
        String category,
        String question,
        String correctAnswer,
        List<String> incorrectAnswers
) {

    public static TriviaApi toTriviaApi(Trivia trivia) {
        return new TriviaApi(
                trivia.getId(),
                trivia.getCategory(),
                trivia.getType(),
                trivia.getCategory(),
                trivia.getQuestion(),
                trivia.getCorrectAnswer(),
                trivia.getIncorrectAnswers().stream().map(IncorrectAnswer::getIncorrectAnswer).toList()
        );
    }

}