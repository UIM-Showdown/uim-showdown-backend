CREATE TABLE `challenge_submissions` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `challenge_id` INT NOT NULL,
    `challenge_relay_component_id` INT NOT NULL,
    `submission_id` INT NOT NULL,
    `seconds` DOUBLE NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`challenge_id`) REFERENCES `challenges` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`challenge_relay_component_id`) REFERENCES `challenge_relay_components` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`submission_id`) REFERENCES `submissions` (`id`) ON DELETE CASCADE
);
