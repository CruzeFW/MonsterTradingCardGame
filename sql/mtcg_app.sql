CREATE DATABASE mtcg_db;
/*
    DROP DATABASE mtcg_db;
    DROP TABLE users;
    DROP TABLE packages;
    DROP TABLE cards;
    DROP TABLE battles;
    DROP TABLE transactions;
*/


CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(255) PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    token VARCHAR(255),
    bio VARCHAR(255),
    image VARCHAR(255),
    elo INT NOT NULL,
    coins INT NOT NULL
);


CREATE TABLE IF NOT EXISTS packages (
    id VARCHAR(255) PRIMARY KEY,
    bought boolean NOT NULL
);

CREATE TABLE IF NOT EXISTS cards (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    damage FLOAT NOT NULL,
    owner VARCHAR(255),
    packageid VARCHAR(255),
    CONSTRAINT fk_packages
        FOREIGN KEY (packageid)
        REFERENCES packages(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS battles (
    id VARCHAR(255) PRIMARY KEY,
    user1 VARCHAR(255) NOT NULL,
    user2 VARCHAR(255),
    winner VARCHAR(255),
    loser VARCHAR(255),
    log VARCHAR(255),
    CONSTRAINT fk_user1
        FOREIGN KEY (user1)
        REFERENCES users(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_user2
        FOREIGN KEY (user1)
        REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS transactions (
    id VARCHAR(255) PRIMARY KEY,
    buyer VARCHAR(255) NOT NULL,
    packageid VARCHAR(255) NOT NULL,
    completed boolean NOT NULL,
    CONSTRAINT fk_user
        FOREIGN KEY (buyer)
        REFERENCES users(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_package
        FOREIGN KEY (packageid)
        REFERENCES packages(id)
        ON DELETE CASCADE
);




SELECT * FROM users;

DELETE FROM users WHERE username ='kienboec';
DELETE FROM users WHERE username ='altenhof';
DELETE FROM users WHERE username ='admin';

UPDATE users SET token = 'roof' WHERE username = 'hans';

UPDATE users SET token = NULL;

DELETE FROM users;
DELETE FROM cards;
DELETE FROM packages;