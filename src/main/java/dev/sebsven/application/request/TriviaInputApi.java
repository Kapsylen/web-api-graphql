package dev.sebsven.application.request;

import dev.sebsven.domain.error.validation.NotEmptyFields;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record TriviaInputApi(
        @NotBlank(message = "Type is mandatory")
        String type,
        @NotBlank(message = "Difficulty is mandatory")
        String difficulty,
        @NotBlank(message = "Category is mandatory")
        String category,
        @NotBlank(message = "CategoryId is mandatory")
        String categoryId,
        @NotBlank(message = "Question is mandatory")
        String question,
        @NotBlank(message = "CorrectAnswer is mandatory")
        String correctAnswer,
        @Size(min = 3, max = 3)
        @NotEmptyFields
        List<String> incorrectAnswers
){
}
