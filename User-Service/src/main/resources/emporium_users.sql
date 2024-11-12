USE emporium_user_service;
CREATE TABLE emporium_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    full_name VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP
);

CREATE TABLE emporium_user_profile (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    role_name VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES emporium_users(id)
);