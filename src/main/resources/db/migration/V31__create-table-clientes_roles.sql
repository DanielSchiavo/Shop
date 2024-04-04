CREATE TABLE clientes_roles (
    id BIGINT NOT NULL,
    data_atribuicao TIMESTAMP NOT NULL,
    role VARCHAR(20) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id) 
     REFERENCES clientes (id) 
      ON DELETE CASCADE
);
