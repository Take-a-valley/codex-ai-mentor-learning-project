CREATE TABLE `tasks` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `project_id` BIGINT NOT NULL,
    `summary` VARCHAR(255) NOT NULL,
    `description` TEXT NULL DEFAULT NULL,
    `status_id` BIGINT NOT NULL,
    `assignee_id` BIGINT NULL DEFAULT NULL,
    `due_date` DATE NULL DEFAULT NULL,
    `priority_id` BIGINT NOT NULL,
    `creator_id` BIGINT NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    `deleted_at` TIMESTAMP NULL DEFAULT NULL,
    `deleted_by` BIGINT NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_task_project_id`
        FOREIGN KEY (`project_id`)
        REFERENCES `projects`(`id`),
    CONSTRAINT `fk_task_status_id`
        FOREIGN KEY (`status_id`)
        REFERENCES `task_statuses`(`id`),
    CONSTRAINT `fk_task_assignee_id`
        FOREIGN KEY (`assignee_id`)
        REFERENCES `users`(`id`),
    CONSTRAINT `fk_task_priority_id`
        FOREIGN KEY (`priority_id`)
        REFERENCES `task_priorities`(`id`),
    CONSTRAINT `fk_task_creator_id`
        FOREIGN KEY (`creator_id`)
        REFERENCES `users`(`id`),
    CONSTRAINT `fk_task_deleted_by`
        FOREIGN KEY (`deleted_by`)
        REFERENCES `users`(`id`)
);