CREATE TABLE `tile_progress` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `team_id` INT NOT NULL,
    `tile_id` INT NOT NULL,
    `points` INT NOT NULL,
    `tier` INT NOT NULL,
    `percentage_to_next_tier` DOUBLE NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`tile_id`) REFERENCES `tiles` (`id`) ON DELETE CASCADE
);
