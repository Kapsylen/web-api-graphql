package dev.sebsven.infrastructure;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name = "incorrect_answers")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncorrectAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String incorrectAnswer;
}