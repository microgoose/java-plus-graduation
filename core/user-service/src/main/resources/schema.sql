CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT       NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name  VARCHAR(250) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);