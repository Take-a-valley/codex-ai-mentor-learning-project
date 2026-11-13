UPDATE users
    SET password_hash = concat('{bcrypt}', password_hash)
    WHERE password_hash LIKE '$2a$%'
    OR password_hash LIKE '$2b$%'
    OR password_hash LIKE '$2y$%';