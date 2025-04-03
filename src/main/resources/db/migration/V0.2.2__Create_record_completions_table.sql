CREATE TABLE `record_completions` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `record_id` INT NOT NULL,
    `record_handicap_id` INT,
    `player_id` INT NOT NULL,
    `completed_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `raw_value` INT NOT NULL,
    `video_url` VARCHAR(512),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`record_id`) REFERENCES `records` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`record_handicap_id`) REFERENCES `record_handicaps` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE
);
