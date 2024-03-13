CREATE TABLE produtos_tipo_entrega (
	id BIGSERIAL,
	tipo_entrega VARCHAR(50) NOT NULL,
	produto_id BIGINT NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (produto_id)
	 REFERENCES produtos (id)
);