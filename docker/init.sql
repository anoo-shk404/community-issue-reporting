-- Create database if not exists
CREATE DATABASE IF NOT EXISTS fsdproject;
USE fsdproject;

-- Create users table (exactly matching your structure)
CREATE TABLE IF NOT EXISTS users (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    name VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    password VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    role ENUM('ADMIN', 'USER') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    username VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY UK_username (username)
);

-- Create category table (exactly matching your structure)
CREATE TABLE IF NOT EXISTS category (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    PRIMARY KEY (id)
);

-- Create issue table (exactly matching your structure)
CREATE TABLE IF NOT EXISTS issue (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6) DEFAULT NULL,
    description VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    image_url VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    location VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    status ENUM('IN_PROGRESS', 'PENDING', 'RESOLVED') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    title VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    category_id BIGINT(20) DEFAULT NULL,
    user_id BIGINT(20) DEFAULT NULL,
    latitude DOUBLE DEFAULT NULL,
    longitude DOUBLE DEFAULT NULL,
    PRIMARY KEY (id),
    KEY FK_issue_category (category_id),
    KEY FK_issue_user (user_id),
    CONSTRAINT FK_issue_category FOREIGN KEY (category_id) REFERENCES category (id),
    CONSTRAINT FK_issue_user FOREIGN KEY (user_id) REFERENCES users (id)
);

-- Create comment table (exactly matching your structure)
CREATE TABLE IF NOT EXISTS comment (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6) DEFAULT NULL,
    text VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    user_id BIGINT(20) DEFAULT NULL,
    issue_id BIGINT(20) DEFAULT NULL,
    PRIMARY KEY (id),
    KEY FK_comment_user (user_id),
    KEY FK_comment_issue (issue_id),
    CONSTRAINT FK_comment_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT FK_comment_issue FOREIGN KEY (issue_id) REFERENCES issue (id)
);

-- Insert default categories
INSERT IGNORE INTO category (name) VALUES 
    ('Infrastructure'),
    ('Environment'),
    ('Public Safety'),
    ('Utilities'),
    ('Health & Sanitation'),
    ('Education'),
    ('Transportation'),
    ('Community Services');

-- Create admin user (password: admin123)
-- BCrypt hash for 'admin123': $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9BVgm5ZvYLlr3Ey
INSERT IGNORE INTO users (username, email, name, password, role) 
VALUES ('admin', 'admin@issuereporting.com', 'System Administrator', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9BVgm5ZvYLlr3Ey', 'ADMIN');

-- Create test user (password: user123)
-- BCrypt hash for 'user123': $2a$10$DOwnh8FQcFEqLBnKMVqp9eFESDdQXH8Qt90j6jSYaBpGNtN3LD9iO
INSERT IGNORE INTO users (username, email, name, password, role) 
VALUES ('testuser', 'user@issuereporting.com', 'Test User', '$2a$10$DOwnh8FQcFEqLBnKMVqp9eFESDdQXH8Qt90j6jSYaBpGNtN3LD9iO', 'USER');