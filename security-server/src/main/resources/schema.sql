CREATE TABLE user (
    id VARCHAR(30) PRIMARY KEY,
    name VARCHAR(30),
    password VARCHAR(MAX),
    create_time TIMESTAMP,
    status BOOLEAN
);

CREATE TABLE menu (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    pattern VARCHAR(30)
);

CREATE TABLE menu_role (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    mid INT NOT NULL,
    rid INT NOT NULL
);

CREATE TABLE role (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(30) NOT NULL,
    name_zh VARCHAR(50) NOT NULL
);

CREATE TABLE user_role (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    uid VARCHAR(30) NOT NULL,
    rid INT NOT NULL
);