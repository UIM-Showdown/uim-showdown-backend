CREATE TABLE `tiles` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(64) NOT NULL,
    `points_per_tier` INT NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT name_unique_idx UNIQUE (`name`)
);
