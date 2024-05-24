INSERT INTO users_security_prac (email, password_hashed)
VALUES ('test', '$2a$10$wHvnOZC8nIhrD7wXNrWQZ.Syq5hOwc61gGtSDcEnSOwULNInZYAWu')
    ON DUPLICATE KEY UPDATE
                         email = VALUES(email),
                         password_hashed = VALUES(password_hashed);
