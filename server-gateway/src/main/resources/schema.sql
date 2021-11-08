CREATE TABLE api_router (
    id SERIAL PRIMARY KEY,
    uri VARCHAR(255),
    host_name VARCHAR(255),
    api_id VARCHAR(255),
    path VARCHAR(255)
);