-- Setting
INSERT INTO `setting` (`id`, `service`, `name`, `code`, `value`, `data_type`, `type`, `description`, `priority_no`, `active_flag`) VALUES
(1, 'AUTH_SERVICE', '管理者', 'ADMIN', '管理者', 'ROLE', 'ADMIN', '系統管理員', 1, 'Y');
INSERT INTO `setting` (`id`, `service`, `name`, `code`, `value`, `data_type`, `type`, `description`, `priority_no`, `active_flag`) VALUES
(2, 'AUTH_SERVICE', '管理者群組', 'ADMIN_GROUP', '管理者群組', 'GROUP', 'ADMIN', '有管理者權限的群組', 1, 'Y');
INSERT INTO `setting` (`id`, `service`, `name`, `code`, `value`, `data_type`, `type`, `description`, `priority_no`, `active_flag`) VALUES
(4, 'AUTH_SERVICE', 'PM', 'PM_GROUP', 'PM', 'GROUP', 'PM', 'PM 群組', 2, 'Y');
INSERT INTO `setting` (`id`, `service`, `name`, `code`, `value`, `data_type`, `type`, `description`, `priority_no`, `active_flag`) VALUES
(5, 'AUTH_SERVICE', 'SALES', 'SALES_GROUP', 'SALES', 'GROUP', 'SALES', '業務群組', 1, 'Y'),
(6, 'AUTH_SERVICE', 'Sales', 'SALES', 'Sales', 'ROLE', 'SALES', '業務(Sales) 角色類別', 2, 'Y'),
(7, 'AUTH_SERVICE', 'PM', 'PM', 'PM', 'ROLE', 'PM', 'PM', 3, 'Y'),
(8, 'AUTH_SERVICE', 'Dashboard', 'DASHBOARD', 'Dashboard', 'FUNCTION', 'DASHBOARD', 'Dashboard 相關功能', 1, 'Y'),
(9, 'AUTH_SERVICE', 'User', 'USER_FUNCTION', 'User', 'FUNCTION', 'USER', '使用者相關功能', 2, 'Y'),
(10, 'AUTH_SERVICE', 'Role', 'ROLE_FUNCTION', 'Role', 'FUNCTION', 'ROLE', '角色相關功能', 3, 'Y'),
(11, 'AUTH_SERVICE', 'Group', 'GROUP_FUNCTION', 'Group', 'FUNCTION', 'GROUP', '群組相關功能', 4, 'Y'),
(12, 'AUTH_SERVICE', '客戶配置', 'CUSTOMER_FUNCTION', '客戶配置', 'FUNCTION', 'CUSTOMER', '客戶配置相關功能', 5, 'Y'),
(13, 'AUTH_SERVICE', '設定配置', 'SETTING_FUNCTION', '設定配置', 'FUNCTION', 'SETTING', '設定配置的功能', 6, 'Y'),
(14, 'AUTH_SERVICE', 'Guest', 'GUEST_GROUP', 'Guest', 'GROUP', 'GUEST', '訪客群組', 3, 'Y'),
(15, 'AUTH_SERVICE', 'BROWSER', 'BROWSER', 'Browser', 'ACTION_TYPE', 'B', '瀏覽（Browse， 回傳多筆資料的 READ)', 1, 'Y'),
(16, 'AUTH_SERVICE', 'READ', 'READ', 'Read', 'ACTION_TYPE', 'R', '讀取（Read）', 2, 'Y'),
(17, 'AUTH_SERVICE', 'EDIT', 'EDIT', 'Edit', 'ACTION_TYPE', 'E', ' 編輯（Edit）', 3, 'Y'),
(18, 'AUTH_SERVICE', 'ADD', 'ADD', 'Add', 'ACTION_TYPE', 'A', '建立（Add）', 4, 'Y'),
(19, 'AUTH_SERVICE', 'DELETE', 'DELETE', 'Delete', 'ACTION_TYPE', 'D', '刪除（Delete）', 5, 'Y'),
(20, 'AUTH_SERVICE', 'SEARCH', 'SEARCH', 'Search', 'ACTION_TYPE', 'S', '搜尋（Search）', 6, 'Y'),
(21, 'AUTH_SERVICE', 'Y', 'Y', 'Y', 'YES_NO', 'Y', 'YES', 1, 'Y'),
(22, 'AUTH_SERVICE', 'N', 'N', 'N', 'YES_NO', 'N', 'NO', 1, 'Y'),
(23, 'AUTH_SERVICE', '英語', 'ENGLISH', 'en', 'LANGUAGE', 'ENGLISH', '英語語系', 1, 'Y'),
(24, 'AUTH_SERVICE', '繁中', 'ZH_TW', 'zh_tw', 'LANGUAGE', 'ZH_TW', '繁體中文語系', 2, 'Y'),
(25, 'AUTH_SERVICE', '簡中', 'ZH_CH', 'zh_ch', 'LANGUAGE', 'ZH_CH', '簡體中文語系', 3, 'Y'),
(26, 'AUTH_SERVICE', 'Maintain', 'MAINTAIN', '維護頁面功能', 'FUNCTION', 'MAINTAIN', '維護頁面功能', 1, 'Y'),
(29, 'AUTH_SERVICE', 'ALL', 'ALL', 'ALL', 'ACTION_TYPE', 'ALL', '全動作權限', NULL, 'Y'),
(32, 'AUTH_SERVICE', 'TEST', 'TEST_GROUP', '測試群組', 'GROUP', 'TEST', '測試群組', 4, 'Y'),
(33, 'AUTH_SERVICE', '測試', 'TEST_FUNCTION', '測試功能', 'FUNCTION', 'TEST', '測試功能', 1, 'Y'),
(34, 'AUTH_SERVICE', '品管人員', 'QC', '品管人員', 'ROLE', 'QC', '品管人員', 4, 'Y'),
(35, 'AUTH_SERVICE', 'QC', 'QC_GROUP', '品管群組', 'GROUP', 'QC', '品質管理人員群組', 5, 'Y'),
(36, 'AUTH_SERVICE', 'QC', 'QC_FUNCTION', '品管權限', 'FUNCTION', 'QC', 'QC 相關權限', 1, 'Y'),
(37, 'AUTH_SERVICE', '開發', 'DEVELOPMENT_FUNCTION', '開發功能', 'FUNCTION', 'DEVELOPMENT', '開發', 1, 'Y'),
(38, 'AUTH_SERVICE', '開發', 'DEVELOPMENT_GROUP', '開發群組', 'GROUP', 'DEVELOPMENT', '開發群組', 1, 'Y'),
(39, 'AUTH_SERVICE', '開發人員', 'DEVELOPMENT', '開發人員', 'ROLE', 'DEVELOPMENT', '開發人員', 1, 'Y'),
(40, 'AUTH_SERVICE', 'Step1 敘述', 'STEP1', '用戶進行登入動作，取得攜帶該用戶資訊的 JWToken，如: 群組(GROUP)、角色(ROLE) 等 ，在後續進行動作時會根據其 Token 記錄的群組 (GROUP) 資料以及個人角色 (ROLE) 資料進行功能 (FUNCTION) 權限檢查。', 'DESCRIPTION', 'STEP1', '用戶進行登入動作，取得攜帶該用戶資訊的 JWToken，如: 群組(GROUP)、角色(ROLE) 等 ，在後續進行動作時會根據其 Token 記錄的群組 (GROUP) 資料以及個人角色 (ROLE) 資料進行功能 (FUNCTION) 權限檢查。', 1, 'Y'),
(41, 'AUTH_SERVICE', 'Step2 敘述', 'STEP2', '使用者所屬的群組擁有一組\"預設\"的權限，該權限是透過群組角色 (GROUP_ROLE) 與群組功能 (ROLE_FUNCTION) 的設定，讓使用者能根據其設定來獲得該群組所設定的功能權限。', 'DESCRIPTION', 'STEP2', '使用者所屬的群組擁有一組\"預設\"的權限，該權限是透過群組角色 (GROUP_ROLE) 與群組功能 (ROLE_FUNCTION) 的設定，讓使用者能根據其設定來獲得該群組所設定的功能權限。', 2, 'Y'),
(42, 'AUTH_SERVICE', 'Step3 敘述', 'STEP3', '除了群組角色權限，使用者也能設定個人的角色權限，這些權限是由使用者與角色的關聯表 (USER_ROLE) 來進行設定，這些個人角色權限會在群組權限基礎上進行\"再擴展\"。', 'DESCRIPTION', 'STEP3', '除了群組角色權限，使用者也能設定個人的角色權限，這些權限是由使用者與角色的關聯表 (USER_ROLE) 來進行設定，這些個人角色權限會在群組權限基礎上進行\"再擴展\"。', 3, 'Y'),
(43, 'AUTH_SERVICE', 'Step4 敘述', 'STEP4', '登錄系統後，系統會檢查用戶所屬的群組 (GROUP) 及其對應的角色權限(ROLE)，然後再檢查用戶的個人角色 (ROLE) 權限。最後，使用者的權限由群組角色 (GROUP_ROLE) 權限和個人角色 (ROLE) 權限所設定的功能 (FUNCTION) 權限合併形成。', 'DESCRIPTION', 'STEP4', '登錄系統後，系統會檢查用戶所屬的群組 (GROUP) 及其對應的角色權限(ROLE)，然後再檢查用戶的個人角色 (ROLE) 權限。最後，使用者的權限由群組角色 (GROUP_ROLE) 權限和個人角色 (ROLE) 權限所設定的功能 (FUNCTION) 權限合併形成。', 4, 'Y'),
(44, 'AUTH_SERVICE', 'RbacService', 'AUTH_SERVICE', 'AuthService', 'SERVICE', 'AUTH_SERVICE', '一個關於 RBAC 實作的微服務', 1, 'Y'),
(45, 'AUTH_SERVICE', 'TrainService', 'TRAIN_SERVICE', 'TrainService', 'SERVICE', 'TRAIN_SERVICE', '一個與火車相關的微服務', 1, 'Y'),
(46, 'TRAIN_SERVICE', '檢票員', 'TICKET_CLERK', '檢票員', 'ROLE', 'TICKET_CLERK', '負責檢票的人員', 2, 'Y');

