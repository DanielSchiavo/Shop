CREATE TABLE orders_payments (
    id BIGSERIAL,
    payment_method VARCHAR(50),
    payment_status VARCHAR(50),
    paymentDateTime TIMESTAMP,
    bank_name VARCHAR(100),
    card_number CHAR(16),
    name_on_card VARCHAR(200),
    expiration_date CHAR(5),
    card_type VARCHAR(50) NOT NULL,
    number_of_installments SMALLINT NOT NULL,
    order_id CHAR(36),
    PRIMARY KEY (id),
    FOREIGN KEY (order_id)
        REFERENCES orders (id)
);