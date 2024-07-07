CREATE TABLE IF NOT EXISTS users
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255),
    email      VARCHAR(255),
    country    VARCHAR(255),
    is_deleted BOOLEAN DEFAULT FALSE
    );

CREATE TABLE IF NOT EXISTS addresses
(
    id                 INT AUTO_INCREMENT PRIMARY KEY,
    address_has_active BOOLEAN DEFAULT FALSE,
    city               VARCHAR(255),
    country            VARCHAR(255),
    street             VARCHAR(255),
    employee_id        INT,
    FOREIGN KEY (employee_id) REFERENCES users(id)
    );

CREATE TABLE IF NOT EXISTS documents
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    expire_date DATETIME(6),
    is_handled  BOOLEAN,
    number      VARCHAR(255) NOT NULL,
    uuid        VARCHAR(255)
    );