-- User
INSERT INTO `user_info` (`id`, `name`, `username`, `password`, `address`, `email`, `national_id`, `birthday`, `active_flag`, `refresh_token`) VALUES
(1, '張承翊', 'max123@example.com', '$2a$10$QDWJ4gglMtRorFY/mQ.m8.gMKqz9/u03RNN3VQhaTSyPUH6ddL.l2', '台北市南港區', 'max123@example.com', 'A123456789', '1969-09-01 00:00:00', 'Y', 'eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6W10sImdyb3VwcyI6W10sImVtYWlsIjoibWF4MTIzQGV4YW1wbGUuY29tIiwiaXNzIjoiU1lTVEVNIiwic3ViIjoibWF4MTIzQGV4YW1wbGUuY29tIiwiaWF0IjoxNzU1OTU5NjM1LCJleHAiOjE3NTY1NjQ0MzV9.loUDi5L8A6v3Q70W0CA8P0XJIIrodmaUGgoTk6rQIAE');
INSERT INTO `user_info` (`id`, `name`, `username`, `password`, `address`, `email`, `national_id`, `birthday`, `active_flag`, `refresh_token`) VALUES
(2, '張耿豪', 'nick123@example.com', '$2a$10$HsSpBpgOug5aKvi38mLNoONF7PqjiYqPwYnX5afNFWqPj/x9/xEsK', '新北市淡水區', 'nick123@example.com', 'T124149396', '1995-09-01 00:00:00', 'Y', 'eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6W10sImdyb3VwcyI6W10sImVtYWlsIjoibmljazEyM0BleGFtcGxlLmNvbSIsImlzcyI6IlNZU1RFTSIsInN1YiI6Im5pY2sxMjNAZXhhbXBsZS5jb20iLCJpYXQiOjE3NTYwMTE2OTUsImV4cCI6MTc1NjYxNjQ5NX0.AtwjZoYI8KSJt60nuAuETFT-5tBE7r9UcwGBhYQZtkQ');
INSERT INTO `user_info` (`id`, `name`, `username`, `password`, `address`, `email`, `national_id`, `birthday`, `active_flag`, `refresh_token`) VALUES
(3, '蔣哈克', 'John123@example.com', '$2a$10$ZTCww4Na1qgtRmuWITMwDemCsUfOD6y.RtdFfQmVvOjb1cnMMlF9W', '台北市內湖區', 'john123@example.com', 'A123456788', NULL, 'Y', 'eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6W10sImdyb3VwcyI6W10sImVtYWlsIjoiam9objEyM0BleGFtcGxlLmNvbSIsImlzcyI6IlNZU1RFTSIsInN1YiI6IkpvaG4xMjNAZXhhbXBsZS5jb20iLCJpYXQiOjE3NTA2MDI4MDksImV4cCI6MTc1MTIwNzYwOX0.uqr0alv2nV5qhOb3P34tm8Bk_xsOU-SZ81lAucztwp8');

