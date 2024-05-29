package dev.sebsven.domain;

import dev.sebsven.application.request.TriviaInputApi;
import dev.sebsven.application.response.TriviaOutputApi;
import dev.sebsven.domain.client.TriviaApi;
import dev.sebsven.infrastructure.IncorrectAnswer;
import dev.sebsven.infrastructure.TriviaRepository;
import dev.sebsven.infrastructure.entity.Trivia;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static dev.sebsven.application.response.TriviaOutputApi.toTriviaApi;
import static dev.sebsven.infrastructure.entity.Trivia.toTrivia;

@Service
@AllArgsConstructor
public class TriviaService {

    private final TriviaRepository triviaRepository;
    private final TriviaApi triviaApi;

    public void saveAll(String amount, String categoryId, String difficulty) {
        if(triviaRepository.findAll().isEmpty()){
            var triviaEntities = this.triviaApi.getTriviaFromExternalApi(amount, categoryId, difficulty)
                    .results()
                    .stream()
                    .map(t -> toTrivia(t, categoryId, difficulty))
                    .toList();

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

    public List<TriviaOutputApi> getAllTrivia(String amount, String categoryId, String difficulty) {
        List<Trivia> triviaList = fetchTriviaFromDb(categoryId, difficulty);

        if (categoryId != null && triviaList.isEmpty()){
           return fetchAndSaveTrivia(amount, categoryId, difficulty, triviaList);
        } else  {
            return triviaList.stream()
                    .map(TriviaOutputApi::toTriviaApi)
                    .toList();
        }
    }

    public TriviaOutputApi create(TriviaInputApi triviaInputApi) {
        return toTriviaApi(triviaRepository.save(toTrivia(triviaInputApi)));
    }

    public void delete(Integer id) {
        Trivia trivia = triviaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trivia not found"));
        triviaRepository.delete(trivia);
    }

    public TriviaOutputApi update(Integer id, TriviaInputApi triviaInputApi) {
        Trivia trivia = triviaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trivia not found"));
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

    private List<TriviaOutputApi> fetchAndSaveTrivia(String amount, String categoryId, String difficulty, List<Trivia> triviaList) {
        if (categoryId != null && triviaList.isEmpty()) {
            return triviaRepository.saveAll(
                            this.triviaApi.getTriviaFromExternalApi(amount, categoryId, difficulty)
                                    .results()
                                    .stream()
                                    .map(t -> toTrivia(t, categoryId, difficulty))
                                    .toList())
                    .stream()
                    .map(TriviaOutputApi::toTriviaApi)
                    .toList();
        }
        return List.of();
    }

    private List<Trivia> fetchTriviaFromDb(String categoryId, String difficulty) {

        if(categoryId != null &&  difficulty != null){
            return triviaRepository.findByDifficultyAndCategory(difficulty, categoryId);
        }
        if(categoryId != null && !categoryId.isEmpty()){
            return triviaRepository.findByCategoryId(categoryId);
        } else if(difficulty != null && !difficulty.isEmpty()){
            return triviaRepository.findByDifficulty(difficulty);
        } else if(difficulty == null && categoryId == null) {
            return triviaRepository.findAll();
        }

        return List.of();
    }
}
