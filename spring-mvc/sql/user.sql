CREATE TABLE app_user (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(32) UNIQUE,
    email VARCHAR(100) UNIQUE
);

INSERT INTO app_user (username, email) VALUES ('test_user1', 'test_user1@example.com');