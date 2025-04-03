CREATE TABLE `team_scoreboards` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `team_id` INT NOT NULL,
    `event_points` INT NOT NULL,
    `event_points_from_challenges` INT NOT NULL,
    `event_points_from_collection_log_items` INT NOT NULL,
    `event_points_from_groups` INT NOT NULL,
    `event_points_from_records` INT NOT NULL,
    `event_points_from_tiles` INT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`) ON DELETE CASCADE
);
