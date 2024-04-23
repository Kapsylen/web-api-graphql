package dev.sebsven.infrastructure.entity;

import dev.sebsven.domain.response.TriviaDto;
import dev.sebsven.infrastructure.IncorrectAnswer;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "trivia")
public class Trivia {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    String type;
    String difficulty;
    String category;
    String question;
    String correctAnswer;
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<IncorrectAnswer> incorrectAnswers;


    public static Trivia toTrivia(TriviaDto triviaDto) {
        Trivia trivia = new Trivia();
        trivia.setType(triviaDto.type());
        trivia.setDifficulty(triviaDto.difficulty());
        trivia.setCategory(triviaDto.category());
        trivia.setQuestion(triviaDto.question());
        trivia.setCorrectAnswer(triviaDto.correct_answer());
        trivia.setIncorrectAnswers(triviaDto.incorrect_answers().stream()
                .map(incorrectAnswer -> {
                    IncorrectAnswer incorrectAnswerEntity = new IncorrectAnswer();
                    incorrectAnswerEntity.setIncorrectAnswer(incorrectAnswer);
                    return incorrectAnswerEntity;
                })
                .collect(Collectors.toList()));
        return trivia;
    }

}
