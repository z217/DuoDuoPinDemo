CREATE DATABASE IF NOT EXISTS DuoDuoPin;
USE DuoDuoPin;

DROP TABLE IF EXISTS `team_member`;
DROP TABLE IF EXISTS `chat_message`;
DROP TABLE IF EXISTS `system_message`;
DROP TABLE IF EXISTS `share_bill`;
DROP TABLE IF EXISTS `user`;

CREATE TABLE `user`
(
    `user_id`     INT UNSIGNED AUTO_INCREMENT,
    `username`    VARCHAR(20) UNIQUE NOT NULL,
    `nickname`    VARCHAR(20)        NOT NULL,
    `password`    VARCHAR(20)        NOT NULL,
    `last_online` TIMESTAMP          NULL,
    PRIMARY KEY (`user_id`)
);

CREATE TABLE share_bill
(
    `bill_id`     INT UNSIGNED AUTO_INCREMENT,
    `user_id`     INT UNSIGNED           NOT NULL,
    `title`       CHAR(40)               NOT NULL,
    `type`        CHAR(4)                NOT NULL,
    `description` VARCHAR(255)           NOT NULL,
    `address`     VARCHAR(255)           NOT NULL,
    `time`        TIMESTAMP              NOT NULL,
    `cur_people`  INT UNSIGNED DEFAULT 1 NOT NULL,
    `max_people`  INT UNSIGNED           NOT NULL,
    `price`       DECIMAL(7, 2)          NOT NULL,
    `longitude`   DOUBLE                 NOT NULL,
    `latitude`    DOUBLE                 NOT NULL,
    `geohash`     CHAR(11)               NOT NULL,
    PRIMARY KEY (`bill_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
        ON DELETE CASCADE ON UPDATE CASCADE,
    KEY (`user_id`),
    fulltext (`description`, `address`),
    CHECK ( `cur_people` > 0 AND `cur_people` <= `max_people` )
);

CREATE TABLE team_member
(
    `bill_id` INT UNSIGNED NOT NULL,
    `user_id` INT UNSIGNED NOT NULL,
    PRIMARY KEY (`bill_id`, `user_id`),
    FOREIGN KEY (`bill_id`) REFERENCES share_bill (`bill_id`)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
        ON DELETE CASCADE ON UPDATE CASCADE,
    KEY (`user_id`)
);

CREATE TABLE chat_message
(
    `user_id` INT UNSIGNED NOT NULL,
    `bill_id` INT UNSIGNED NOT NULL,
    `type`    CHAR(4)      NOT NULL,
    `time`    TIMESTAMP    NOT NULL,
    content   VARCHAR(255) NOT NULL,
    PRIMARY KEY (`user_id`, `bill_id`, `time`),
    FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`bill_id`) REFERENCES share_bill (`bill_id`)
        ON DELETE CASCADE ON UPDATE CASCADE,
    KEY (`bill_id`)
);

CREATE TABLE system_message
(
    `message_id`  INT UNSIGNED AUTO_INCREMENT,
    `sender_id`   INT UNSIGNED NULL,
    `receiver_id` INT UNSIGNED NULL,
    `bill_id`     INT UNSIGNED NULL,
    `type`        CHAR(5)      NOT NULL,
    `time`        TIMESTAMP    NOT NULL,
    `content`     VARCHAR(255) NOT NULL,
    PRIMARY KEY (`message_id`),
    FOREIGN KEY (`sender_id`) REFERENCES `user` (`user_id`)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`receiver_id`) REFERENCES `user` (`user_id`)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`bill_id`) REFERENCES `share_bill` (`bill_id`)
        ON DELETE CASCADE ON UPDATE CASCADE,
    KEY (`time`, `receiver_id`),
    KEY (`sender_id`, `bill_id`)
);

INSERT INTO `user` (`username`, `nickname`, `password`)
VALUES ('admin', 'admin', 'password'),
       ('z217', 'z217', '123456');

INSERT INTO share_bill (`user_id`, `title`, `type`, `description`, `address`, `time`, `cur_people`, `max_people`,
                        `price`, `longitude`,
                        `latitude`, `geohash`)
VALUES (1, '首都机场T2拼车', 'CAR', '1月1日早上首都机场T2航站楼拼车，11点的飞机，8点半以前出发都可以', '北京邮电大学西门', '2021-01-01 16:00:00', 2, 3,
        23.00, 32, 20, 'sewcb0vsqn1');

INSERT INTO team_member (`bill_id`, `user_id`)
VALUES (1, 1);
