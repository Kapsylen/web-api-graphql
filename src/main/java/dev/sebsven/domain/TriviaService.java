package dev.sebsven.domain;

import dev.sebsven.application.request.TriviaInputApi;
import dev.sebsven.application.response.TriviaOutputApi;
import dev.sebsven.domain.response.TriviaResponse;
import dev.sebsven.infrastructure.IncorrectAnswer;
import dev.sebsven.infrastructure.entity.Trivia;
import dev.sebsven.infrastructure.TriviaRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

import static dev.sebsven.application.response.TriviaOutputApi.toTriviaApi;
import static dev.sebsven.infrastructure.entity.Trivia.toTrivia;

@Service
public class TriviaService {

    private final RestClient.Builder triviaClientBuilder;
    private final TriviaRepository triviaRepository;;

    public TriviaService(RestClient.Builder triviaClientBuilder, TriviaRepository triviaRepository) {
        this.triviaClientBuilder = triviaClientBuilder;
        this.triviaRepository = triviaRepository;
    }

    private TriviaResponse getTriviaFromExternalApi(String category) {
        return triviaClientBuilder
                .baseUrl("https://opentdb.com/api.php?amount=10&category=" + category + "&difficulty=easy")
                .build()
                .get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(TriviaResponse.class);
    }

    public void saveAll(String Category) {
        if(triviaRepository.findAll().isEmpty()){
            var triviaEntities = getTriviaFromExternalApi(Category).results().stream().map(Trivia::toTrivia).toList();
            triviaRepository.saveAll(triviaEntities);
        }
    }

    public TriviaOutputApi triviaById(Integer id) {
        return toTriviaApi(triviaRepository.findById(id)
                .orElseThrow(RuntimeException::new));
    }

    public List<TriviaOutputApi> getByType(String type) {
        return triviaRepository.findByType(type)
                .stream()
                .map(TriviaOutputApi::toTriviaApi)
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

    public List<TriviaOutputApi> getAllTrivia(String category) {
        List<Trivia> triviaList = category != null ?
                triviaRepository.findByCategory(category) : triviaRepository.findAll();

        if (category != null && triviaList.isEmpty()) {
            return triviaRepository.saveAll(
                            getTriviaFromExternalApi(category)
                                    .results()
                                    .stream()
                                    .map(Trivia::toTrivia)
                                    .toList())
                    .stream()
                    .map(TriviaOutputApi::toTriviaApi)
                    .toList();
        }

        return triviaList.stream()
                .map(TriviaOutputApi::toTriviaApi)
                .toList();
    }

    public TriviaOutputApi create(TriviaInputApi triviaInputApi) {
        return toTriviaApi(triviaRepository.save(toTrivia(triviaInputApi)));
    }

    public void delete(Integer id) {
        triviaRepository.deleteById(id);
    }

    public TriviaOutputApi update(Integer id, TriviaInputApi triviaInputApi) {
        Trivia trivia = triviaRepository.findById(id)
                .orElseThrow(RuntimeException::new);
        trivia.setType(triviaInputApi.type());
        trivia.setDifficulty(triviaInputApi.difficulty());
        trivia.setCategory(triviaInputApi.category());
        trivia.setQuestion(triviaInputApi.question());
        trivia.setCorrectAnswer(triviaInputApi.correctAnswer());
        trivia.getIncorrectAnswers().stream().map((incorrectAnswer) -> {
         return  triviaInputApi.incorrectAnswers().stream().filter(incorrectAnswer::equals).findFirst().orElse(incorrectAnswer.getIncorrectAnswer());
        }).toList();
        return toTriviaApi(triviaRepository.save(trivia));
    }
}
