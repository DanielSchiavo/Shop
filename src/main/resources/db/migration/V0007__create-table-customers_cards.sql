CREATE TABLE customers_cards (
    id BIGSERIAL,
    bank_name VARCHAR(100) NOT NULL,
    card_number VARCHAR(16) NOT NULL,
    name_on_card VARCHAR(200) NOT NULL,
    expiration_date CHAR(5) NOT NULL,
    card_type VARCHAR(50) NOT NULL,
    customer_id BIGINT NOT NULL,
    is_default BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (customer_id)
        REFERENCES customers (id)
);