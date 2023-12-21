CREATE DATABASE mtcg_db;
/*
    DROP DATABASE mtcg_db;
    DROP TABLE users;


*/

CREATE TABLE IF NOT EXISTS users (
    uuid VARCHAR(255) PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    bio VARCHAR(255),
    image VARCHAR(255),
    elo INT NOT NULL,
    coins INT NOT NULL
    );

CREATE TABLE IF NOT EXISTS cards (
    carduid VARCHAR(255) PRIMARY KEY,
    cardname VARCHAR(255) NOT NULL,
    damage FLOAT NOT NULL,
    owner VARCHAR(255),
    packageid VARCHAR(255),
    CONSTRAINT fk_packages
        FOREIGN KEY (packageid)
        REFERENCES packages(packageuid)
);

CREATE TABLE IF NOT EXISTS packages (
    packageuid VARCHAR(255) PRIMARY KEY,
    bought boolean NOT NULL
);

CREATE TABLE IF NOT EXISTS battles (
    battleuid VARCHAR(255) PRIMARY KEY,
    user1 VARCHAR(255) NOT NULL,
    user2 VARCHAR(255),
    winner VARCHAR(255),
    loser VARCHAR(255),
    log VARCHAR(255),
    CONSTRAINT fk_user
        FOREIGN KEY (user1, user2)
        REFERENCES users(uuid)
);

CREATE TABLE IF NOT EXISTS transactions (
    transactionid VARCHAR(255) PRIMARY KEY,
    buyer VARCHAR(255) NOT NULL,
    packageid VARCHAR(255) NOT NULL,
    completed boolean NOT NULL,
    CONSTRAINT fk_user
        FOREIGN KEY (buyer)
        REFERENCES users(uuid),
    CONSTRAINT fk_package
        FOREIGN KEY (packageid)
        REFERENCES packages(packageuid)
);