-- Function
INSERT INTO `function_info` (`id`, `service`, `type`, `action_type`, `name`, `code`, `description`, `active_flag`) VALUES
(1, 'AUTH_SERVICE', 'DASHBOARD', 'B', 'ViewDashboard', 'VIEW_DASHBOARD', '查詢 DashBoard 資料', 'Y');
INSERT INTO `function_info` (`id`, `service`, `type`, `action_type`, `name`, `code`, `description`, `active_flag`) VALUES
(3, 'AUTH_SERVICE', 'USER', 'B', 'QueryUserData', 'QUERY_USER_DATA', '查詢使用者資料', 'Y');
INSERT INTO `function_info` (`id`, `service`, `type`, `action_type`, `name`, `code`, `description`, `active_flag`) VALUES
(4, 'AUTH_SERVICE', 'ROLE', 'B', 'QueryRoleData', 'QUERY_ROLE_DATA', '查詢角色資料', 'Y');
INSERT INTO `function_info` (`id`, `service`, `type`, `action_type`, `name`, `code`, `description`, `active_flag`) VALUES
(5, 'AUTH_SERVICE', 'USER', 'A', 'AddUserData', 'ADD_USER_DATA', '新增使用者資料', 'Y'),
(6, 'AUTH_SERVICE', 'USER', 'D', 'DeleteUserData', 'DELETE_USER_DATA', '刪除使用者資料', 'Y'),
(7, 'AUTH_SERVICE', 'USER', 'E', 'EditUserData', 'EDIT_USER_DATA', '編輯使用者資料', 'Y'),
(8, 'AUTH_SERVICE', 'USER', 'S', 'SearchUserData', 'SEARCH_USER_DATA', '搜尋使用者資料', 'Y'),
(9, 'AUTH_SERVICE', 'MAINTAIN', 'ALL', 'UserRoleMaintaining', 'USER_ROLE_MAINTAIN', '維護使用者角色權限，具有進入 /users/roles 頁面的權利。', 'Y'),
(10, 'AUTH_SERVICE', 'MAINTAIN', 'ALL', 'GroupRoleMaintaining', 'GROUP_ROLE_MAINTAIN', '維護群組角色權限，具有進入 /groups/roles 頁面的權利。', 'Y'),
(11, 'AUTH_SERVICE', 'MAINTAIN', 'ALL', 'RoleFunctionMaintaining', 'ROLE_FUNCTION_MAINTAIN', '維護角色功能權限，具有進入 /roles/functions 頁面的權利。', 'Y'),
(12, 'AUTH_SERVICE', 'MAINTAIN', 'ALL', 'UserMaintaining', 'USER_MAINTAIN', '維護使用者資料權限，具有進入 /users 頁面的權利。', 'Y'),
(13, 'AUTH_SERVICE', 'MAINTAIN', 'ALL', 'RoleMaintaining', 'ROLE_MAINTAIN', '維護角色資料權限，具有進入 /roles 頁面的權利。', 'Y'),
(14, 'AUTH_SERVICE', 'MAINTAIN', 'ALL', 'GroupMaintaining', 'GROUP_MAINTAIN', '維護群組資料權限，具有進入 /groups 頁面的權利。', 'Y'),
(15, 'AUTH_SERVICE', 'MAINTAIN', 'ALL', 'FunctionMaintaining', 'FUNCTION_MAINTAIN', '維護功能資料權限，具有進入 /functions 頁面的權利。', 'Y'),
(16, 'AUTH_SERVICE', 'MAINTAIN', 'ALL', 'SettingMaintaining', 'SETTING_MAINTAIN', '維護自定義設定權限，具有進入 /settings 頁面的權利。', 'Y'),
(17, 'TRAIN_SERVICE', 'TEST', 'R', 'TestTrainService', 'TEST_TRAIN_SERVICE', '測試 Train Service\n查詢資料功能', 'Y');

