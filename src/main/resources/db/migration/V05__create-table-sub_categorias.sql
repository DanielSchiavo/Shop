CREATE TABLE sub_categorias (
	id BIGSERIAL,
	nome VARCHAR(100) NOT NULL,
	categoria_id BIGINT NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (categoria_id)
	 REFERENCES categorias (id)
);