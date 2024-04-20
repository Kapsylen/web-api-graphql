package dev.sebsven.infrastructure;
import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "incorrect_answers")
public class IncorrectAnswerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trivia_id", nullable = false)
    private TriviaEntity triviaEntity;

    private String incorrectAnswer;
}