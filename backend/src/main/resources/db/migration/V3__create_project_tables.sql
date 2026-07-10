CREATE TABLE `projects` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `description` TEXT NULL DEFAULT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `created_by` BIGINT NOT NULL,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    `deleted_at` TIMESTAMP NULL DEFAULT NULL,
    `deleted_by` BIGINT NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_projects_created_by`
        FOREIGN KEY (`created_by`)
        REFERENCES `users`(`id`),
    CONSTRAINT `fk_projects_deleted_by`
        FOREIGN KEY (`deleted_by`)
        REFERENCES `users`(`id`)
);

CREATE TABLE `project_members` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `project_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `project_role_id` BIGINT NOT NULL,
    `joined_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `deleted_at` TIMESTAMP NULL DEFAULT NULL,
    `deleted_by` BIGINT NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `uk_project_members_project_id_user_id` UNIQUE (`project_id`, `user_id`),
    CONSTRAINT `fk_project_members_project_id`
        FOREIGN KEY (`project_id`)
        REFERENCES `projects`(`id`),
    CONSTRAINT `fk_project_members_user_id`
        FOREIGN KEY (`user_id`)
        REFERENCES `users`(`id`),
    CONSTRAINT `fk_project_members_project_role_id`
        FOREIGN KEY (`project_role_id`)
        REFERENCES `project_roles`(`id`),
    CONSTRAINT `fk_project_members_deleted_by`
        FOREIGN KEY (`deleted_by`)
        REFERENCES `users`(`id`)
);