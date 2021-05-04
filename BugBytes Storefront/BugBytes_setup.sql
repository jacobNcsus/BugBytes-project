DROP USER IF EXISTS 'shopMgr'@'localhost';
FLUSH PRIVILEGES;
CREATE USER 'shopMgr'@'localhost'
  IDENTIFIED WITH caching_sha2_password BY 'csc131';
GRANT ALL PRIVILEGES ON shop_test.* TO 'shopMgr'@'localhost';