CREATE TABLE produtos (
	id BIGSERIAL,
	nome VARCHAR(255) NOT NULL UNIQUE,
	descricao TEXT NOT NULL,
	preco NUMERIC(8,2) NOT NULL,
	quantidade INTEGER NOT NULL,
	ativo BOOLEAN NOT NULL,
	categoria_id BIGINT NOT NULL,
	sub_categoria_id BIGINT NOT NULL,
	PRIMARY KEY(id),
	FOREIGN KEY (categoria_id)
	 REFERENCES categorias (id),
	FOREIGN KEY (sub_categoria_id)
	 REFERENCES sub_categorias (id)
);