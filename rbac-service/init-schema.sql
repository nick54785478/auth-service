-- 用戶資料表 (USER)：存儲用戶的基本信息
DROP TABLE IF EXISTS user_info;
CREATE TABLE IF NOT EXISTS user_info (
    `id` BIGINT(20) AUTO_INCREMENT,
    `name` VARCHAR(100),
    `username` VARCHAR(100),
    `password` VARCHAR(100),
    `address` VARCHAR(255),
    `email` VARCHAR(100),
    `national_id` VARCHAR(10),
    `birthday` DATETIME,
    `active_flag` CHAR(1),
    `refresh_token` text,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 角色資料表 (ROLE)：存儲角色信息
DROP TABLE IF EXISTS role_info;
CREATE TABLE IF NOT EXISTS role_info (
    `id` BIGINT(20) AUTO_INCREMENT,
    `service` VARCHAR(100),
    `code` VARCHAR(100),
    `name` VARCHAR(100),
    `type` VARCHAR(100),
    `description` VARCHAR(255),
    `active_flag` CHAR(1),
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 群組表 (GROUP_INFO)：存儲群組信息
DROP TABLE IF EXISTS group_info;
CREATE TABLE IF NOT EXISTS group_info (
    `id` BIGINT(20) AUTO_INCREMENT,
    `service` VARCHAR(100),
    `type` VARCHAR(100),
    `name` VARCHAR(100),
    `code` VARCHAR(100),
    `description` VARCHAR(255),
    `active_flag` CHAR(1),
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 功能表 (FUNCTION_INFO)：存儲功能代碼和行為類型
DROP TABLE IF EXISTS function_info;
CREATE TABLE function_info (
    `id` BIGINT(20) AUTO_INCREMENT,
    `service` VARCHAR(100),
    `type` VARCHAR(30),
    `action_type` VARCHAR(100),
    `name` VARCHAR(100),
    `code` VARCHAR(100),
    `description` VARCHAR(255),
    `active_flag` CHAR(1),
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 群組使用者關聯表 (USER_GROUP)：定義群組與用戶的關聯
DROP TABLE IF EXISTS user_group;
CREATE TABLE IF NOT EXISTS user_group (
    `id` BIGINT(20) AUTO_INCREMENT,
    `user_id` BIGINT(20),
    `group_id` BIGINT(20),
    `active_flag` CHAR(1),  
    CONSTRAINT FK_USER_GROUP FOREIGN KEY (user_id) REFERENCES user_info(id),
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 群組角色關聯表 (ROLE_GROUP)：定義群組與角色的關聯
DROP TABLE IF EXISTS group_role;
CREATE TABLE IF NOT EXISTS group_role (
    `id` BIGINT(20) AUTO_INCREMENT,
    `role_id` BIGINT(20),
    `group_id` BIGINT(20),
    `active_flag` CHAR(1), 
    CONSTRAINT FK_ROLE_GROUP FOREIGN KEY (group_id) REFERENCES group_info(id),
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 角色功能關聯表 (ROLE_FUNCTION)：定義角色與功能的關聯
DROP TABLE IF EXISTS role_function;
CREATE TABLE IF NOT EXISTS role_function (
    `id` BIGINT(20) AUTO_INCREMENT,
    `role_id` BIGINT(20),
    `function_id` BIGINT(20),
    `active_flag` CHAR(1),  
    CONSTRAINT FK_ROLE_FUNCTION FOREIGN KEY (role_id) REFERENCES role_info(id),
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 使用者角色關聯表 (USER_ROLE)：定義用戶與角色的關聯
DROP TABLE IF EXISTS user_role;
CREATE TABLE IF NOT EXISTS user_role (
    `id` BIGINT(20) AUTO_INCREMENT,
    `user_id` BIGINT(20),
    `role_id` BIGINT(20),
    `active_flag` CHAR(1), 
    CONSTRAINT FK_USER_ROLE FOREIGN KEY (user_id) REFERENCES user_info(id),
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS setting;
CREATE TABLE IF NOT EXISTS setting (
    `id` BIGINT(20) AUTO_INCREMENT,
    `service` VARCHAR(20),
    `data_type` VARCHAR(20),
    `type` VARCHAR(100),
    `name` VARCHAR(100),
    `code` VARCHAR(100),
    `value` VARCHAR(255),
    `description` TEXT,
    `priority_no` INT,
    `active_flag` CHAR(1),
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS customisation;
CREATE TABLE customisation (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(100),
    `component` VARCHAR(100),
    `type` VARCHAR(100),       
    `name` VARCHAR(100),
    `value` VARCHAR(150),   
    `description` TEXT,
    `active_flag` CHAR(2)
);