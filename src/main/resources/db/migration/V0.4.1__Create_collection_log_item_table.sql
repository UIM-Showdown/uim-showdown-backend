CREATE TABLE `collection_log_items` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `group_id` INT NOT NULL,
    `description` VARCHAR(512) NOT NULL,
    `name` VARCHAR(64) NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`group_id`) REFERENCES `collection_log_groups` (`id`) ON DELETE CASCADE,
    CONSTRAINT name_unique_idx UNIQUE (`name`)
);
