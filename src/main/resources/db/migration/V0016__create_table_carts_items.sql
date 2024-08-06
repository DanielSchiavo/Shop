CREATE TABLE carts_items (
    id BIGSERIAL,
    quantity INT,
    product_id BIGINT,
    sub_total NUMERIC (12,2),
    insertion_date_time TIMESTAMP,
    update_date_time TIMESTAMP,
    cart_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (cart_id)
        REFERENCES carts (customer_id)
);