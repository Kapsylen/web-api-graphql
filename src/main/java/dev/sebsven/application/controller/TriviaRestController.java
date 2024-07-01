package dev.sebsven.application.controller;

import dev.sebsven.application.request.TriviaInputApi;
import dev.sebsven.application.response.TriviaOutputApi;
import dev.sebsven.domain.TriviaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api")
@AllArgsConstructor
public class TriviaRestController {

    private final TriviaService triviaService;

    @Operation(summary = "Get a Trivia by id", description = "Returns a Trivia as per the id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "No trivia could be found")
    })
    @GetMapping("/trivia/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public TriviaOutputApi getTriviaById(@PathVariable Integer id) {
        return triviaService.triviaById(id);
    }


    @Operation(summary = "Create a trivia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TriviaOutputApi.class)) }),
            @ApiResponse(responseCode = "400", description = "Name can not be empty",
                    content = @Content) })
    @PostMapping("/trivia")
    @ResponseStatus(value = HttpStatus.CREATED)
    public TriviaOutputApi saveTrivia(@Valid @RequestBody TriviaInputApi triviaInputApi) {
        return triviaService.create(triviaInputApi);
    }

    @Operation(summary = "Get all trivias", description = "Returns all trivias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/trivias")
    @ResponseStatus(value = HttpStatus.OK)
    public List<TriviaOutputApi> getAllTrivia(
            @RequestParam String amount,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String difficulty

    ) {
        return triviaService.getAllTrivia(amount, category, difficulty);
    }

    @Operation(summary = "Delete a Trivia", description = "Delete a Trivia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - No Trivia could be found")
    })
    @DeleteMapping("/trivia/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteTrivia(@PathVariable Integer id) {
        triviaService.delete(id);
    }

    @Operation(summary = "Update a Trivia", description = "Update a Trivia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated"),
            @ApiResponse(responseCode = "404", description = "Not found - No Trivia could be found")
    })
    @PutMapping("/trivia/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public TriviaOutputApi updateTrivia(@PathVariable Integer id, @RequestBody TriviaInputApi triviaInputApi) {
        return triviaService.update(id, triviaInputApi);
    }
}
