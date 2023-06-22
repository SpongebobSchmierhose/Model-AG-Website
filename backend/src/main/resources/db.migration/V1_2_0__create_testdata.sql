INSERT INTO users (first_name, last_name, username, password, expo_push_token)
VALUES ('John', 'Johnson', 'john@example.com', '$2a$12$bkZ/cgdZVACsuCdl.5mVfuuOJ25QNI//hwzvBHbUa8gFWmB8.IL6K', NULL),
       ('Jane', 'Jannet', 'jane@example.com', '$2a$12$YsBa1kesWFnio7vflgyU8etkUZIRIiT49ykJDU/dA3CUG8XLBTjmy', NULL),
       ('Bob', 'Marley', 'bob@example.com', '$2a$12$VAkkVrnh3QT8hBGIQv7BjuoCugq5/1atm4MH7Mfh/DTyYOJ7kHvTO', NULL),
       ('Billy', 'Bolton', 'billy@example.com', '$2a$12$PiWphO21kjkvKuzkeQIVcejUttuCoikToYPZ4c3jb5vgMmI1Tg9DS', NULL);

INSERT INTO roles (name)
VALUES ('ROLE_ADMIN'),
       ('ROLE_MODERATOR'),
       ('ROLE_USER');

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
         JOIN roles r
              ON u.username = 'john@example.com' AND r.name = 'ROLE_ADMIN'
                  OR u.username = 'jane@example.com' AND r.name = 'ROLE_MODERATOR'
                  OR u.username = 'bob@example.com' AND r.name = 'ROLE_USER';
