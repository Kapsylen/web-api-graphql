package dev.sebsven.infrastructure;
import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "incorrect_answers")
public class IncorrectAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String incorrectAnswer;
}