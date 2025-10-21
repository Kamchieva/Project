CREATE TABLE IF NOT EXISTS Employee (
      id INT PRIMARY KEY ,
      name VARCHAR(255),
      age INT,
      department VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS UserCredential (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL
);
