CREATE TABLE `record_handicaps` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `record_id` INT NOT NULL,
    `name` VARCHAR(64) NOT NULL,
    `multiplier` DOUBLE NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`record_id`) REFERENCES `records` (`id`) ON DELETE CASCADE
);
