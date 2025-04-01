CREATE TABLE `submissions` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `player_id` INT NOT NULL,
    `reviewed_at` TIMESTAMP,
    `reviewer` VARCHAR(64),
    `state` ENUM('OPEN', 'APPROVED', 'DENIED') NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`player_id`) REFERENCES `players` (`id`) ON DELETE CASCADE,
    INDEX state_idx (`state`) 
);
