CREATE TABLE pedidos_items (
	id BIGSERIAL,
	preco NUMERIC(8,2) NOT NULL,
	quantidade INTEGER NOT NULL,
	nome_produto VARCHAR(255) NOT NULL,
	primeira_imagem VARCHAR(100) NOT NULL,
	produto_id BIGINT NOT NULL,
	pedido_id BIGINT NOT NULL,
	PRIMARY KEY(id),
	FOREIGN KEY (pedido_id)
	 REFERENCES pedidos (id),
	FOREIGN KEY (produto_id)
	 REFERENCES produtos (id)
);