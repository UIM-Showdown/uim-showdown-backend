CREATE TABLE `player_scoreboards` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `player_id` INT NOT NULL,
    `collection_log_points` INT NOT NULL,
    `other_tile_contribution` DOUBLE NOT NULL,
    `pvm_tile_contribution` DOUBLE NOT NULL,
    `skilling_tile_contribution` DOUBLE NOT NULL,
    `total_tile_contribution` DOUBLE NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE
);
