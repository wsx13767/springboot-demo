CREATE TABLE IF NOT EXISTS taco (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
   ingredient_list json default '[]'
);