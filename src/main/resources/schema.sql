CREATE TABLE trivia
(
    id            SERIAL PRIMARY KEY,
    type          VARCHAR(255),
    difficulty    VARCHAR(255),
    category      VARCHAR(255),
    question      VARCHAR(255),
    correctAnswer VARCHAR(255)
);

CREATE TABLE incorrect_answers
(
    id               SERIAL PRIMARY KEY,
    trivia_id        INTEGER,
    incorrect_answer VARCHAR(255),
    FOREIGN KEY (trivia_id) REFERENCES trivia (id)
);