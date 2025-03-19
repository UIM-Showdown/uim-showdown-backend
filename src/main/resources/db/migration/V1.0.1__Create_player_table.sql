CREATE TABLE `players` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `rsn` VARCHAR(64) NOT NULL,
    `discord_name` VARCHAR(64) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT discord_name_unique_idx UNIQUE (`discord_name`),
    CONSTRAINT rsn_unique_idx UNIQUE (`rsn`)
);
