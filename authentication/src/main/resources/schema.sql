DROP TABLE IF EXISTS authorities;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS oauth_code;
DROP TABLE IF EXISTS oauth_refresh_token;
DROP TABLE IF EXISTS oauth_access_token;
DROP TABLE IF EXISTS oauth_client_token;
DROP TABLE IF EXISTS oauth_client_details;


CREATE TABLE IF NOT EXISTS oauth_client_details (
  client_id VARCHAR(255) PRIMARY KEY,
  resource_ids VARCHAR(255),
  client_secret VARCHAR(255),
  scope VARCHAR(255),
  authorized_grant_types VARCHAR(255),
  web_server_redirect_uri VARCHAR(255),
  authorities VARCHAR(255),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additional_information VARCHAR(4096),
  autoapprove tinyint
);

CREATE TABLE IF NOT EXISTS oauth_client_token (
  token_id VARCHAR(255),
  token BLOB,
  authentication_id VARCHAR(255),
  user_name VARCHAR(255),
  client_id VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS oauth_access_token (
  token_id VARCHAR(255),
  token BLOB,
  authentication_id VARCHAR(255),
  user_name VARCHAR(255),
  client_id VARCHAR(255),
  authentication BLOB,
  refresh_token VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS oauth_refresh_token (
  token_id VARCHAR(255),
  token BLOB,
  authentication BLOB
);


CREATE TABLE IF NOT EXISTS oauth_code (
  code VARCHAR(255), authentication BLOB
);



CREATE TABLE IF NOT EXISTS users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(256) NOT NULL,
  password VARCHAR(256) NOT NULL,
  enabled BOOLEAN,
  account_non_expired BOOLEAN,
  account_non_locked BOOLEAN,
  credentials_non_expired BOOLEAN,
  UNIQUE KEY unique_username(username)
);



CREATE TABLE IF NOT EXISTS authorities (
 id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  authority VARCHAR(256) NOT NULL,
  CONSTRAINT  fk_authorities FOREIGN KEY (user_id) REFERENCES users (id)
);
