package dev.sebsven.application.controller;

import dev.sebsven.application.response.TriviaOutputApi;
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

import static org.junit.jupiter.api.Assertions.*;

@GraphQlTest(TriviaGraphQLController.class)
@AutoConfigureGraphQlTester
public class TriviaGraphQLControllerTest {

    @Autowired
    private GraphQlTester tester;
    @MockBean
    private TriviaService service;

    @Test
    void findById() {
        var expectedTrivia = TriviaOutputApi.builder()
                .id(1)
                .category("Science &amp; Nature")
                .type("multiple")
                .build();

        Mockito.when(service.triviaById(Mockito.any())).thenReturn(expectedTrivia);

        String query = "{triviaById(id: 1) {id category type}}";
        TriviaOutputApi trivia = tester.document(query)
                .execute()
                .path("triviaById")
                .entity(TriviaOutputApi.class)
                .get();

        assertAll(
                () -> assertNotNull(trivia),
                () -> assertEquals(expectedTrivia.id(), trivia.id()),
                () -> assertEquals(expectedTrivia.category(), trivia.category()),
                () -> assertEquals(expectedTrivia.type(), trivia.type())
        );
    }

    @Test
    void findAll() {
        var expectedTriviaList = Arrays.asList(
                TriviaOutputApi.builder()
                        .id(1)
                        .category("Science &amp; Nature")
                        .type("multiple")
                        .build(),
                TriviaOutputApi.builder()
                        .id(2)
                        .category("History")
                        .type("boolean")
                        .build()
        );

        Mockito.when(service.getAllTrivia(null, null)).thenReturn(expectedTriviaList);

        String query = "{allTrivia {id category type}}";
        List<TriviaOutputApi> triviaList = tester.document(query)
                .execute()
                .path("allTrivia")
                .entityList(TriviaOutputApi.class).get().stream()
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
                TriviaOutputApi.builder()
                        .id(1)
                        .category("Science &amp; Nature")
                        .type("multiple")
                        .build(),
                TriviaOutputApi.builder()
                        .id(2)
                        .category("History")
                        .type("boolean")
                        .build()
        );

        Mockito.when(service.getByType(Mockito.any())).thenReturn(expectedTriviaList);

        String query = "{triviaByType(type: \"multiple\") {id category type}}";
        List<TriviaOutputApi> triviaList = tester.document(query)
                .execute()
                .path("triviaByType")
                .entityList(TriviaOutputApi.class).get().stream()
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
        TriviaOutputApi trivia = TriviaOutputApi.builder()
                .id(1)
                .type("multiple")
                .category("Science &amp; Nature")
                .difficulty("medium")
                .question("How many planets are in our Solar System?")
                .build();
        Mockito.when(service.create(Mockito.any())).thenReturn(trivia);

        String mutation = "mutation {newTrivia(triviaInputApi: {type: \"multiple\", difficulty: \"medium\", category: \"Science &amp; Nature\", question: \"How many planets are in our Solar System?\"}) {id, type difficulty category question}}";

        TriviaOutputApi savedTrivia = tester.document(mutation)
                .execute()
                .path("newTrivia")
                .entity(TriviaOutputApi.class)
                .get();

        assertAll(
                () -> assertNotNull(savedTrivia),
                () -> assertEquals(savedTrivia.id(), trivia.id()),
                () -> assertEquals(trivia.category(), savedTrivia.category()),
                () -> assertEquals(trivia.type(), savedTrivia.type()),
                () -> assertEquals(trivia.difficulty(), savedTrivia.difficulty()),
                () -> assertEquals(trivia.question(), savedTrivia.question())
        );
    }
}
