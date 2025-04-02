CREATE TABLE `record_submissions` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `handicap_id` INT,
    `record_id` INT NOT NULL,
    `submission_id` INT NOT NULL,
    `submitted_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `value` INT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`handicap_id`) REFERENCES `record_handicaps` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`record_id`) REFERENCES `records` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`submission_id`) REFERENCES `submissions` (`id`) ON DELETE CASCADE
);
