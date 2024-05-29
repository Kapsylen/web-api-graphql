package dev.sebsven.domain;

import dev.sebsven.application.request.TriviaInputApi;
import dev.sebsven.application.response.TriviaOutputApi;
import dev.sebsven.infrastructure.IncorrectAnswer;
import dev.sebsven.infrastructure.TriviaRepository;
import dev.sebsven.infrastructure.entity.Trivia;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class TriviaServiceTest {

    @Autowired
    TriviaService triviaService;

    @MockBean
    TriviaRepository repository;


    @Test
    void when_triviaById_then_triviaIsReturned() {
        Trivia trivia = Trivia.builder()
                .id(1)
                .type("multiple")
                .difficulty("medium")
                .category("General Knowledge")
                .question("What is the capital of France?")
                .correctAnswer("Paris")
                .incorrectAnswers(List.of(IncorrectAnswer.builder().id(1).incorrectAnswer("London").build(),
                        IncorrectAnswer.builder().id(2).incorrectAnswer("Berlin").build(),
                        IncorrectAnswer.builder().id(3).incorrectAnswer("Rome").build())
                )
                .build();
        when(repository.findById(1)).thenReturn(Optional.of(trivia));
        TriviaOutputApi expectedOutput = TriviaOutputApi.builder()
                .id(1)
                .type("multiple")
                .difficulty("medium")
                .category("General Knowledge")
                .question("What is the capital of France?")
                .correctAnswer("Paris")
                .incorrectAnswers(Arrays.asList("London", "Berlin", "Rome"))
                .build();
        TriviaOutputApi actualOutput = triviaService.triviaById(1);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void when_getTriviaByType_then_triviaIsReturned() {
        List<Trivia> triviaList = List.of(
                Trivia.builder()
                        .id(1)
                        .type("multiple")
                        .difficulty("medium")
                        .category("General Knowledge")
                        .question("What is the capital of France?")
                        .correctAnswer("Paris")
                        .incorrectAnswers(List.of(IncorrectAnswer.builder().id(1).incorrectAnswer("London").build(),
                                IncorrectAnswer.builder().id(2).incorrectAnswer("Berlin").build(),
                                IncorrectAnswer.builder().id(3).incorrectAnswer("Rome").build())
                        )
                        .build(),
                Trivia.builder()
                        .id(2)
                        .type("multiple")
                        .difficulty("medium")
                        .category("General Knowledge")
                        .question("What is the capital of Sweden?")
                        .correctAnswer("Stockholm")
                        .incorrectAnswers(List.of(IncorrectAnswer.builder().id(2).incorrectAnswer("London").build(),
                                IncorrectAnswer.builder().id(2).incorrectAnswer("Berlin").build(),
                                IncorrectAnswer.builder().id(3).incorrectAnswer("Rome").build())
                        )
                        .build()
        );
        when(repository.findByType("multiple")).thenReturn(triviaList);
        List<TriviaOutputApi> expectedOutput = List.of(
                TriviaOutputApi.builder()
                        .id(1)
                        .type("multiple")
                        .difficulty("medium")
                        .category("General Knowledge")
                        .question("What is the capital of France?")
                        .correctAnswer("Paris")
                        .incorrectAnswers(Arrays.asList("London", "Berlin", "Rome"))
                        .build(),
                TriviaOutputApi.builder()
                        .id(2)
                        .type("multiple")
                        .difficulty("medium")
                        .category("General Knowledge")
                        .question("What is the capital of Sweden?")
                        .correctAnswer("Stockholm")
                        .incorrectAnswers(Arrays.asList("London", "Berlin", "Rome"))
                        .build()
        );
        List<TriviaOutputApi> actualOutput = triviaService.getByType("multiple");
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void when_getAllTrivia_then_triviaIsReturned() {

        List<Trivia> triviaList = List.of(
                Trivia.builder()
                        .id(1)
                        .type("multiple")
                        .difficulty("medium")
                        .category("General Knowledge")
                        .question("What is the capital of France?")
                        .correctAnswer("Paris")
                        .incorrectAnswers(List.of(IncorrectAnswer.builder().id(1).incorrectAnswer("London").build(),
                                IncorrectAnswer.builder().id(2).incorrectAnswer("Berlin").build(),
                                IncorrectAnswer.builder().id(3).incorrectAnswer("Rome").build())
                        )
                        .build(),
                Trivia.builder()
                        .id(2)
                        .type("multiple")
                        .difficulty("medium")
                        .category("General Knowledge")
                        .question("What is the capital of Sweden?")
                        .correctAnswer("Stockholm")
                        .incorrectAnswers(List.of(IncorrectAnswer.builder().id(2).incorrectAnswer("London").build(),
                                IncorrectAnswer.builder().id(2).incorrectAnswer("Berlin").build(),
                                IncorrectAnswer.builder().id(3).incorrectAnswer("Rome").build())
                        )
                        .build()
        );
        List<TriviaOutputApi> expectedOutput = List.of(
                TriviaOutputApi.builder()
                        .id(1)
                        .type("multiple")
                        .difficulty("medium")
                        .category("General Knowledge")
                        .question("What is the capital of France?")
                        .correctAnswer("Paris")
                        .incorrectAnswers(Arrays.asList("London", "Berlin", "Rome"))
                        .build(),
                TriviaOutputApi.builder()
                        .id(2)
                        .type("multiple")
                        .difficulty("medium")
                        .category("General Knowledge")
                        .question("What is the capital of Sweden?")
                        .correctAnswer("Stockholm")
                        .incorrectAnswers(Arrays.asList("London", "Berlin", "Rome"))
                        .build()
        );
        when(repository.findAll()).thenReturn(triviaList);

        List<TriviaOutputApi> actualOutput = triviaService.getAllTrivia("2", null, null);
        assertEquals(expectedOutput, actualOutput);

    }

    @Test
    void when_createTrivia_then_triviaIsReturned() {
        TriviaInputApi triviaInputApi = TriviaInputApi.builder()
                .type("multiple")
                .difficulty("medium")
                .category("General Knowledge")
                .question("What is the capital of France?")
                .correctAnswer("Paris")
                .incorrectAnswers(Arrays.asList("London", "Berlin", "Rome"))
                .build();
        Trivia trivia = Trivia.builder()
                .id(1)
                .type("multiple")
                .difficulty("medium")
                .category("General Knowledge")
                .question("What is the capital of France?")
                .correctAnswer("Paris")
                .incorrectAnswers(List.of(IncorrectAnswer.builder().id(1).incorrectAnswer("London").build(),
                        IncorrectAnswer.builder().id(2).incorrectAnswer("Berlin").build(),
                        IncorrectAnswer.builder().id(3).incorrectAnswer("Rome").build())
                )
                .build();
        when(repository.save(any())).thenReturn(trivia);
        TriviaOutputApi expectedOutput = TriviaOutputApi.builder()
                .id(1)
                .type("multiple")
                .difficulty("medium")
                .category("General Knowledge")
                .question("What is the capital of France?")
                .correctAnswer("Paris")
                .incorrectAnswers(Arrays.asList("London", "Berlin", "Rome"))
                .build();
        TriviaOutputApi actualOutput = triviaService.create(triviaInputApi);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void when_deleteTriviaWithExistingId_then_noExceptionIsThrown() {
        var trivia = Optional.of(Trivia.builder()
                .id(1)
                .type("multiple")
                .difficulty("medium")
                .category("General Knowledge")
                .question("What is the capital of France?")
                .correctAnswer("Paris")
                .incorrectAnswers(List.of(IncorrectAnswer.builder().id(1).incorrectAnswer("London").build(),
                        IncorrectAnswer.builder().id(2).incorrectAnswer("Berlin").build(),
                        IncorrectAnswer.builder().id(3).incorrectAnswer("Rome").build())
                )
                .build());
        when(repository.findById(1)).thenReturn(trivia);
        doNothing().when(repository).deleteById(anyInt());
        assertDoesNotThrow(() -> triviaService.delete(1));
    }

    @Test
    void when_updateTrivia_then_triviaIsReturned() {
        TriviaInputApi triviaInputApi = TriviaInputApi.builder()
                .type("multiple")
                .difficulty("easy")
                .category("General Knowledge")
                .question("What is the capital of France?")
                .correctAnswer("Paris")
                .incorrectAnswers(Arrays.asList("London", "Berlin", "Rome"))
                .build();
        Trivia trivia = Trivia.builder()
                .id(1)
                .type("multiple")
                .difficulty("medium")
                .category("General Knowledge")
                .question("What is the capital of France?")
                .correctAnswer("Paris")
                .incorrectAnswers(List.of(IncorrectAnswer.builder().id(1).incorrectAnswer("London").build(),
                        IncorrectAnswer.builder().id(2).incorrectAnswer("Berlin").build(),
                        IncorrectAnswer.builder().id(3).incorrectAnswer("Rome").build())
                )
                .build();
        when(repository.findById(1)).thenReturn(Optional.of(trivia));
        when(repository.save(any())).thenReturn(trivia);
        TriviaOutputApi expectedOutput = TriviaOutputApi.builder()
                .id(1)
                .type("multiple")
                .difficulty("easy")
                .category("General Knowledge")
                .question("What is the capital of France?")
                .correctAnswer("Paris")
                .incorrectAnswers(Arrays.asList("London", "Berlin", "Rome"))
                .build();
        TriviaOutputApi actualOutput = triviaService.update(1, triviaInputApi);
        assertEquals(expectedOutput, actualOutput);
    }

}