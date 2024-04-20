package dev.sebsven;

import dev.sebsven.domain.TriviaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WebApiGraphqlApplication {

    private final TriviaService triviaService;

    public WebApiGraphqlApplication(TriviaService triviaService) {
        this.triviaService = triviaService;
    }

    public static void main(String[] args) {
        SpringApplication.run(WebApiGraphqlApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            triviaService.saveAll();

        };
    }

}
