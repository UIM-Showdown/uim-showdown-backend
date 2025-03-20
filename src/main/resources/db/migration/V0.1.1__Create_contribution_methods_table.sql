CREATE TABLE `contribution_methods` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `tile_id` INT NOT NULL,
    `name` VARCHAR(64) NOT NULL,
    `category` ENUM('PVM', 'SKILLING', 'OTHER') NOT NULL,
    `type` ENUM('KC', 'SUBMISSION', 'XP') NOT NULL,
    `eht_rate` FLOAT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`tile_id`) REFERENCES `tiles` (`id`) ON DELETE CASCADE,
    CONSTRAINT name_unique_idx UNIQUE (`name`)
);
