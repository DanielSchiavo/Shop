CREATE TABLE orders_deliveries (
    id BIGSERIAL,
    delivery_type VARCHAR(50) NOT NULL,
    order_id CHAR(36),
    PRIMARY KEY (id),
    FOREIGN KEY (order_id)
        REFERENCES orders (id)
);