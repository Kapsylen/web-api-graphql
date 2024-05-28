package dev.sebsven.infrastructure;


import dev.sebsven.infrastructure.entity.Trivia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TriviaRepository extends JpaRepository<Trivia, Integer> {
    List<Trivia> findByType(String type);
    List<Trivia> findByCategoryId(String category);
}
