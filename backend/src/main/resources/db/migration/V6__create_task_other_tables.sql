CREATE TABLE `task_comments` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `task_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `body` TEXT NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    `deleted_at` TIMESTAMP NULL DEFAULT NULL,
    `deleted_by` BIGINT NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_task_comments_task_id`
        FOREIGN KEY (`task_id`)
        REFERENCES `tasks`(`id`),
    CONSTRAINT `fk_task_comments_user_id`
        FOREIGN KEY (`user_id`)
        REFERENCES `users`(`id`),
    CONSTRAINT `fk_task_comments_deleted_by`
        FOREIGN KEY (`deleted_by`)
        REFERENCES `users`(`id`)
);

CREATE TABLE `attachments` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `task_id` BIGINT NOT NULL,
    `uploaded_by` BIGINT NOT NULL,
    `file_name` VARCHAR(255) NOT NULL,
    `file_data` LONGBLOB NOT NULL,
    `file_size` BIGINT NOT NULL,
    `content_type` VARCHAR(255) NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `deleted_at` TIMESTAMP NULL DEFAULT NULL,
    `deleted_by` BIGINT NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_attachments_task_id`
        FOREIGN KEY (`task_id`)
        REFERENCES `tasks`(`id`),
    CONSTRAINT `fk_attachments_uploaded_by`
        FOREIGN KEY (`uploaded_by`)
        REFERENCES `users`(`id`),
    CONSTRAINT `fk_attachments_deleted_by`
        FOREIGN KEY (`deleted_by`)
        REFERENCES `users`(`id`)
);

CREATE INDEX `idx_task_comments_task_id` ON `task_comments` (`task_id`);
CREATE INDEX `idx_task_comments_user_id` ON `task_comments` (`user_id`);
CREATE INDEX `idx_task_comments_deleted_at` ON `task_comments` (`deleted_at`);

CREATE INDEX `idx_attachments_task_id` ON `attachments` (`task_id`);
CREATE INDEX `idx_attachments_uploaded_by` ON `attachments` (`uploaded_by`);
CREATE INDEX `idx_attachments_deleted_at` ON `attachments` (`deleted_at`);