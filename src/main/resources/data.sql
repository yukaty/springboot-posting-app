-- This SQL is executed every time the app runs
-- The IGNORE keyword ignore any duplicate key errors and continue with the next row.
INSERT IGNORE INTO roles (id, name) VALUES (1, 'ROLE_GENERAL');

INSERT IGNORE INTO users (id, name, email, password, role_id, enabled) VALUES (1, 'John Doe', 'johnd@example.com', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', 1, true);
INSERT IGNORE INTO users (id, name, email, password, role_id, enabled) VALUES (2, 'Jane Doe', 'janed@example.com', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', 1, true);
INSERT IGNORE INTO users (id, name, email, password, role_id, enabled) VALUES (3, 'Chloe Doe', 'chloed@example.com', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', 1, true);

INSERT IGNORE INTO posts (id, user_id, title, content, created_at, updated_at) VALUES (1, 1, 'Day 1', 'Started learning programming today! Let\'s go for it!', '2024-01-01 00:00:00', '2024-01-01 00:00:00');
INSERT IGNORE INTO posts (id, user_id, title, content, created_at, updated_at) VALUES (2, 1, 'Day 2', 'I need to setup a local development environment first. Installed Visual Studio Code for now.', '2024-01-02 00:00:00', '2024-01-02 00:00:00');
INSERT IGNORE INTO posts (id, user_id, title, content, created_at, updated_at) VALUES (3, 1, 'Day 3', 'I started with HTML, and learned how websites are displayed. Happy coding!', '2024-01-03 00:00:00', '2024-01-03 00:00:00');
INSERT IGNORE INTO posts (id, user_id, title, content, created_at, updated_at) VALUES (4, 1, 'Day 4', 'I learned CSS today and Flexbox is difficult. I\'ll have to get used to it through hands-on.', '2024-01-04 00:00:00', '2024-01-04 00:00:00');
INSERT IGNORE INTO posts (id, user_id, title, content, created_at, updated_at) VALUES (5, 1, 'Day 5', 'Started an advanced HTML/CSS course. I enjoy learning coding!', '2024-01-05 00:00:00', '2024-01-05 00:00:00');