CREATE TABLE `challenge_completions` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `challenge_id` INT NOT NULL,
    `team_id` INT NOT NULL,
    `completed_at` TIMESTAMP,
    `seconds` FLOAT,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`challenge_id`) REFERENCES `challenges` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`) ON DELETE CASCADE
);
