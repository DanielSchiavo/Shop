CREATE TABLE customers (
    id BIGSERIAL,
    cpf CHAR(11) NOT NULL UNIQUE,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    birth_date DATE NOT NULL,
    account_creation_date DATE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    cellphone_number CHAR(11) UNIQUE,
    profile_picture VARCHAR(40) NOT NULL,
    PRIMARY KEY(id)
);