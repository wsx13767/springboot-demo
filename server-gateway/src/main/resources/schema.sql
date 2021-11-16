CREATE TABLE route_group (
    id VARCHAR(30) PRIMARY KEY,
    status BOOLEAN,
    description VARCHAR(255)
);

CREATE TABLE api_server (
    id VARCHAR(50) PRIMARY KEY,
    group_id VARCHAR(30),
    name VARCHAR(50),
    uri VARCHAR(255),
    status BOOLEAN,
    description VARCHAR(255)
);

CREATE TABLE api_route (
    id INT PRIMARY KEY AUTO_INCREMENT,
    server_id VARCHAR(50),
    path VARCHAR(255),
    before timestamp,
    after timestamp,
    status BOOLEAN,
    description VARCHAR(255)
);