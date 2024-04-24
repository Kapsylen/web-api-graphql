package dev.sebsven.domain;

import dev.sebsven.application.response.TriviaApi;
import dev.sebsven.domain.response.TriviaResponse;
import dev.sebsven.infrastructure.IncorrectAnswer;
import dev.sebsven.infrastructure.entity.Trivia;
import dev.sebsven.infrastructure.TriviaRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

import static dev.sebsven.application.response.TriviaApi.toTriviaApi;

@Service
public class TriviaService {

    private final RestClient.Builder triviaClientBuilder;
    private final TriviaRepository triviaRepository;;

    public TriviaService(RestClient.Builder triviaClientBuilder, TriviaRepository triviaRepository) {
        this.triviaClientBuilder = triviaClientBuilder;
        this.triviaRepository = triviaRepository;
    }

    private TriviaResponse getTriviaFromExternalApi() {
        return triviaClientBuilder
                .baseUrl("https://opentdb.com/api.php?amount=10&category=17&difficulty=easy")
                .build()
                .get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(TriviaResponse.class);
    }

    public void saveAll() {
        if(triviaRepository.findAll().isEmpty()){
            var triviaEntities = getTriviaFromExternalApi().results().stream().map(Trivia::toTrivia).toList();
            triviaRepository.saveAll(triviaEntities);
        }
    }

    public Optional<TriviaApi> triviaById(Integer id) {
        return Optional.of(toTriviaApi(triviaRepository.findById(id)
                .orElseThrow(RuntimeException::new)));
    }

    public List<TriviaApi> getByType(String type) {
        return triviaRepository.findByType(type)
                .stream()
                .map(TriviaApi::toTriviaApi)
                .toList();
    }

    public List<String> getIncorrectAnswers(Integer id) {
        var trivia = triviaRepository.findById(id);
        return trivia.orElseThrow(RuntimeException::new)
                .getIncorrectAnswers()
                .stream()
                .map(IncorrectAnswer::getIncorrectAnswer)
                .toList();
    }

    public List<TriviaApi> getAllTrivia() {
        return triviaRepository.findAll()
                .stream()
                .map(TriviaApi::toTriviaApi)
                .toList();
    }
}
