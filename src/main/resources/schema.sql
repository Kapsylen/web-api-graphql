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
    incorrect_answer VARCHAR(255)
);