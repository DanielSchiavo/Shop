CREATE TABLE orders_deliveries (
    id BIGSERIAL,
    delivery_type VARCHAR(50) NOT NULL,
    order_id UUID,
    PRIMARY KEY (id),
    FOREIGN KEY (order_id)
        REFERENCES orders (id)
);