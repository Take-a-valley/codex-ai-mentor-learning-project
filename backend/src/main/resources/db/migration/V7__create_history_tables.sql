CREATE TABLE `project_invitations` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `project_id` BIGINT NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `token` VARCHAR(255) NOT NULL,
    `status` VARCHAR(50) NOT NULL,
    `invited_by` BIGINT NOT NULL,
    `initial_project_role_id` BIGINT NOT NULL,
    `expires_at` TIMESTAMP NOT NULL,
    `accepted_at` TIMESTAMP NULL DEFAULT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `uk_project_invitations_token` UNIQUE (`token`),
    CONSTRAINT `fk_project_invitations_project_id`
        FOREIGN KEY (`project_id`)
        REFERENCES `projects`(`id`),
    CONSTRAINT `fk_project_invitations_invited_by`
        FOREIGN KEY (`invited_by`)
        REFERENCES `users`(`id`),
    CONSTRAINT `fk_project_invitations_initial_project_role_id`
        FOREIGN KEY (`initial_project_role_id`)
        REFERENCES `project_roles`(`id`)
);

CREATE TABLE `password_reset_tokens` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `token` VARCHAR(255) NOT NULL,
    `status` VARCHAR(50) NOT NULL,
    `expires_at` TIMESTAMP NOT NULL,
    `used_at` TIMESTAMP NULL DEFAULT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `uk_password_reset_tokens_token` UNIQUE (`token`),
    CONSTRAINT `fk_password_reset_tokens_user_id`
        FOREIGN KEY (`user_id`)
        REFERENCES `users`(`id`)
);

CREATE INDEX `idx_project_invitations_project_id` ON `project_invitations` (`project_id`);
CREATE INDEX `idx_project_invitations_email` ON `project_invitations` (`email`);
CREATE INDEX `idx_project_invitations_status` ON `project_invitations` (`status`);
CREATE INDEX `idx_project_invitations_expires_at` ON `project_invitations` (`expires_at`);

CREATE INDEX `idx_password_reset_tokens_user_id` ON `password_reset_tokens` (`user_id`);
CREATE INDEX `idx_password_reset_tokens_status` ON `password_reset_tokens` (`status`);
CREATE INDEX `idx_password_reset_tokens_expires_at` ON `password_reset_tokens` (`expires_at`);