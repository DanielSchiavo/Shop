CREATE TABLE customers_roles (
    id BIGSERIAL NOT NULL,
    assignment_date_time TIMESTAMP NOT NULL,
    role VARCHAR(20) NOT NULL,
    PRIMARY KEY (id),
        FOREIGN KEY (id)
            REFERENCES customers (id)
            ON DELETE CASCADE
);