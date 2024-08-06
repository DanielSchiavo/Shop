CREATE TABLE products(
    id BIGSERIAL,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    price NUMERIC(10,2) NOT NULL,
    quantity INTEGER NOT NULL,
    active BOOLEAN NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (category_id)
                     REFERENCES categories (id)
);