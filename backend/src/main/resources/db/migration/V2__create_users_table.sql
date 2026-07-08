CREATE TABLE `users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(255) NOT NULL,
  `password_hash` VARCHAR(255) NOT NULL,
  `display_name` VARCHAR(100) NOT NULL,
  `system_role_id` BIGINT NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
  `deleted_at` TIMESTAMP NULL DEFAULT NULL,
  `deleted_by` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `uk_users_email` UNIQUE (`email`),
  CONSTRAINT `fk_users_system_role_id`
    FOREIGN KEY(`system_role_id`)
    REFERENCES `system_roles`(`id`),
  CONSTRAINT `fk_users_deleted_by`
    FOREIGN KEY (`deleted_by`)
    REFERENCES `users`(`id`)
);