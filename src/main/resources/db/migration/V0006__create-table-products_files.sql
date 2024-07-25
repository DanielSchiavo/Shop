CREATE TABLE products_files (
    id BIGSERIAL,
    name VARCHAR(50) NOT NULL,
    position SMALLINT NOT NULL,
    product_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (product_id)
        REFERENCES products (id)
);