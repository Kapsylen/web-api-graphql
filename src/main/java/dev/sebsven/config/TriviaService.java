package dev.sebsven.config;

import dev.sebsven.application.response.TriviaResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class TriviaService {

    private final RestClient.Builder triviaClientBuilder;

    public TriviaService(RestClient.Builder triviaClientBuilder) {
        this.triviaClientBuilder = triviaClientBuilder;
    }

    public TriviaResponse getAll() {
        return triviaClientBuilder
                .baseUrl("https://opentdb.com/api.php?amount=10&category=17&difficulty=easy")
                .build()
                .get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(TriviaResponse.class);
    }
}
