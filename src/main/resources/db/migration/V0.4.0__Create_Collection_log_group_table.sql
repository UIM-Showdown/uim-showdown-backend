CREATE TABLE `collection_log_groups` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `description` VARCHAR(512) NOT NULL,
    `name` VARCHAR(64) NOT NULL,
    `type` ENUM('CHECKLIST', 'COUNTER') NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT name_unique_idx UNIQUE (`name`)
);
