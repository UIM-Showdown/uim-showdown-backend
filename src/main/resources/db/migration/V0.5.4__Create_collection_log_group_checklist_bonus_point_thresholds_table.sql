CREATE TABLE `collection_log_group_checklist_bonus_point_thresholds` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `collection_log_group_id` INT NOT NULL,
    `value` INT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`collection_log_group_id`) REFERENCES `collection_log_groups` (`id`) ON DELETE CASCADE
);
