CREATE TABLE user (
    id VARCHAR(30) PRIMARY KEY,
    name VARCHAR(30),
    password VARCHAR(30),
    create_time TIMESTAMP,
    status BOOLEAN
);