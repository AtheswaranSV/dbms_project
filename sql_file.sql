CREATE DATABASE voting;

USE voting;

CREATE TABLE admin (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL
);
INSERT INTO admin (username, password) VALUES ('admin', 'password');

CREATE TABLE citizen (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(10),
    contact VARCHAR(15) NOT NULL
);
