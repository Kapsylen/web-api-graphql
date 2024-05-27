package dev.sebsven.application.response;

import dev.sebsven.infrastructure.IncorrectAnswer;
import dev.sebsven.infrastructure.entity.Trivia;
import lombok.Builder;
import lombok.Value;

import java.util.List;
@Builder
public record TriviaOutputApi(
        Integer id,
        String type,
        String difficulty,
        String category,
        String question,
        String correctAnswer,
        List<String> incorrectAnswers
) {

    public static TriviaOutputApi toTriviaApi(Trivia trivia) {
        return new TriviaOutputApi(
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