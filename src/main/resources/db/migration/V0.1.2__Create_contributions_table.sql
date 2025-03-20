CREATE TABLE `contributions` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `contribution_method_id` INT NOT NULL,
    `player_id` INT NOT NULL,
    `initial_value` INT,
    `initial_value_screenshot_url` VARCHAR(512),
    `final_value` INT,
    `final_value_screenshot_url` VARCHAR(512),
    `staff_adjustment` INT NOT NULL,
    `unranked_starting_value` INT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`contribution_method_id`) REFERENCES `contribution_methods` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE
);
