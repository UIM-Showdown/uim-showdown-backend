CREATE TABLE `collection_log_completions` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `item_id` INT NOT NULL,
    `player_id` INT NOT NULL,
    `screenshot_url` VARCHAR(512) NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`item_id`) REFERENCES `collection_log_items` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE
);