-- Role
INSERT INTO `role_info` (`id`, `service`, `code`, `name`, `type`, `description`, `active_flag`) VALUES
(1, 'AUTH_SERVICE', 'ADMIN', '管理員', 'ADMIN', '管理者，具有管理者權限。', 'Y');
INSERT INTO `role_info` (`id`, `service`, `code`, `name`, `type`, `description`, `active_flag`) VALUES
(3, 'AUTH_SERVICE', 'INSIDE_SALES', 'Inside Sales', 'SALES', '業務，主要通過電話、電子郵件、視訊會議等方式與客戶聯繫，通常在辦公室內工作，與客戶進行遠程的銷售活動。', 'Y');
INSERT INTO `role_info` (`id`, `service`, `code`, `name`, `type`, `description`, `active_flag`) VALUES
(4, 'AUTH_SERVICE', 'OUTSIDE_SALES', 'Outside Sales', 'SALES', '業務，透過親自拜訪客戶、參加會議或參加產品展示會等場合，進行面對面的銷售活動。他們經常外出到客戶公司或其他商業場所工作。', 'Y');
INSERT INTO `role_info` (`id`, `service`, `code`, `name`, `type`, `description`, `active_flag`) VALUES
(5, 'AUTH_SERVICE', 'CSR', 'CSR', 'SALES', 'Customer Service Representative 的縮寫，直接與客戶互動，負責解答疑問、處理客戶問題、提供支持、促進顧客服務的流程。', 'Y'),
(6, 'TRAIN_SERVICE', 'ADMIN', '管理員', 'ADMIN', '管理者，具有管理者權限。', 'Y'),
(7, 'TRAIN_SERVICE', 'TICKET_CLERK', '檢票員', 'TICKET_CLERK', '檢票員，負責執行檢票行為。', 'Y');

-- Group
INSERT INTO `group_info` (`id`, `service`, `type`, `name`, `code`, `description`, `active_flag`) VALUES
(1, 'AUTH_SERVICE', 'ADMIN', '管理者群組', 'ADMIN_GROUP ', '管理者群組', 'Y');
INSERT INTO `group_info` (`id`, `service`, `type`, `name`, `code`, `description`, `active_flag`) VALUES
(2, 'AUTH_SERVICE', 'SALES', '業務群組', 'SALES_GROUP ', '業務群組', 'Y');
INSERT INTO `group_info` (`id`, `service`, `type`, `name`, `code`, `description`, `active_flag`) VALUES
(3, 'AUTH_SERVICE', 'GUEST', '訪客群組', 'GUEST_GROUP', '訪客所在的群組', 'Y');
INSERT INTO `group_info` (`id`, `service`, `type`, `name`, `code`, `description`, `active_flag`) VALUES
(4, 'TRAIN_SERVICE', 'TEST', 'TRAIN_SERVICE 測試用群組', 'TEST_GROUP', 'TRAIN_SERVICE 測試用群組', 'Y');