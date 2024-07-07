CREATE TABLE IF NOT EXISTS `history`
(
    `id`            INT AUTO_INCREMENT,
    `description`   VARCHAR(255),
    `date_and_time` TIMESTAMP,
    `document`      INT,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`document`) REFERENCES `documents`(`id`)
    );