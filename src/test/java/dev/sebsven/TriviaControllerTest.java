package dev.sebsven;

import dev.sebsven.application.controller.TriviaController;
import dev.sebsven.application.response.TriviaApi;
import dev.sebsven.domain.TriviaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@GraphQlTest(TriviaController.class)
@AutoConfigureGraphQlTester
public class TriviaControllerTest {

    @Autowired
    private GraphQlTester tester;
    @MockBean
    private TriviaService service;

    @Test
    void findById() {
        var expectedTrivia = Optional.of(TriviaApi.builder()
                .id(1)
                .category("Science &amp; Nature")
                .type("multiple")
                .build());

        Mockito.when(service.triviaById(Mockito.any())).thenReturn(expectedTrivia);

        String query = "{triviaById(id: 1) {id category type}}";
        TriviaApi trivia = tester.document(query)
                .execute()
                .path("triviaById")
                .entity(TriviaApi.class)
                .get();

        assertAll(
                () -> assertNotNull(trivia),
                () -> assertEquals(expectedTrivia.get().id(), trivia.id()),
                () -> assertEquals(expectedTrivia.get().category(), trivia.category()),
                () -> assertEquals(expectedTrivia.get().type(), trivia.type())
        );
    }

    @Test
    void findAll() {
        var expectedTriviaList = Arrays.asList(
                TriviaApi.builder()
                        .id(1)
                        .category("Science &amp; Nature")
                        .type("multiple")
                        .build(),
                TriviaApi.builder()
                        .id(2)
                        .category("History")
                        .type("boolean")
                        .build()
        );

        Mockito.when(service.getAllTrivia()).thenReturn(expectedTriviaList);

        String query = "{allTrivia {id category type}}";
        List<TriviaApi> triviaList = tester.document(query)
                .execute()
                .path("allTrivia")
                .entityList(TriviaApi.class).get().stream()
                .toList();

        assertAll(
                () -> assertNotNull(triviaList),
                () -> assertEquals(expectedTriviaList.size(), triviaList.size()),
                () -> assertEquals(expectedTriviaList.get(0).id(), triviaList.get(0).id()),
                () -> assertEquals(expectedTriviaList.get(0).category(), triviaList.get(0).category()),
                () -> assertEquals(expectedTriviaList.get(0).type(), triviaList.get(0).type()),
                () -> assertEquals(expectedTriviaList.get(1).id(), triviaList.get(1).id()),
                () -> assertEquals(expectedTriviaList.get(1).category(), triviaList.get(1).category()),
                () -> assertEquals(expectedTriviaList.get(1).type(), triviaList.get(1).type()));
    }

    @Test
    void triviaByType() {
        var expectedTriviaList = Arrays.asList(
                TriviaApi.builder()
                        .id(1)
                        .category("Science &amp; Nature")
                        .type("multiple")
                        .build(),
                TriviaApi.builder()
                        .id(2)
                        .category("History")
                        .type("boolean")
                        .build()
        );

        Mockito.when(service.getByType(Mockito.any())).thenReturn(expectedTriviaList);

        String query = "{triviaByType(type: \"multiple\") {id category type}}";
        List<TriviaApi> triviaList = tester.document(query)
                .execute()
                .path("triviaByType")
                .entityList(TriviaApi.class).get().stream()
                .toList();

        assertAll(
                () -> assertNotNull(triviaList),
                () -> assertEquals(expectedTriviaList.size(), triviaList.size()),
                () -> assertEquals(expectedTriviaList.get(0).id(), triviaList.get(0).id()),
                () -> assertEquals(expectedTriviaList.get(0).category(), triviaList.get(0).category()),
                () -> assertEquals(expectedTriviaList.get(0).type(), triviaList.get(0).type()),
                () -> assertEquals(expectedTriviaList.get(1).id(), triviaList.get(1).id()),
                () -> assertEquals(expectedTriviaList.get(1).category(), triviaList.get(1).category()),
                () -> assertEquals(expectedTriviaList.get(1).type(), triviaList.get(1).type()));
    }

    @Test
    void newTrivia() {
        TriviaApi trivia = TriviaApi.builder()
                .id(1)
                .type("multiple")
                .category("Science &amp; Nature")
                .difficulty("medium")
                .question("How many planets are in our Solar System?")
                .build();
        Mockito.when(service.save(Mockito.any())).thenReturn(trivia);

        String mutation = "mutation {newTrivia(triviaInputApi: {type: \"multiple\", difficulty: \"medium\", category: \"Science &amp; Nature\", question: \"How many planets are in our Solar System?\"}) {type difficulty category question}}";

        TriviaApi savedTrivia = tester.document(mutation)
                .execute()
                .path("newTrivia")
                .entity(TriviaApi.class)
                .get();

        assertAll(
                () -> assertNotNull(savedTrivia),
                () -> assertEquals(trivia.category(), savedTrivia.category()),
                () -> assertEquals(trivia.type(), savedTrivia.type()),
                () -> assertEquals(trivia.difficulty(), savedTrivia.difficulty()),
                () -> assertEquals(trivia.question(), savedTrivia.question())
        );
    }
}
