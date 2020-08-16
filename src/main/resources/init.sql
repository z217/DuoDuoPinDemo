CREATE DATABASE DuoDuoPin;
USE DuoDuoPin;
CREATE TABLE `user`
(
    id         INT UNSIGNED AUTO_INCREMENT,
    username   VARCHAR(20) UNIQUE NOT NULL,
    nickname   VARCHAR(20)        NOT NULL,
    `password` VARCHAR(20)        NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO `user`(username, nickname, `password`)
VALUES ('admin', 'admin', 'password');

create table share_bill
(
    bill_id     INT UNSIGNED AUTO_INCREMENT,
    user_id     INT UNSIGNED           NOT NULL,
    type        CHAR(10)               NOT NULL,
    description VARCHAR(50)            NOT NULL,
    address     VARCHAR(50)            NOT NULL,
    time        TIMESTAMP              NOT NULL,
    cur_people  INT UNSIGNED DEFAULT 1 NOT NULL,
    max_people  INT UNSIGNED           NOT NULL,
    price       DECIMAL(7, 2)          NOT NULL,
    longitude   DOUBLE                 NOT NULL,
    latitude    DOUBLE                 NOT NULL,
    geohash     CHAR(11)               NOT NULL,
    PRIMARY KEY (bill_id),
    FOREIGN KEY (user_id) REFERENCES `user` (user_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);
