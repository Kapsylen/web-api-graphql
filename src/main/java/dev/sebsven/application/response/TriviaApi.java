package dev.sebsven.application.response;

import dev.sebsven.infrastructure.IncorrectAnswer;
import dev.sebsven.infrastructure.entity.Trivia;
import lombok.Builder;

import java.util.List;
@Builder
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
                trivia.getType(),
                trivia.getDifficulty(),
                trivia.getCategory(),
                trivia.getQuestion(),
                trivia.getCorrectAnswer(),
                trivia.getIncorrectAnswers().stream().map(IncorrectAnswer::getIncorrectAnswer).toList()
        );
    }
}