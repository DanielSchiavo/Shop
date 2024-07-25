CREATE TABLE customers_addresses (
    id BIGSERIAL,
    postal_code VARCHAR(10) NOT NULL,
    street VARCHAR(100) NOT NULL,
    number VARCHAR(50),
    complement VARCHAR(255),
    neighborhood VARCHAR(100),
    city VARCHAR(100),
    state CHAR(2),
    is_default BOOLEAN NOT NULL,
    PRIMARY KEY (id)
);