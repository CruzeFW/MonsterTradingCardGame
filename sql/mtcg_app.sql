CREATE DATABASE mtcg_db;
/*
    DROP DATABASE mtcg_db;
*/
    DROP TABLE packages CASCADE ;
    DROP TABLE users CASCADE;
    DROP TABLE cards CASCADE;
    DROP TABLE battles;
    DROP TABLE transactions;



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
    deckid INT,
    CONSTRAINT fk_packages
        FOREIGN KEY (packageid)
        REFERENCES packages(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_users
        FOREIGN KEY (owner)
        REFERENCES users(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS battles (
    id SERIAL PRIMARY KEY,
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
        FOREIGN KEY (user2)
        REFERENCES users(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_user3
        FOREIGN KEY (winner)
            REFERENCES users(id)
            ON DELETE CASCADE,
    CONSTRAINT fk_user4
        FOREIGN KEY (loser)
            REFERENCES users(id)
            ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS transactions (
    id SERIAL PRIMARY KEY,
    buyer VARCHAR(255),
    packageid VARCHAR(255),
    CONSTRAINT fk_user
        FOREIGN KEY (buyer)
        REFERENCES users(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_package
        FOREIGN KEY (packageid)
        REFERENCES packages(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
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

INSERT INTO battles (user1, user2, winner, loser) VALUES ('25e2906a-ee48-4009-991f-9e3c458204e7', '7378f36c-ef43-4653-afa3-ce60201a2990', '25e2906a-ee48-4009-991f-9e3c458204e7', '7378f36c-ef43-4653-afa3-ce60201a2990');
INSERT INTO battles (user1, user2, winner, loser) VALUES ('5146fe1c-6e8c-4b6c-aa6d-df06bd0fe14d', '7378f36c-ef43-4653-afa3-ce60201a2990', '25e2906a-ee48-4009-991f-9e3c458204e7', '5146fe1c-6e8c-4b6c-aa6d-df06bd0fe14d');
INSERT INTO battles (user1, user2, winner, loser) VALUES ('5146fe1c-6e8c-4b6c-aa6d-df06bd0fe14d', '7378f36c-ef43-4653-afa3-ce60201a2990', '5146fe1c-6e8c-4b6c-aa6d-df06bd0fe14d', '25e2906a-ee48-4009-991f-9e3c458204e7')