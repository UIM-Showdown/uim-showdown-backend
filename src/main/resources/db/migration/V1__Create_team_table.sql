CREATE TABLE `teams` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(128) NOT NULL,
    `abbreviation` VARCHAR(8) NOT NULL,
    `color` CHAR(6) NOT NULL,
    PRIMARY KEY (id),
    INDEX `name_idx` (`name`)
);