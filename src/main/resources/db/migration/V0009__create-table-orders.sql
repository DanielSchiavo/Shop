CREATE TABLE orders (
    id UUID DEFAULT gen_random_uuid(),
    total_value NUMERIC(12,2) NOT NULL,
    customer_name VARCHAR(200) NOT NULL,
    cpf CHAR(11) NOT NULL,
    customer_id BIGINT NOT NULL,
    order_time TIMESTAMP NOT NULL,
    expiry_time TIMESTAMP NOT NULL,
    purchased_via_cart BOOLEAN NOT NULL,
    order_status VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (customer_id)
        REFERENCES customers (id)
);