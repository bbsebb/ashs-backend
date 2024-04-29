CREATE TABLE access_token (
    id VARCHAR(255) NOT NULL,
    access_token VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    token_type VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);