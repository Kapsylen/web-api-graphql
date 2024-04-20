package dev.sebsven.application.response;

import java.util.List;

public record Trivia (
        String type,
        String difficulty,
        String category,
        String question,
        String correct_answer,
        List<String> incorrect_answers
){
}
