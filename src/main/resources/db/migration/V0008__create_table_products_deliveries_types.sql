CREATE TABLE products_deliveries_types (
    id BIGSERIAL,
    delivery_type VARCHAR(50),
    product_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (product_id)
        REFERENCES products (id)
)