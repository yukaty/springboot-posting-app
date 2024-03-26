-- This SQL is executed every time the app runs
-- The IGNORE keyword ignore any duplicate key errors and continue with the next row.
INSERT IGNORE INTO roles (id, name) VALUES (1, 'ROLE_GENERAL');

INSERT IGNORE INTO users (id, name, email, password, role_id, enabled) VALUES (1, 'John Doe', 'johnd@example.com', 'xxx', 1, true);
INSERT IGNORE INTO users (id, name, email, password, role_id, enabled) VALUES (2, 'Jane Doe', 'janed@example.com', 'yyy', 1, true);
INSERT IGNORE INTO users (id, name, email, password, role_id, enabled) VALUES (3, 'Chloe Doe', 'chloed@example.com', 'zzz', 1, true);
