CREATE TABLE carts (
    customer_id BIGINT,
    total_value NUMERIC(12,2),
    last_update TIMESTAMP,
    PRIMARY KEY (customer_id),
    FOREIGN KEY (customer_id)
                   REFERENCES customers (id)
);