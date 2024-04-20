package dev.sebsven.infrastructure;

import dev.sebsven.application.response.Trivia;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "trivia")
public class TriviaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String type;
    String difficulty;
    String category;
    String question;
    String correctAnswer;
    @OneToMany(mappedBy = "triviaEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IncorrectAnswerEntity> incorrectAnswers;


    public static TriviaEntity toTriviaEntity(Trivia trivia) {
        TriviaEntity triviaEntity = new TriviaEntity();
        triviaEntity.setType(trivia.type());
        triviaEntity.setDifficulty(trivia.difficulty());
        triviaEntity.setCategory(trivia.category());
        triviaEntity.setQuestion(trivia.question());
        triviaEntity.setCorrectAnswer(trivia.correct_answer());
        triviaEntity.setIncorrectAnswers(trivia.incorrect_answers().stream()
                .map(incorrectAnswer -> {
                    IncorrectAnswerEntity incorrectAnswerEntity = new IncorrectAnswerEntity();
                    incorrectAnswerEntity.setIncorrectAnswer(incorrectAnswer);
                    incorrectAnswerEntity.setTriviaEntity(triviaEntity);
                    return incorrectAnswerEntity;
                })
                .collect(Collectors.toList()));
        return triviaEntity;
    }

}
