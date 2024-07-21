CREATE TABLE categories (
    id BIGSERIAL,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    image VARCHAR(100),
    parent_category_id BIGINT,
    PRIMARY KEY (id)
);