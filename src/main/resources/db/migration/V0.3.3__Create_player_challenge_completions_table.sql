CREATE TABLE `player_challenge_completions` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `challenge_completion_id` INT NOT NULL,
    `player_id` INT NOT NULL,
    `relay_component_id` INT NOT NULL,
    `screenshot_url` VARCHAR(512) NOT NULL,
    `seconds` DOUBLE NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`challenge_completion_id`) REFERENCES `challenge_completions` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`relay_component_id`) REFERENCES `challenge_relay_components` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE
);
