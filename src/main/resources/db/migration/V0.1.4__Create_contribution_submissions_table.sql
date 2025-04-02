CREATE TABLE `contribution_submissions` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `contribution_method_id` INT NOT NULL,
    `submission_id` INT NOT NULL,
    `value` INT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`contribution_method_id`) REFERENCES `contribution_methods` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`submission_id`) REFERENCES `submissions` (`id`) ON DELETE CASCADE
);
