CREATE TABLE `submission_screenshot_urls` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `submission_id` INT NOT NULL,
    `screenshot_url` VARCHAR(512) NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`submission_id`) REFERENCES `submissions` (`id`) ON DELETE CASCADE
);
