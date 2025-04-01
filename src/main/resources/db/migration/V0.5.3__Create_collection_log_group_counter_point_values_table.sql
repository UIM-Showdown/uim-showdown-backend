CREATE TABLE `collection_log_group_counter_point_values` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `collection_log_group_id` INT NOT NULL,
    `value` INT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`collection_log_group_id`) REFERENCES `collection_log_groups` (`id`) ON DELETE CASCADE
);
