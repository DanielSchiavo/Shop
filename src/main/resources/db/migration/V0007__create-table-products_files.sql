CREATE TABLE products_files (
    id BIGSERIAL,
    file_name VARCHAR(100),
    type VARCHAR(50),
    url_video VARCHAR(255),
    position SMALLINT NOT NULL,
    product_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (product_id)
        REFERENCES products (id)
);