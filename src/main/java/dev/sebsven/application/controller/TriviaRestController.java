package dev.sebsven.application.controller;

import dev.sebsven.application.request.TriviaInputApi;
import dev.sebsven.application.response.TriviaOutputApi;
import dev.sebsven.domain.TriviaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api")
@AllArgsConstructor
public class TriviaRestController {

    private final TriviaService triviaService;

    @GetMapping("/trivia/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public TriviaOutputApi getTriviaById(@PathVariable Integer id) {
        return triviaService.triviaById(id);
    }


    @PostMapping("/trivia")
    @ResponseStatus(value = HttpStatus.CREATED)
    public TriviaOutputApi saveTrivia(@RequestBody TriviaInputApi triviaInputApi) {
        return triviaService.create(triviaInputApi);
    }

    @GetMapping("/trivias")
    @ResponseStatus(value = HttpStatus.OK)
    public List<TriviaOutputApi> getAllTrivia(@RequestParam(required = false) String category) {
        return triviaService.getAllTrivia(category);
    }

    @DeleteMapping("/trivia/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteTrivia(@PathVariable Integer id) {
        triviaService.delete(id);
    }

    @PutMapping("/trivia/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public TriviaOutputApi updateTrivia(@PathVariable Integer id, @RequestBody TriviaInputApi triviaInputApi) {
        return triviaService.update(id, triviaInputApi);
    }
}
