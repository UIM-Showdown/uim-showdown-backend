CREATE TABLE `challenges` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `description` VARCHAR(512) NOT NULL,
    `name` VARCHAR(64) NOT NULL,
    `team_size` INT NOT NULL,
    `type` ENUM('RELAY', 'SPEEDRUN') NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT name_unique_idx UNIQUE (`name`)
);
