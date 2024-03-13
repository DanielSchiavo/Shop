CREATE TABLE carrinhos_items (
	id BIGSERIAL,
	quantidade INTEGER NOT NULL,
	produto_id BIGINT NOT NULL,
	carrinho_id BIGINT NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (produto_id)
	 REFERENCES produtos (id),
	FOREIGN KEY (carrinho_id)
	 REFERENCES carrinhos (id)
);