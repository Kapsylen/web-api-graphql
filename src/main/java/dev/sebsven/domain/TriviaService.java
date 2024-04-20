package dev.sebsven.domain;

import dev.sebsven.application.response.TriviaResponse;
import dev.sebsven.infrastructure.TriviaEntity;
import dev.sebsven.infrastructure.TriviaRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class TriviaService {

    private final RestClient.Builder triviaClientBuilder;
    private final TriviaRepository triviaRepository;;

    public TriviaService(RestClient.Builder triviaClientBuilder, TriviaRepository triviaRepository) {
        this.triviaClientBuilder = triviaClientBuilder;
        this.triviaRepository = triviaRepository;
    }

    private TriviaResponse getAll() {
        return triviaClientBuilder
                .baseUrl("https://opentdb.com/api.php?amount=10&category=17&difficulty=easy")
                .build()
                .get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(TriviaResponse.class);
    }

    public void saveAll() {
        var response = getAll();
        var triviaEntities = response.
                results().stream().map(
                TriviaEntity::toTriviaEntity
        ).toList();
        triviaRepository.saveAll(triviaEntities);
    }
}
