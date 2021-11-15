CREATE TABLE api_route (
    id INT PRIMARY KEY AUTO_INCREMENT,
    uri VARCHAR(255),
    host_name VARCHAR(255),
    api_id VARCHAR(255),
    path VARCHAR(255),
    before timestamp,
    after timestamp,
    status BOOLEAN
);