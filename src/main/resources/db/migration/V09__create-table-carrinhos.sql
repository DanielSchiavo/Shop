CREATE TABLE carrinhos (
	id BIGSERIAL,
	cliente_id BIGINT NOT NULL,
	PRIMARY KEY(id),
	FOREIGN KEY (cliente_id)
	 REFERENCES clientes (id)
);

