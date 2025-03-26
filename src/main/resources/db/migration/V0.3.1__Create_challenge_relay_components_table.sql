CREATE TABLE `challenge_relay_components` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `challenge_id` INT NOT NULL,
    `name` VARCHAR(64) NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`challenge_id`) REFERENCES `challenges` (`id`) ON DELETE CASCADE
);
