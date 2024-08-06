CREATE TABLE files_references (
    file_name VARCHAR(100),
    directory VARCHAR(255),
    created_at TIMESTAMP,
    content_type VARCHAR(50),
    content_length BIGINT,
    temp BOOLEAN,
    type VARCHAR(100),
    is_public_accessible BOOLEAN,
    PRIMARY KEY (file_name, directory)
);