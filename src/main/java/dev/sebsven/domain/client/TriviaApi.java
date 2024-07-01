package dev.sebsven.domain.client;

import dev.sebsven.domain.response.TriviaResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@AllArgsConstructor
public class TriviaApi {

    private final RestClient triviaClient;

    public TriviaResponse getTriviaFromExternalApi(String amount, String category, String difficulty) {
        return triviaClient
                .get()
                .uri(buildUri -> buildUri
                        .queryParam("amount", amount)
                        .queryParam("category", category)
                        .queryParam("difficulty", difficulty)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(TriviaResponse.class);
    }
}
