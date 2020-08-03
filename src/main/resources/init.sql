CREATE DATABASE DuoDuoPin;
USE DuoDuoPin;
CREATE TABLE `User`
(
    id         INT UNSIGNED AUTO_INCREMENT,
    username   VARCHAR(20) UNIQUE NOT NULL,
    nickname   VARCHAR(20)        NOT NULL,
    `password` VARCHAR(20)        NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO `User`(username, nickname, `password`)
VALUES ('admin', 'admin', 'password');