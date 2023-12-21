CREATE DATABASE mtcg_db;
/*DROP DATABASE mtcg_db; */


CREATE TABLE IF NOT EXISTS user (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    done BOOLEAN
    );

DROP TABLE task;